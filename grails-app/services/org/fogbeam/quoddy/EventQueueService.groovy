package org.fogbeam.quoddy

import javax.jms.Message

import org.fogbeam.quoddy.social.FriendCollection
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.EventType
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.StreamItemBase
import org.fogbeam.quoddy.stream.constants.EventTypes

class EventQueueService 
{	
	static scope = "singleton";
	
	def userService;
	def eventTypeService;
	
	Map<String, Deque<ActivityStreamItem>> eventQueues = new HashMap<String, Deque<ActivityStreamItem>>();
	
	static expose = ['jms']
	static destination = "uitestActivityQueue"; // TODO: rename this to something more meaningful
	
	def onMessage(msg)
	{
		log.debug( "Message class: ${msg.class}" );
		log.debug( "Received message from JMS: ${msg}" );
		
		
		// now, figure out which user(s) are interested in this message, and put it on
		// all the appropriate queues
		Set<Map.Entry<String, Deque<ActivityStreamItem>>> entries = eventQueues.entrySet();
		log.debug( "got entrySet from eventQueues object: ${entries}" );
		for( Map.Entry<String, Deque<ActivityStreamItem>> entry : entries )
		{
			log.debug( "entry: ${entry}");
			log.debug( "key: ${entry.getKey()}" );
			
			String key = entry.getKey();
			
			
			// TODO: deal with the case where the post was to a UserGroup, not
			// a direct stream post.  In that case, we only offer it to a user if
			// that user is also a member of the same group?
			// BUT, for now, let's just implement it so that we only offer
			// messages that were to the public stream.  We'll come back to deal with
			// common group membership and other scenarios later.
			def streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC);
			ActivityStreamItem event = (ActivityStreamItem)msg.getObject();
			if( ! event.targetUuid.equals( streamPublic.uuid ))
			{
				return;
			}
			
			
			// TODO: don't offer message unless the owner of this queue
			// and the event creator, are friends (or the owner *is* the creator)
			User msgCreator = userService.findUserByUserId( event.owner.userId );
			if( msgCreator )
			{
				log.debug( "found User object for ${msgCreator.userId}");
			}
			else
			{
				log.debug( "No User for ${event.owner.userId}");	
			}
			
			FriendCollection friendCollection = FriendCollection.findByOwnerUuid( msgCreator.uuid );
			if( friendCollection )
			{
				log.debug(  "got a valid friends collection for ${msgCreator.userId}");
			}
			
			Set<String> friends = friendCollection.friends;
			if( friends )
			{
				log.debug( "got valid friends set: ${friends}");
				for( String friend : friends )
				{
					log.debug( "friend: ${friend}");
				}
			}
			User targetUser = userService.findUserByUserId( key );
			if( friends.contains( targetUser.uuid ) || 
				( msgCreator.uuid.equals( targetUser.uuid ) && 
						!event.objectClass.equals(EventTypes.STATUS_UPDATE.name ) ) )
			{
				log.debug( "match found, offering message" );
				Deque<Map> userQueue = entry.getValue();
				if( msg instanceof Message )
				{
					log.debug( "Message being offered" );					
					log.debug( "putting message on user queue for user ${key}");
					userQueue.offerFirst( event );
				}
				else
				{
					log.debug( "WTF is this? ${msg}");
				}
			}			
		}
		
