package org.fogbeam.quoddy

import javax.jms.Message

import org.fogbeam.quoddy.social.FriendCollection
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.StreamItemBase

class EventQueueService 
{	
	def userService;
	
	Map<String, Deque<Map>> eventQueues = new HashMap<String, Deque<Map>>();
	
	static expose = ['jms']
	static destination = "uitestActivityQueue"; // TODO: rename this to something more meaningful
	
	def onMessage(msg)
	{
		println "Message class: ${msg.class}";
		println "Received message from JMS: ${msg}";
		
		
		// now, figure out which user(s) are interested in this message, and put it on
		// all the appropriate queues
		Set<Map.Entry<String, Deque<Map>>> entries = eventQueues.entrySet();
		println "got entrySet from eventQueues object: ${entries}";
		for( Map.Entry<String, Deque<Map>> entry : entries )
		{
			println "entry: ${entry}";
			println "key: ${entry.getKey()}";
			
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
			// println "msg creator: ${msg.getString( 'creator')}";
			User msgCreator = userService.findUserByUserId( event.owner.userId );
			if( msgCreator )
			{
				println "found User object for ${msgCreator.userId}";
			}
			else
			{
				println( "No User for ${event.owner.userId}");	
			}
			
			FriendCollection friendCollection = FriendCollection.findByOwnerUuid( msgCreator.uuid );
			if( friendCollection )
			{
				println "got a valid friends collection for ${msgCreator.userId}";
			}
			
			Set<String> friends = friendCollection.friends;
			if( friends )
			{
				println "got valid friends set: ${friends}";
				for( String friend : friends )
				{
					println "friend: ${friend}";
				}
			}
			User targetUser = userService.findUserByUserId( key );
			if( friends.contains( targetUser.uuid ) || 
				( msgCreator.uuid.equals( targetUser.uuid ) && !event.objectClass.equals('StatusUpdate') ) )
			{
				println "match found, offering message";
				Deque<Map> userQueue = entry.getValue();
				if( msg instanceof Message )
				{
					println "Message being offered";
					
					// Map internalMsg = new HashMap();
					// TODO: turn this into a plain old Map
					// Enumeration mapNames = msg.getMapNames();
					// while( mapNames.hasMoreElements())
					// {
						// String name = mapNames.nextElement();
						// internalMsg.put( name, msg.getObject(name)); 	
					// }
					
					println "putting message on user queue for user ${key}";
					userQueue.offerFirst( event );
				}
				else
				{
					println "WTF is this? ${msg}";
				}
			}			
		}
		
		println "done processing eventQueue instances";
	}
	
	public long getQueueSizeForUser( final String userId )
	{
		// println "getting queue size for user: ${userId}";
		
		long queueSize = 0;
		Deque<Map> userQueue = eventQueues.get( userId ); 
		if( userQueue != null )
		{
			queueSize = userQueue.size();
		}
		
		// println "Queue size for user: ${userId} = ${queueSize}";
		
		return queueSize;	
	}
	
	public List<StreamItemBase> getMessagesForUser( final String userId, final int msgCount )
	{
		println "getting messages for user: ${userId}, msgCount: ${msgCount}";
		List<StreamItemBase> messages = new ArrayList<Map>();
		Deque<Map> userQueue = eventQueues.get( userId );
		if( userQueue != null )
		{
			println "got userQueue for user ${userId}";
			for( int i = 0; i < msgCount; i++ )
			{
				// get message from queue, put it in return set	
				StreamItemBase msg = userQueue.pollFirst();
				messages.add( msg ); 
			}
		}
		
		return messages;
	}
	
	
	public void registerEventQueueForUser( final String userId )
	{
		println "registering eventqueue for user: ${userId}";
		
		if( !eventQueues.containsKey( userId ))
		{
			Deque<String> userQueue = new ArrayDeque<String>();
			eventQueues.put( userId, userQueue ); 
		}
		else
		{
			println "We already have an event queue for this user: ${userId}";
		}
	}

	public void unRegisterEventQueueForUser( final String userId )
	{
		// TODO: implement me
		throw new UnsupportedOperationException( "Not implemented yet!" );
	}
}
