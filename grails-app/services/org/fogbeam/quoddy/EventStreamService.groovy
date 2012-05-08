package org.fogbeam.quoddy;

import java.util.Calendar

class EventStreamService {

	def userService;
	def jmsService;
	def eventQueueService;
	def existDBService;
	
	public void saveActivity( Activity activity )
	{
		println "about to save activity...";
		if( !activity.save(flush:true) )
		{
			println( "Saving activity FAILED");
			activity.errors.allErrors.each { println it };
		}
		else 
		{
			println "Successfully saved Activity: ${activity.id}";	
		}
		
	}
	
	public List<Activity> getRecentActivitiesForUser( final User user, final int maxCount, final UserStream userStream )
	{
		
		// ok, in this version we select the events to return, based on the specification defined by the
		// passed in userStream instance.
		
		// in the very earliest cut of this, we're going to ignore messages waiting on the queue and just
		// go straight to the database.  Once we're confident we have all the selectors and filters
		// working that way, we can revisit what to do about queued messages.
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -600 );
		Date cutoffDate = cal.getTime();
		
					
		List<User> friends = userService.listFriends( user );
		if( friends != null && friends.size() >= 0 )
		{
			println "Found ${friends.size()} friends";
			List<Integer> friendIds = new ArrayList<Integer>();
			for( User friend: friends )
			{
				def id = friend.id;
				println( "Adding friend id: ${id}, userId: ${friend.userId} to list" );
				friendIds.add( id );
			}
		
			
			String query = "select event from EventBase as event where event.effectiveDate >= :cutoffDate " 
							+ " and event.owner.id in (:friendIds)"  
							+ " and event.targetUuid = :targetUuid ";
			
			if( userStream.includeAllEventTypes )
			{
				// don't do anything, the default query returns all event types
			}				
			else 
			{
				// start limiting the return types based on what we have in the
				// stream object...
				Set<String> eventTypesToInclude = userStream.getEventTypesToInclude();
					
				for( String eventType : eventTypesToInclude ) 
				{
						query = query + " and event.class = " + eventType;
				}
				
				
			}								 
							
			query = query + " order by event.effectiveDate desc";
							
			// for the purpose of this query, treat a user as their own friend... that is, we
			// will want to read Activities created by this user (we see our own updates in our
			// own feed)
			friendIds.add( user.id );
			ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
			List<EventBase> queryResults =
				EventBase.executeQuery( queryString,
					['cutoffDate':cutoffDate,
					 'oldestOriginTime':new Date(oldestOriginTime),
					 'friendIds':friendIds,
					 'targetUuid':streamPublic.uuid],
					['max': recordsToRetrieve ]);
		
				println "adding ${queryResults.size()} activities read from DB";
				for( EventBase event : queryResults ) {
					
					println "Populating XML into SubscriptionEvents";
					
					event = existDBService.populateSubscriptionEventWithXmlDoc( event );
					recentEvents.add( event );
				}
		}
		else
		{
			println( "no friends, so no activity read from DB" );
		}
		
		
		
