package org.fogbeam.quoddy

import java.util.Set;

class EventQueueService 
{	
	def userService;
	
	Map<String, Deque<Map>> eventQueues = new HashMap<String, Deque<Map>>();
	
	static expose = ['jms']
	static destination = "uitestActivityQueue"
	
	def onMessage(msg)
	{
		println "Received message from JMS: ${msg}";
		
		// now, figure out which user(s) are interested in this message, and put it on
		// all the appropriate queues
		
		
		// NOTE: in this prototype version, we'll assume anybody who is logged in
		// is interested in messages from every other user (note: except themselves)
		Set<Map.Entry<String, Deque<Map>>> entries = eventQueues.entrySet();
		println "got entrySet from eventQueues object: ${entries}";
		for( Map.Entry<String, Deque<Map>> entry : entries )
		{
			println "entry: ${entry}";
			println "key: ${entry.getKey()}";
			
			String key = entry.getKey();
			
			// don't put the message on the queue for the user who sent it
			if( !key.equalsIgnoreCase( msg.creator )) 
			{
			
				// TODO: don't offer message unless the owner of this queue
				// and the event creator, are friends.
				println "msg creator: ${msg.creator}";
				User msgCreator = userService.findUserByUserId( msg.creator );
				if( msgCreator )
				{	
					println "found User object for ${msgCreator.userId}";
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
				if( friends.contains( targetUser.uuid ))
				{
					println "match found, offering message";
					Deque<Map> userQueue = entry.getValue();
					if( msg instanceof Map )
					{
						println "MapMessage being offered";
						userQueue.offerFirst( msg );
					}
					else
					{
						println "WTF is this? ${msg}";	
					}	 
				}
			}
		}
		println "done processing eventQueue instances";
	}
	
	public long getQueueSizeForUser( final String userId )
	{
		long queueSize = 0;
		Deque<Map> userQueue = eventQueues.get( userId ); 
		if( userQueue != null )
		{
			queueSize = userQueue.size();
		}
		
		// println "Queue size for user: ${userId} = ${queueSize}";
		
		return queueSize;	
	}
	
	public List<Map> getMessagesForUser( final String userId, final int msgCount )
	{
		println "getting messages for user: ${userId}, msgCount: ${msgCount}";
		List<Map> messages = new ArrayList<Map>();
		Deque<Map> userQueue = eventQueues.get( userId );
		if( userQueue != null )
		{
			println "got userQueue for user ${userId}";
			for( int i = 0; i < msgCount; i++ )
			{
				// get message from queue, put it in return set	
				Map msg = userQueue.pollFirst();
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
