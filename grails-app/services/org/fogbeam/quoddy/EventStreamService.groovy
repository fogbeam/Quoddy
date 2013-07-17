package org.fogbeam.quoddy;

import java.util.Calendar

import org.fogbeam.quoddy.stream.ActivityStreamItem;
import org.fogbeam.quoddy.stream.EventType;
import org.fogbeam.quoddy.stream.ShareTarget;
import org.fogbeam.quoddy.stream.StreamItemBase;

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
	
	
	public List<ActivityStreamItem> getRecentActivitiesForUser( final User user, final int maxCount, final UserStream userStream )
	{
		
		
		println "getting recent activities for user, using stream: ${userStream}";
		println "streamId = ${userStream.id}";
		
		// ok, in this version we select the events to return, based on the specification defined by the
		// passed in userStream instance.
		
		// in the very earliest cut of this, we're going to ignore messages waiting on the queue and just
		// go straight to the database.  Once we're confident we have all the selectors and filters
		// working that way, we can revisit what to do about queued messages.
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
		
		List<ActivityStreamItem> recentActivityStreamItems = new ArrayList<ActivityStreamItem>();
		
					
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
							" and ( item.owner.id in (:friendIds)  " + 
							        " and not ( item.owner <> :owner and item.objectClass = 'BusinessEventSubscriptionItem' ) " + 
							        " and not ( item.owner <> :owner and item.objectClass = 'CalendarFeedItem' ) " +
									" and not ( item.owner <> :owner and item.objectClass = 'RssFeedItem' ) " +
									" and not ( item.owner <> :owner and item.objectClass = 'ActivitiUserTask' ) " +
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
			
			def parameters = ['cutoffDate':cutoffDate,
					 // 'oldestOriginTime':new Date(oldestOriginTime),
					 'friendIds':friendIds,
					 'targetUuid':streamPublic.uuid, 'owner': user, 'userUuid': user.uuid]
			
			if( !userStream.includeAllUsers && !userStream.includeSelfOnly ) 
			{
				parameters << ['streamId':userStream.id]	
			}
			
			println "Using parameters map: ${parameters}";
			
			List<ActivityStreamItem> queryResults =
				ActivityStreamItem.executeQuery( query,
					parameters,
					['max': maxCount ]);
		
				println "adding ${queryResults.size()} activities read from DB";
				for( ActivityStreamItem event : queryResults ) {
					
					println "Populating XML into SubscriptionEvents";
					println "event = ${event}";
					event.streamObject = existDBService.populateSubscriptionEventWithXmlDoc( event.streamObject );
					recentActivityStreamItems.add( event );
				}
		}
		else
		{
			println( "no friends, so no activity read from DB" );
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