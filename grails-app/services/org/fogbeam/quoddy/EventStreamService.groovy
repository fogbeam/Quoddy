package org.fogbeam.quoddy;

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.EventType
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.StreamItemBase
import org.fogbeam.quoddy.stream.constants.EventTypeNames

class EventStreamService {

	def userService;
	def jmsService;
	def eventQueueService;
	def existDBService;
	
	public void saveActivity( ActivityStreamItem activity )
	{
		println "about to save activity...";
		if( !activity.save(flush:true) )
		{
			println( "Saving activity FAILED");
			activity.errors.allErrors.each { println it };
		}
		else 
		{
			println "Successfully saved ActivityStreamItem: ${activity.id}";	
		}
		
	}
	
	
	
	
	
	/* NOTE: note to self, we got rid of the overload of this method that does not take a UserStream
	 * parameter, and now we assume that we'll always have SOME UserStream in play, even if it's the
	 * (pre-defined, system) "default" stream.  So now we need to merge some logic that had originally been
	 * written into *that* overload, that never made it into this one, that manages retrieving messages
	 * from the userQueue, and then adjusting the db query to allow for messages retrieved from the queue.
	 * 
	 *  Part of the problem with this, is that we can no longer blindly read messages off of the queue, since
	 *  they might not match the filters of the selected stream, and therefore should not appear in the
	 *  current result set.  Now we'll have to make the cache stream aware.  That probably means that the
	 *  poll method that updates the notification count in the UI, will also have to be made stream aware.
	 *  
	 *  further note: this will lead to weirdness, like "I'm sitting here watching stream X, and a new message
	 *  comes in that matches stream Y, but not stream X, so the user won't (given the way notifications work now)
	 *  see any visible indicator that something new is waiting for them.  Sooo... do we need two notifications, one
	 *  that's stream specific, and one that's system-wide?  Hmmm...   
	 */
	
