package org.fogbeam.quoddy;

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.StreamItemBase
import org.fogbeam.quoddy.stream.constants.EventTypes

class EventStreamService 
{
	def userService;
	def jmsService;
	def eventQueueService;
	def existDBService;
	def userGroupService;
	def userListService;
	
	public void saveActivity( ActivityStreamItem activity )
	{
		log.debug( "about to save activity...");
		if( !activity.save(flush:true) )
		{
			log.error( "Saving activity FAILED");
			activity.errors.allErrors.each { log.debug( it ) };
		}
		else 
		{
			log.debug( "Successfully saved ActivityStreamItem: ${activity.id}" );	
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
										final UserStreamDefinition userStream 
								     )
	{
		
		log.debug( "getting recent activities for user, using stream: ${userStream}" );
		log.debug( "streamId = ${userStream.id}");
		
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
	
		  Additional NOTE: now that we are making the event queue stuff "stream aware", we have more
		  of a possibility of something being pushed onto the queue which matches NO sream and
		  will NEVER be read from the queue at all.  So we really need to implement a "reaper" job
		  that will reap the old messages from the queue so it doesn't keep unused messages around
		  forever.  Events will still be in the db if the user ever configures their stream(s)
		  such that that message should appear.
		  
		*/
		
		int msgsOnQueue = eventQueueService.getQueueSizeForUser( user.userId, userStream );
		log.debug( "Messages available on queue: ${msgsOnQueue}");
		log.debug( "MaxCount requested: ${maxCount}");
		
		int msgsToRead = 0;
		if( msgsOnQueue > 0 )
		{
			if( msgsOnQueue <= maxCount )
			{
				msgsToRead = msgsOnQueue;
			}
			else
			{
				// msgsToRead = maxCount - msgsOnQueue;
				msgsToRead = maxCount;
			}
		}
		
		log.debug( "Messages to read from queue: ${msgsToRead}");
		
		long oldestOriginTime = new Date().getTime();
		
		// NOTE: we could avoid iterating over this list again by returning the "oldest message time"
		// as part of this call.  But it'll mean wrapping this stuff up into an object of some
		// sort, or returning a Map of Maps instead of a List of Maps
		List<ActivityStreamItem> messages = eventQueueService.getMessagesForUser( user.userId, msgsToRead, userStream );
		log.debug( "read ${messages?.size()} messages from the queue for user: ${user.userId}");
        
        for( ActivityStreamItem msg : messages )
		{
			log.debug( "msg.published.time: ${msg.published.time}");
			if( msg.published.time < oldestOriginTime )
			{
				oldestOriginTime = msg.published.time;
			}
		}
		
		log.debug( "oldestOriginTime: ${oldestOriginTime}");
		log.debug( "as date: " + new Date( oldestOriginTime ) );
		
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
			if (!activityStreamItem.isAttached())
			{
				activityStreamItem.attach()
			}
			
			// log.debug("got message: ${msg} off of queue");
	
			activityStreamItem.streamObject = existDBService.populateSubscriptionEventWithXmlDoc( activityStreamItem.streamObject );
			
			recentActivityStreamItems.add( activityStreamItem );
		}
		
		log.debug( "recentActivityStreamItems.size() = ${recentActivityStreamItems.size()}");
		
		/* NOTE: here, we need to make sure we don't retrieve anything NEWER than the OLDEST
		 * message we may have in hand - that we received from the queue.  Otherwise, we risk
		 * showing the same event twice.
		 */
				
        log.info( "About to read activity stream items from the DB" );
		// now, do we need to go to the DB to get some more activities?
		if( maxCount > msgsToRead )
		{
				
			int recordsToRetrieve = maxCount - msgsToRead;
			log.info( "retrieving up to ${recordsToRetrieve} records from the database");
			
			// NOTE: get up to recordsToRetrieve records, but don't retrieve anything that
			// would already be in our working set.
			// also... we need to make a distinction between the "get recent" method which has
			// this cutoff logic and the generic "get older" method that can be used to incrementally
			// step backwards into history as far as (they want to go | as far as we let them go)
			
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -2160 );
			Date cutoffDate = cal.getTime();
			
			log.debug( "Using ${cutoffDate} as cutoffDate");
			log.debug("Using ${new Date(oldestOriginTime)} as oldestOriginTime");
					
						
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
			
			
			User dummyUser = userService.findUserByUserId( "SYS_dummy_user" );
			UserGroup dummyGroup = userGroupService.findUserGroupByName( "SYS_dummy_group" );

			/* do a separate query to find any UserGroups for this User.  That means
			 * any group the user owns, OR is a member of.  Then compile the list of
			 * group members from all of those groups, into a collection that we
			 * can check for membership in.
			 */
			List<UserGroup> groupsForUser = userGroupService.getAllGroupsForUser(user);
			List<String> includedGroups = new ArrayList<String>(); // to hold the UUID's of the groups that are valid
                                                                   // values for the targetUuid of items to be included via group
            Set<User> validOwners = new HashSet<User>();
			
            validOwners.add( dummyUser ); // a dummy item that can't match anything, needed
                                                // because Postgresql chokes on "in" queries with an empty collection passed as a parameter
            
			includedGroups.add( dummyGroup.uuid ); // again, a dummy item since queries break on empty collections
			
            
			log.debug( "found: ${groupsForUser?.size()} groups for user ${user}");
			
			if( groupsForUser != null && groupsForUser.size() > 0 )
			{
				log.debug( "found some groups to add to validOwners!");
				for( UserGroup groupForUser : groupsForUser )
				{
					log.debug( "adding group members for group ${groupForUser}");
					validOwners.addAll( groupForUser.groupMembers );
                    includedGroups.add( groupForUser.uuid );
				}	
			}			
			else
			{
				log.debug( "no groups, so adding only current user to validOwners collection");
				// adding the user to this collection, because anything that returns is something
				// we'd get anyway, and the query breaks if you use an empty collection here.
				
				// NOTE: we might need to revisit this, and make sure this won't pull in something
				// we didn't actually want. Especially if we make the setting for "include my own
				// items" explicit with an "includeSelf" setting. 
				
				validOwners.add( user );
			}
			
			/* log.debug( "validOwners.size() = ${validOwners.size()}");
			for( User validOwner : validOwners )
			{
				log.debug( "valid owner: ${validOwner}");
			}
			*/
			
			// NOTE: could we drop the idea of having both a "friends" list AND a "validOwners"
			// list, and collapse those into one thing, to make this query simpler?  The fundamental
			// idea is the same:  generate a list of people who's posts we are willing to accept,
			// regardless of the reason *why* we're willing to accept them.  
			
			
			def parameters = [];
// 			String query = "select item from ActivityStreamItem as item, UserStreamDefinition as stream ";
			String query = "select item from ActivityStreamItem as item left outer join item.streamObject as streamObject left outer join streamObject.owningSubscription as owningSubscription";
			log.debug( "base query: ${query}");
	
			// There are two major divisions in how this works.  
			
			// IF userStream.includeEverything
			// is turned on, then we include: all items owned by the current user, regardless of
			// their type or source (eg, subscription); all posts by all friends and followed users;
			// all posts to all groups the user is in; all posts by all users in any userlists
			// owned by this user.  
			if( userStream.includeEverything )
			{	
				log.info( "userStream.includeEverything = TRUE" );
				
				query = query + " where item.published >= :cutoffDate " +
								" and item.published < :oldestOriginTime " +
								" and ( ";
																			
				query = query +
							" ( item.owner.id in (:friendIds) " +
								" and not ( item.owner.id <> :ownerId and item.objectClass = '${EventTypes.BUSINESS_EVENT_SUBSCRIPTION_ITEM.name}' ) " +
								" and not ( item.owner.id <> :ownerId and item.objectClass = '${EventTypes.CALENDAR_FEED_ITEM.name}' ) " +
								" and not ( item.owner.id <> :ownerId and item.objectClass = '${EventTypes.RSS_FEED_ITEM.name}' ) " +
								" and not ( item.owner.id <> :ownerId and item.objectClass = '${EventTypes.ACTIVITI_USER_TASK.name}' ) " +
								" and item.targetUuid = :targetUuid " +
							") or ";
	
										
				query = query +
								
						"(" +
							" item.targetUuid = :userUuid " + 
						") or ";
						    
				// include any posts to groups this user is a member of, or owns
				// TODO: this should always only include StatusUpdates, but we should add that to the
				// query here to make it explicit, in case something changes in the future.
                   query = query + 
                       "( item.targetUuid in (:includedGroups) )";
                    				
				// NOTE: no need to deal with userlists here, since you can only add
				// a user to a UserList if you are friends with them anyway? (For now).
				// if you could create userlists without friending first, that would effectively
				// just be adding "follow" support in a multi-user fashion, when we don't
				// even officially have a "follow" implemented anyway. 
														
				query = query +") ";
					
				log.debug( "query now: ${query}");
				// query = query + " and stream.id = :streamId "; 
				query = query + " order by item.published desc";
				
				log.debug( "Final query before execution: $query" );
				
				log.debug( "Found ${friends.size()} friends");
				List<Integer> friendIds = new ArrayList<Integer>();
				for( User friend: friends )
				{
					def friendId = friend.id;
					log.debug( "Adding friend id: ${friendId}, userId: ${friend.userId} to list" );
					friendIds.add( friendId );
				}
	
								
				// for the purpose of this query, treat a user as their own friend... that is, we
				// will want to read Activities created by this user (we see our own updates in our
				// own feed)
				friendIds.add( user.id );
				log.debug( "friendIds has ${friendIds.size()} entries!");
				
				ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
				
				parameters =
						['cutoffDate':cutoffDate,
						 'oldestOriginTime':new Date(oldestOriginTime),
						 'targetUuid':streamPublic.uuid,
						 // 'streamId':userStream.id,
						 'userUuid': user.uuid,
                         'includedGroups':includedGroups]
			
						log.debug( "Using parameters map: ${parameters}");
				if( !userStream.includeSelfOnly )
				{
					parameters << ['friendIds':friendIds];
				}
				
				log.debug( "Using parameters map: ${parameters}");
				
				if( userStream.includeSelfOnly || ( userStream.userUuidsIncluded == null || userStream.userUuidsIncluded.isEmpty() ) )
				{
					parameters << ['ownerId':user.id];
				}
						
				log.debug( "Using parameters map: ${parameters}" );
				
				
				log.debug( "Executing query NOW:");
	
			}
			
			
			// ELSE
			// we ONLY include things that are specifically selected in the stream
			// definition.  This means a non "include everything" selection here
			// won't even include the user's own activity unless they explicitly
			// chose to include it.  
			else
			{
				log.info( "userStream.includeEverything = FALSE" );

				log.debug( "Query begins as: \n${query}" );
				
	
				query = query + " where item.published >= :cutoffDate " +
								" and item.published < :oldestOriginTime " +
								" and ( ";
								
								
										/* deal with user filter */
										if( userStream.includeAllUsers )
										{
											log.debug( "includeAllUsers == true case" );
											
											/* NOTE: We may never turn this on, as it may not be a desirable use
											 * case.  Basically, this is saying "show me posts from every user
											 * in the system.  If we turn that on, we have to deal with the possiblity
											 * that some users may not wnat me to see their posts.  That adds another
											 * level of complexity to deal with.  As it is now, you have to
											 * accept somebody's friend request before they can see your posts (although
											 * the idea of "follow" support changes that dynamic as well!)  Basically
											 * it's going to get complicated deciding how to control who can see
											 * what.
											 */
											
											// nothing to do, default query will return hits from all eligible users
											// query = query + "("
										}
										else if( userStream.includeSelfOnly )
										{
											log.debug( "includeSelfOnly == true case" );
											
											// left alone, the existing default query would return posts from any
											// user in the friends list.  We should rework the entire base query
											// but - for now - we can cheat and just add an extra and clause to
											// filter down to the user id of our user
											
											query = query + " ( item.owner.id = :ownerId ) ";
										}
										else if( ( userStream.userUuidsIncluded != null && !userStream.userUuidsIncluded.isEmpty() ) || userStream.includeSelf )
										{
											log.debug( "userUuidsIncluded case OR includeSelf case");
											
											// this means that neither "include all" nor "include self only" was turned on
											// so here work off the specific list of included users  
											// Set up the query here to work off the userUuidsIncluded list
											// NOTE: we enter this block if includeSelf is turned on, even if the
											// userUuidsIncluded list is empty. That's becuase when includeSelf is
											// turned on, we add the user himself to the list of User UUIDs we
											// query against.  See below.
											
											// NOTE: this should only return "user stuff", so exclude, for example
											// subscription items here.  
											// query = query + " and ( ";
											query = query + " (item.owner.uuid in (:userUuidsIncluded)" + 
											" and not item.objectClass = '${EventTypes.BUSINESS_EVENT_SUBSCRIPTION_ITEM.name}'" +
											" and not item.objectClass = '${EventTypes.CALENDAR_FEED_ITEM.name}'" +
											" and not item.objectClass = '${EventTypes.RSS_FEED_ITEM.name}'" +
											" and not item.objectClass = '${EventTypes.ACTIVITI_USER_TASK.name}'" +
											" and item.targetUuid = :targetUuid " +
						 					") ";
										}
										else
										{
											query = query + " ( true = false ) ";
										}
																																
				log.debug( "query now: ${query}" );
				
				if( userStream.userGroupUuidsIncluded != null && !userStream.userGroupUuidsIncluded.isEmpty())
				{
					// deal with including group posts here
					// include any posts to groups this user is a member of, or owns
					// TODO: sort out how eventtypes factor in here. For now we assume that groups
					// only receive StatusUpdates and just include any item for a selected group 
					query = query +
						" or ( item.targetUuid in (:includedGroups) )";
				}
				
				
				if( userStream.subscriptionUuidsIncluded != null && !userStream.subscriptionUuidsIncluded.isEmpty())
				{
					//  deal with including subscription items
					query = query + 
						" or ( owningSubscription.uuid in (:includedSubscriptions) )"; 
				}
				
				
				if( userStream.userListUuidsIncluded != null && !userStream.userListUuidsIncluded.isEmpty() )
				{
					//  deal with including items based on user lists
					query = query + 
						" or ( item.owner.uuid in (:includedUserListUsers) ) "; 
				}
					
				// query = query + " and stream.id = :streamId " 
				query = query + ") order by item.published desc";
							
				log.info( "executing query: $query" );
				
				log.debug( "Found ${friends.size()} friends");
				List<Integer> friendIds = new ArrayList<Integer>();
				for( User friend: friends )
				{
					def friendId = friend.id;
					log.debug( "Adding friend id: ${friendId}, userId: ${friend.userId} to list" );
					friendIds.add( friendId );
				}
	
				// for this version, we do NOT add ourself to the friends list by default, since that
				// would mean picking up all sorts of things we might not want, where "we" are the owner
				// (eg, subscription items, etc.)				
				// friendIds.add( user.id );
				
				ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
				
				parameters =
						['cutoffDate':cutoffDate,
						 'oldestOriginTime':new Date(oldestOriginTime),
						 // 'streamId':userStream.id,
						 // 'userUuid': user.uuid
						 ]
			
				log.debug( "Using parameters map: ${parameters}" );
				if( !userStream.includeSelfOnly )
				{
					// parameters << ['friendIds':friendIds];
				}
				
				log.debug( "Using parameters map: ${parameters}" );
				
				if( ( userStream.userUuidsIncluded != null && !userStream.userUuidsIncluded.isEmpty() ) || userStream.includeSelf )
				{
					List<String> userUuidsIncluded = new ArrayList<String>();
					userUuidsIncluded.addAll( userStream.userUuidsIncluded );
					
					if( userStream.includeSelf )
					{
						userUuidsIncluded.add( user.uuid );
					}
					
					parameters << ['userUuidsIncluded':userUuidsIncluded, 'targetUuid':streamPublic.uuid];
				}
						
				if( userStream.userGroupUuidsIncluded != null && !userStream.userGroupUuidsIncluded.isEmpty())
				{
					parameters <<['includedGroups':userStream.userGroupUuidsIncluded];
				}
				
				if( userStream.subscriptionUuidsIncluded != null && !userStream.subscriptionUuidsIncluded.isEmpty())
				{
					parameters << ['includedSubscriptions':userStream.subscriptionUuidsIncluded];
				}

				if( userStream.userListUuidsIncluded != null && !userStream.userListUuidsIncluded.isEmpty() )
				{
					List<String> includedUserListUsers = new ArrayList<String>();
					
					for( String userListUuid : userStream.userListUuidsIncluded )
					{
						// look up user list by UUID
						UserList list = userListService.findUserListByUuid( userListUuid );
						
						list.members.each {
							includedUserListUsers.add( it.uuid );
						}
						
					}					
					
					parameters << ['includedUserListUsers': includedUserListUsers ];
				}
												
				log.debug( "Using parameters map: ${parameters}" );
				
				
				log.debug( "Executing query NOW:");
	
			} // END handling the "not include everything" case.

			
						
			
			log.info( "Query to be executed:\n\n\n" + query + "\n\n\n" );
			
			log.info( "Using parameters map:\n\n\n" + parameters + "\n\n\n" );
			
			List<ActivityStreamItem> queryResults =
				ActivityStreamItem.executeQuery( query, parameters, ['max': maxCount ]);
		
			log.debug( "adding ${queryResults.size()} activities read from DB" );
			
			for( ActivityStreamItem event : queryResults ) 
			{
					
					log.debug( "Populating XML into SubscriptionEvents" );
					log.debug( "event = ${event}" );
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

	public void deleteActivityStreamItemByUuid( final String delItemUuid )
	{
		// TODO: lookup item, then delete it.
		ActivityStreamItem itemToDelete = ActivityStreamItem.findByUuid( delItemUuid );
		
		
		if( itemToDelete != null )
		{
			log.debug( "found it!" );
			itemToDelete.delete();
			
		}
		else
		{
			log.warn( "ActivityStreamItem with uuid ${delItemUuid} not found" );
		}
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
		
		List<ActivityStreamItem> results = 
			ActivityStreamItem.executeQuery( 
				"select actItem from ActivityStreamItem as actItem where actItem.owner = :owner " + 
				" and actItem.verb = :verb and actItem.targetUuid = :targetUuid order by actItem.dateCreated desc",
				['owner':user, verb:'quoddy_status_update', targetUuid:streamPublic.uuid] );
		
		statusUpdatesForUser.addAll( results );
		
		
		return statusUpdatesForUser;
	}				
}