		log.debug( "done processing eventQueue instances" );
	}
	
	def userStreamAwareQueueFilter =
	{  
		userStream, it ->
		
		
		log.debug( "invoking userStreamAwareQueueFilter for userStream" );
		
		// "it" is an ActivityStreamItem reference now that we've gone
		// to the revamped domain model
		
		boolean countThisOne = false;
		
		// will come into play for determining if the
		// item was created by a subscription and potentially also
		// for determining who posted this item
		// actorUuid
		// actorObjectType

		// will come into play for determining if the target
		// is "public", a particular group, a specific user, etc.
		// it.targetObjectType
		// it.targetUuid
		
		// it.objectClass

		
		switch( it.actorObjectType )
		{
			case "User":
			
				log.debug( "this was submitted directly by a User" );
				
				// check to see if the target is stream_public
				// if it is, we just need to see if the owner's uuid
				// is in the included list
				if( it.targetObjectType.equals( "STREAM_PUBLIC" ))
				{
					log.debug( "targetObjectType == STREAM_PUBLIC");
					
					// is includeAllUsers set to TRUE? If so, we don't have
					// to do any explicit checking against the actorUuid
					
					if( userStream.includeAllUsers )
					{
						log.debug( "includeallUsers is on!" );
						// tentatively set this to true (this may be overridden further down)
						countThisOne = true;
					}
					else
					{
						// we do have to check to see if the user is in the included users
						// list
						if( userStream.userUuidsIncluded.contains( it.actorUuid ) )
						{
							log.debug( "actorUuid was in included list" );
							countThisOne = true;
						}
					}
				}
				else
				{
					
					if( it.targetObjectType.equals( "UserGroup" ) )
					{
						
						log.debug( "targetObjectType == UserGroup" );
						// check if the target is a group? If it is, check
						// if the targetUuid is in the included list
						
						if( userStream.includeAllGroups )
						{
							// tentatively set this to true (this may be overridden further down)
							countThisOne = true;
						}
						else
						{
							// we do have to check to see if the group is in the included groups
							// list
							if( userStream.userGroupUuidsIncluded.contains( it.targetUuid ))
							{
								countThisOne = true;
							}
						}
						
					}
					else if( it.targetObjectType.equals( "UserList" ) )
					{
						
						log.debug( "targetObjectType == UserList" );
						
						// check if the target is a list? If it is, check
						// if the targetUuid is in the included list
						
						if( userStream.includeAllLists )
						{
							// tentatively set this to true (this may be overridden further down)
							countThisOne = true;
						}
						else
						{
							// we do have to check to see if the list is in the included groups
							// list
							if( userStream.userListUuidsIncluded.contains( it.targetUuid ))
							{
								countThisOne = true;
							}
						}
						
					}
					
					// what if it was shared straight to a User as the target?  We wouldn't do anything
					// special here, but just leave it down to a combination of "is user (actor) included"
					// and "is event type included"
					
					
				}
				
									
				break;
				
			case "BaseSubscription":
			case "ActivitiUserTaskSubscription":
			case "ActivityStreamsSubscription":
			case "BusinessEventSubscription":
			case "CalendarFeedSubscription":
			case "RssFeedSubscription":
				// this was submitted by a subscription of some sort
				// check if the subscription is in the included list
			
				log.debug( "this is a Subscription of some sort" );
				
				if( userStream.includeAllSubscriptions )
				{
					log.debug( "includeAllSubscriptions is on!" );
					countThisOne = true;
				}
				else
				{
					if( userStream.subscriptionUuidsIncluded.contains(it.actorUuid))
					{
						countThisOne = true;
					}
				}
			
								
				break;
				
			default:
				// right now there really isn't any other possibility for this
				log.debug( "INVALID" );
				break;
		}
			
		// regardless of the target or the actor, we still need to check the eventType
		// check if the eventType (objectClass) is in the included
		// list
		if( userStream.includeAllEventTypes )
		{
			// since all eventTypes are included, our current event type
			// is definitely OK.  If we passed all the filters up to here,
			// leave an existing "true" value alone
		}
		else
		{
			// but if there *are* event type filters, we have to check
			// ours, and if it isn't included, we have to set countThisOne
			// to false
			
			// TODO: look up the actual EventType instance using the provided
			// objectClass value. NOTE: this means we have to rework every place
			// we insert an objectClass value to make sure we use a valid value
			// for a corresponding eventType.  We should probably have an enum or
			// something for these names.
			EventType eventType = eventTypeService.findEventTypeByName( it.objectClass );
			if( ! userStream.eventTypesIncluded.contains( eventType ))
			{
				countThisOne = false;
			}
		}
		
		println "returning countThisOne: ${countThisOne}";
		return countThisOne;
	}
	
	
	// TODO: we can no longer just blindly send the size of the queue, since we
	// have to do this in a "stream aware" fashion.  So we'll have to iterate the
	// pending messages and evaluate against the provided UserStream.
	// in a future version, we might calculate these sizes against each existing
	// UserStream and update them as messages are pushed to us. 
	// we might also optimize this by just caching the value and only recalculate
	// it if the overall queue size has changed relative to last time we were called.
	public long getQueueSizeForUser( final String userId, final UserStreamDefinition userStream )
	{
		// println "getting queue size for user: ${userId}";
		
		long queueSize = 0;
		Deque<ActivityStreamItem> userQueue = eventQueues.get( userId ); 
		if( userQueue != null )
		{
			// println "found a userQueue";
			// look at each message on the queue, without removing it
			// and evaluate it against the UserStream object we were passed.
			def filter = userStreamAwareQueueFilter.curry( userStream );
			queueSize = userQueue.count(filter); 
		}
		else
		{
			// println "No userQueue found!";
			// println "eventQueues.size: ${eventQueues.size}";
		}
		// println "Queue size for user: ${userId} = ${queueSize}";
		
		return queueSize;	
	}
	
	public List<ActivityStreamItem> getMessagesForUser( final String userId, final int msgCount, final UserStreamDefinition userStream )
	{
		
		println "EventQueueService: getting messages for user: ${userId}, msgCount: ${msgCount}";
		List<ActivityStreamItem> messages = new ArrayList<Map>();
		Deque<Map> userQueue = eventQueues.get( userId );
		if( userQueue != null )
		{
			println "got userQueue for user ${userId}";

			// collect the messages that match
			def filter = userStreamAwareQueueFilter.curry( userStream );
			Collection matchingMessages = userQueue.findAll( filter );
			List<ActivityStreamItem> tempList = matchingMessages.asList();
			
			// iterate through up to msgCount of the messages
			// and add the message to the collection we return as well as
			// remove it from the queue.  We can't just return all of them
			// as there may be more messages pending than were requested.
			for( int i = 0; i < msgCount; i++ )
			{
				ActivityStreamItem item = tempList.get(i);
				messages.add( item );
				userQueue.removeFirstOccurrence( item ); 
			}
		}
		
		println "EventQueueService: returning ${messages.size()} messages from userQueue for user ${userId}";
		return messages;
	}
	
	
	public void registerEventQueueForUser( final String userId )
	{
		println "registering eventqueue for user: ${userId}";
		
		if( eventQueues.containsKey( userId ))
		{
			eventQueues.remove( userId );
		}

		Deque<String> userQueue = new ArrayDeque<String>();
		eventQueues.put( userId, userQueue );
		
	}

	public void unRegisterEventQueueForUser( final String userId )
	{
		// TODO: implement me
		throw new UnsupportedOperationException( "Not implemented yet!" );
	}
}