	public List<ActivityStreamItem> getRecentActivitiesForUser( 
									    final User user, 
										final int maxCount, 
										final UserStream userStream 
								     )
	{
		
		println "getting recent activities for user, using stream: ${userStream}";
		println "streamId = ${userStream.id}";
		
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
		
		 Also, for now let's pretend that the queue we're reading from has aleady been filtered so that
		 it only contains messages that we are interested in; including expiring messages for age, etc.
	
		  Additional NOTE: now that we are making the event queue stuff "stream aware", we have more
		  of a possibility of something being pushed onto the queue which matches NO sream and
		  will NEVER be read from the queue at all.  So we really need to implement a "reaper" job
		  that will reap the old messages from the queue so it doesn't keep unused messages around
		  forever.  Events will still be in the db if the user ever configures their stream(s)
		  such that that message should appear.
		  
		*/
		
		int msgsOnQueue = eventQueueService.getQueueSizeForUser( user.userId, userStream );
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
		List<ActivityStreamItem> messages = eventQueueService.getMessagesForUser( user.userId, msgsToRead, userStream );
		for( ActivityStreamItem msg : messages )
		{
			// println "msg.originTime: ${msg.originTime}";
			if( msg.published.time < oldestOriginTime )
			{
				oldestOriginTime = msg.published.time;
			}
		}
		
		println "oldestOriginTime: ${oldestOriginTime}";
		println "as date: " + new Date( oldestOriginTime);
		
		List<ActivityStreamItem> recentActivityStreamItems = new ArrayList<ActivityStreamItem>();
		
		// NOTE: we wouldn't really want to iterate over this list here... better
		// to build up this list above, and never bother storing the JMS Message instances
		// at all...  but for now, just to get something so we can prototype the
		// behavior up through the UI...
		
		// TODO: now that we are passing notifications for different kinds of
		// events through this mechanism, this has to be smarter... It can't just
		// convert everything to an activity, it has to convert to Activity, SubscriptionEvent,
		// CalendarEvent, etc., depending on what the notification is for.
		for( int i = 0; i < messages.size(); i++ )
		{
			ActivityStreamItem activityStreamItem = messages.get(i);
			// println "got message: ${msg} off of queue";
			
			activityStreamItem.streamObject = existDBService.populateSubscriptionEventWithXmlDoc( activityStreamItem.streamObject );
			
			recentActivityStreamItems.add( activityStreamItem );
		}
		
		println "recentActivityStreamItems.size() = ${recentActivityStreamItems.size()}"
		
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
			cal.add(Calendar.HOUR_OF_DAY, -2160 );
			Date cutoffDate = cal.getTime();
			
			println "Using ${cutoffDate} as cutoffDate";
			println "Using ${new Date(oldestOriginTime)} as oldestOriginTime";
					
						
			List<User> friends = userService.listFriends( user );
			
			/* NOTE: we're changing this to remove the check for 
			 *
			 *     if( friends != null && friends.size() >= 0 )
			 *
			 *  Because we "cheat" further down and force the user to be his/her own friend
			 *  so users see their own posts in their stream.  Since we *always* have to have
			 *  a valid friends collection, if we get a null collection here, we just instantiate a
			 *  new, empty list.  Likewise, if we get back an empty list, we don't have to do
			 *  anything special, as we're going to add the user to it manually anyway. 
			 */
			
			
			println "Found ${friends.size()} friends";
			List<Integer> friendIds = new ArrayList<Integer>();
			for( User friend: friends )
			{
				def id = friend.id;
				println( "Adding friend id: ${id}, userId: ${friend.userId} to list" );
				friendIds.add( id );
			}
		
			
			String query = "select item ";
			println "query now: ${query}";
			
			
			if( !userStream.includeAllUsers && !userStream.includeSelfOnly ) 
			{
				// println "filtering by user";
				// query = query + ", stream "; 	
			
			}
			println "query now: ${query}";
			
			
			query = query + " from ActivityStreamItem as item ";
			println "query now: ${query}";
			
			
			if( !userStream.includeAllUsers && !userStream.includeSelfOnly )
			{
				println "filtering by user";
				query = query + ", UserStream as stream ";
			}
			println "query now: ${query}";
			
			query = query + " where item.published >= :cutoffDate " +
							" and item.published < :oldestOriginTime " + 
							" and ( item.owner.id in (:friendIds)  " + 
							        " and not ( item.owner <> :owner and item.objectClass = '${EventTypeNames.BUSINESS_EVENT_SUBSCRIPTION_ITEM.name}' ) " + 
							        " and not ( item.owner <> :owner and item.objectClass = '${EventTypeNames.CALENDAR_FEED_ITEM.name}' ) " +
									" and not ( item.owner <> :owner and item.objectClass = '${EventTypeNames.RSS_FEED_ITEM.name}' ) " +
									" and not ( item.owner <> :owner and item.objectClass = '${EventTypeNames.ACTIVITI_USER_TASK.name}' ) " +
								  ") " + 
							" and ( item.targetUuid = :targetUuid or item.targetUuid = :userUuid)";
			
							
			println "query now: ${query}";
			
			
			/* deal with event type filter */
			if( userStream.includeAllEventTypes )
			{
				// don't do anything, the default query returns all event types
				println "selecting ALL event types";
			}				
			else 
			{
				
				println "filtering by event type";
				
				// start limiting the return types based on what we have in the
				// stream object...
				Set<EventType> eventTypesToInclude = userStream.getEventTypesToInclude();
					
				println "query was: ${query}";
				
				boolean first = true;
				for( EventType eventType : eventTypesToInclude ) 
				{
					println "adding eventType: ${eventType}";
					if( first ) 
					{
						query = query + " and (";
						first = false;	
					}
					else 
					{
						query = query + " or ";
						
					}
					query = query + " item.objectClass = " + eventType.name;
				}
				query = query + " )";
				println "query now: ${query}";
				
			}								 
				
			/* deal with user filter */
			if( userStream.includeAllUsers ) 
			{
				// nothing to do, default query will return hits from all eligible users
			}
			else if( userStream.includeSelfOnly )
			{
				// left alone, the existing default query would return posts from any
				// user in the friends list.  We should rework the entire base query
				// but - for now - we can cheat and just add an extra and clause to
				// filter down to the user id of our user
				query = query + " and item.owner = :owner";	
			}
			else 
			{
				println "adding userUuidsIncluded and streamid filters to query";
				query = query + " and item.owner.uuid in elements( stream.userUuidsIncluded ) and stream.id = :streamId ) ";	
			}
			
			/* deal with user list filter */
			
			
			
			/* deal with group filter */
			
			
			
			/* deal with subscription filter */
			
			
			
						
			query = query + " order by item.published desc";
			
			println "executing query: $query";
							
			// for the purpose of this query, treat a user as their own friend... that is, we
			// will want to read Activities created by this user (we see our own updates in our
			// own feed)
			friendIds.add( user.id );
			ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
			
			def parameters = 
					['cutoffDate':cutoffDate,
					 'oldestOriginTime':new Date(oldestOriginTime),
					 'friendIds':friendIds,
					 'targetUuid':streamPublic.uuid, 
					 'owner': user, 
					 'userUuid': user.uuid]
			
			if( !userStream.includeAllUsers && !userStream.includeSelfOnly ) 
			{
				parameters << ['streamId':userStream.id]	
			}
			
			println "Using parameters map: ${parameters}";
			
			List<ActivityStreamItem> queryResults =
				ActivityStreamItem.executeQuery( query, parameters, ['max': maxCount ]);
		
			println "adding ${queryResults.size()} activities read from DB";
			
			for( ActivityStreamItem event : queryResults ) 
			{
					
					println "Populating XML into SubscriptionEvents";
					println "event = ${event}";
					event.streamObject = existDBService.populateSubscriptionEventWithXmlDoc( event.streamObject );
					recentActivityStreamItems.add( event );
			}
		
		}
		