		return null;
	}
	
	public List<Activity> getRecentActivitiesForUser( final User user, final int maxCount )
	{
		println "getRecentActivitiesForUser: ${user.userId} - ${maxCount}";
		/*
		 
		 so what do we do here?  Ok... we receive a request for up to maxCount recent activities.
		 Since, by definition, the stuff in the queue is most recent, we read up to maxCount entries
		 from the queue. If the queue has more than maxCount activities we ??? (what? Blow away the
		 extras? Leave 'em hanging around for later? Force a flush to the db? ???)
		 
		 If the queue had less than maxCount records (down to as few as NONE), we retrieve
		 up to (maxCount - readfromQueueCount) matching records from the db. 
		
		 The resulting list is the union of the set of activities retrieved from the queue and
		 the activities loaded from the DB.
		
		 Note: Since we really want to show "newest at top" or "newest first" we really wish this
		 "queue" were actually a stack, so we'd be starting with the newest messages and
		 getting progressively older ones.  We need to explore the possibility of having our
		 underlying messaging system offer stack semantics, OR implement an intermediate 
		 queue, that reads the messages from the underlying messaging fabric, and offers them
		 to us in the right order.  Possibly explore using Camel for this, or roll our own
		 thing?
		
		 we could also just read everything that's currently on the queue, sort by timestamp,
		 use up to maxCount of the messages, and then throw away anything that's left-over.
		 but if we do too much of this, we wind up throwing away a lot of queued messages, which
		 negates the benefit of not having to read from the DB.
		
		 ok, just to get something prototyped... let's pretend that the queue we're reading from
		 right here *is* the "intermediate queue" and everything is just magically in the right order.
		 "no problem in computer science that you can't solve by adding a layer of abstraction" right?
		
		  Also, for now let's pretend that the queue we're reading from has already been filtered so that
		  it only contains messages that we are interested in; including expiring messages for age, etc.
		
		*/
		
		int msgsOnQueue = eventQueueService.getQueueSizeForUser( user.userId );
		println "Messages available on queue: ${msgsOnQueue}";
		int msgsToRead = 0;
		if( msgsOnQueue > 0 )
		{
			if( msgsOnQueue <= maxCount )
			{
				msgsToRead = msgsOnQueue;
			}
			else 
			{
				msgsToRead = maxCount - msgsOnQueue;	
			}
		}
		
		println "Messages to read from queue: ${msgsToRead}";
		
		// long oldestOriginTime = Long.MAX_VALUE;
		long oldestOriginTime = new Date().getTime();
		
		// NOTE: we could avoid iterating over this list again by returning the "oldest message time"
		// as part of this call.  But it'll mean wrapping this stuff up into an object of some
		// sort, or returning a Map of Maps instead of a List of Maps
		List<EventBase> messages = eventQueueService.getMessagesForUser( user.userId, msgsToRead );
		for( EventBase msg : messages )
		{
			// println "msg.originTime: ${msg.originTime}";
			if( msg.effectiveDate.time < oldestOriginTime )
			{
				oldestOriginTime = msg.effectiveDate.time;
			}
		}
		
		println "oldestOriginTime: ${oldestOriginTime}";
		println "as date: " + new Date( oldestOriginTime);
		
		List<EventBase> recentEvents = new ArrayList<EventBase>();
		
		// NOTE: we wouldn't really want to iterate over this list here... better
		// to build up this list above, and never bother storing the JMS Message instances
		// at all...  but for now, just to get something so we can prototype the
		// behavior up through the UI...
		
		// TODO: now that we are passing notifications for different kinds of
		// events through this mechanism, this has to be smarter... It can't just
		// conver everything to an activity, it has to convert to Activity, SubscriptionEvent,
		// CalendarEvent, etc., depending on what the notification is for.
		for( int i = 0; i < messages.size(); i++ )
		{
			EventBase event = messages.get(i);
			// println "got message: ${msg} off of queue";
			
			event = existDBService.populateSubscriptionEventWithXmlDoc( event );
			
			recentEvents.add( event );				
		}
		
		println "recentEvents.size() = ${recentEvents.size()}"
		
		/* NOTE: here, we need to make sure we don't retrieve anything NEWER than the OLDEST
		 * message we may have in hand - that we received from the queue.  Otherwise, we risk
		 * showing the same event twice.
		 */
		
		// now, do we need to go to the DB to get some more activities?
		if( maxCount > msgsToRead ) 
		{
			int recordsToRetrieve = maxCount - msgsToRead;
			println "retrieving up to ${recordsToRetrieve} records from the database";
			
			// NOTE: get up to recordsToRetrieve records, but don't retrieve anything that
			// would already be in our working set.
			// also... we need to make a distinction between the "get recent" method which has
			// this cutoff logic and the generic "get older" method that can be used to incrementally
			// step backwards into history as far as (they want to go | as far as we let them go)
			
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -600 );
			Date cutoffDate = cal.getTime();
			
			println "Using ${cutoffDate} as cutoffDate";
			println "Using ${new Date(oldestOriginTime)} as oldestOriginTime";
						
			List<User> friends = userService.listFriends( user );
			if( friends != null && friends.size() >= 0 ) 
			{
				println "Found ${friends.size()} friends";
				List<Integer> friendIds = new ArrayList<Integer>();
				for( User friend: friends )
				{
					def id = friend.id;
					println( "Adding friend id: ${id}, userId: ${friend.userId} to list" );
					friendIds.add( id );
				}
			
				
				// for the purpose of this query, treat a user as their own friend... that is, we
				// will want to read Activities created by this user (we see our own updates in our
				// own feed)
				friendIds.add( user.id );
				ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
				List<EventBase> queryResults = 
					EventBase.executeQuery( "select event from EventBase as event where event.effectiveDate >= :cutoffDate and event.owner.id in (:friendIds) and event.effectiveDate < :oldestOriginTime and event.targetUuid = :targetUuid order by event.effectiveDate desc",
						['cutoffDate':cutoffDate, 
						 'oldestOriginTime':new Date(oldestOriginTime), 
						 'friendIds':friendIds, 
						 'targetUuid':streamPublic.uuid], 
					    ['max': recordsToRetrieve ]);
			
					println "adding ${queryResults.size()} activities read from DB";
					for( EventBase event : queryResults ) {
						
						println "Populating XML into SubscriptionEvents";
						
						event = existDBService.populateSubscriptionEventWithXmlDoc( event );
						recentEvents.add( event );
					}
			}
			else
			{
				println( "no friends, so no activity read from DB" );	
			}
		}
		else
		{
			println "Reading NO messages from DB";	
		}
		
		println "recentEvents.size() = ${recentEvents.size()}";
		return recentEvents;
	}	
}