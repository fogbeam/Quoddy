package org.fogbeam.quoddy

import java.util.List;

class LocalFriendService 
{
	public void addToFollow( final User destinationUser, final User targetUser )
	{
		IFollowCollection iFollowCollection = IFollowCollection.findByOwnerUuid( destinationUser.uuid );
		if( iFollowCollection == null )
		{
			throw new RuntimeException( "can't get iFollowCollection for user: ${destinationUser.userId}" );	
		}

		iFollowCollection.addToIFollow( targetUser.uuid );
		iFollowCollection.save();		
	}

	/* note: this is a "two way" operation, so to speak.  That is, the initial
	 * request was half of the overall operation of adding a friend... now that
	 * the requestee has confirmed, we have to update *both* users to show the
	 * new confirmed friend connection.  We also have to remove the "pending" request.
	 */
	public void confirmFriend( final User currentUser, final User newFriend )
	{
		
		// currentUser is the one confirming a request, newFriend is the one
		// who requested it originally.  So, remove the "pending" request from
		// currentUser, and then insert an entry for newUser into currentUser's
		// "confirmed friends" group and an entry for currentUser into newUser's
		// "confirmed friends" group.
		FriendCollection friendCollectionCU = FriendCollection.findByOwnerUuid( currentUser.uuid );
		FriendCollection friendCollectionNF = FriendCollection.findByOwnerUuid( newFriend.uuid );
		FriendRequestCollection friendRequestsCU = FriendRequestCollection.findByOwnerUuid( currentUser.uuid );
		
		friendRequestsCU.removeFromFriendRequests( newFriend.uuid );
		friendCollectionCU.addToFriends( newFriend.uuid );
		friendCollectionNF.addToFriends( currentUser.uuid );
		
		friendRequestsCU.save();
		friendCollectionCU.save();
		friendCollectionNF.save();
		

	}
	
	public void addToFriends( final User currentUser, final User newFriend )
	{
		println "UserService.addTofriends: ${currentUser.userId} / ${newFriend.userId}";
		
		FriendRequestCollection friendRequests = FriendRequestCollection.findByOwnerUuid( newFriend.uuid );
		if( friendRequests == null )
		{
			throw new RuntimeException( "can't get friendRequests for user: ${destinationUser.userId}" );
		}
	
		friendRequests.addToFriendRequests( currentUser.uuid );
		friendRequests.save();
	}

	
	public List<User> listFriends( final User user )
	{
		List<User> friends = new ArrayList<User>();
		FriendCollection friendsCollection = FriendCollection.findByOwnerUuid( user.uuid );
		
		Set<String> friendUuids = friendsCollection.friends;
		
		for( String friendUuid : friendUuids )
		{
			User friend = User.findByUuid( friendUuid );
			friends.add( friend );
		}
		
		friendsCollection = null;
		println "returning friends: ${friends}";
		return friends;
	}
	
	public List<User> listFollowers( final User user )
	{
		/* list the users who follow the supplied user */
		List<User> followers = new ArrayList<User>();
		
		// select ownerUuid from IFollowCollection where iFollow contains user.uuid 
		// from Item item join item.labels lbls where 'hello' in (lbls)
		List<IFollowCollection> iFollowCollections = 
			IFollowCollection.executeQuery( 
				"select collection from IFollowCollection as collection join collection.iFollow iFollow where ? in (iFollow)", [user.uuid] );
		
		for( IFollowCollection collection: iFollowCollections )
		{
			User follower = User.findByUuid( collection.ownerUuid );
			followers.add( follower ); 	
		}
		iFollowCollections = null;
		
		return followers;
	}
	

	public List<User> listIFollow( final User user )
	{
		List<User> peopleIFollow = new ArrayList<User>();
		IFollowCollection iFollowCollection = IFollowCollection.findByOwnerUuid( user.uuid );
		
		Set<String> iFollowUuids = iFollowCollection.iFollow;
		
		for( String iFollowUuid : iFollowUuids )
		{
			User iFollow = User.findByUuid( iFollowUuid );
			peopleIFollow.add( iFollow );
		}
		
		iFollowCollection = null;		
		return peopleIFollow;
	}
	
	public List<FriendRequest> listOpenFriendRequests( final User user )
	{
		List<FriendRequest> openFriendRequests = new ArrayList<FriendRequest>();

		FriendRequestCollection friendRequestCollection = FriendRequestCollection.findByOwnerUuid( user.uuid );
		
		Set<String> unconfirmedFriendUuids = friendRequestCollection.friendRequests;
		
		for( String unconfirmedFriendUuid : unconfirmedFriendUuids )
		{
			User unconfirmedFriend = User.findByUuid( unconfirmedFriendUuid );
			FriendRequest friendRequest = new FriendRequest( user, unconfirmedFriend );
			openFriendRequests.add( friendRequest );
		}
		
		return openFriendRequests;		
	}

}