		return recentActivityStreamItems;
	}
	

	
	public StreamItemBase getEventById( final long eventId )
	{
		StreamItemBase event = StreamItemBase.findById( eventId );
		
		return event;
	}

	public StreamItemBase getEventByUuid( final String uuid )
	{
		StreamItemBase event = StreamItemBase.findByUuid( uuid );
		
		return event;
	}

	
	
	public ActivityStreamItem getActivityStreamItemById( final long itemId )
	{
		ActivityStreamItem item = ActivityStreamItem.findById( itemId );
		
		return item;
	}

	public ActivityStreamItem getActivityStreamItemByUuid( final String uuid )
	{
		ActivityStreamItem item = ActivityStreamItem.findByUuid( uuid );
		
		return item;
	}

	
	
	
	
		
	// NOTE: we will probably need a version of this that supports
	// chunking, since returning *every* item in the stream will consume
	// massive memory once the system has been in use for a while.
	public List<StreamItemBase> getAllStreamItems()
	{
		List<StreamItemBase> items = new ArrayList<StreamItemBase>();
		items.addAll( StreamItemBase.findAll() );
		
		return items;
	}

	
	// NOTE: we will probably need a version of this that supports
	// chunking, since returning *every* item in the stream will consume
	// massive memory once the system has been in use for a while.
	public List<ActivityStreamItem> getAllActivityStreamItems()
	{
		/* NOTE: We'll also need a way to not return multiple ASI's that point
		 * to the same streamObject, once we support sharing / re-sharing of stream
		 * entries
		 */
		List<ActivityStreamItem> items = new ArrayList<ActivityStreamItem>();
		items.addAll( ActivityStreamItem.findAll() );
		
		return items;
	}
		
	/*
	 * NOTE: this version just assumes we're only looking for things shared to "Public".
	 * we haven't yet really fully integrated the ability to select different "ShareTarget"
	 * instances, but we'll have to account for that later.
	 */
	public List<ActivityStreamItem> getStatusUpdatesForUser( final User user )
	{
		List<ActivityStreamItem> statusUpdatesForUser = new ArrayList<ActivityStreamItem>();
		
		ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
		
		// TODO: write query to get activitystreamitem (status updates) for a User
		List<ActivityStreamItem> results = 
			ActivityStreamItem.executeQuery( 
				"select actItem from ActivityStreamItem as actItem where actItem.owner = :owner " + 
				" and actItem.verb = :verb and actItem.targetUuid = :targetUuid",
				['owner':user, verb:'quoddy_status_update', targetUuid:streamPublic.uuid] );
		
		statusUpdatesForUser.addAll( results );
		
		
		return statusUpdatesForUser;
	}
	
			
}