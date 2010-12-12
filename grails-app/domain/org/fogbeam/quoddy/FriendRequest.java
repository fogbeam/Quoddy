package org.fogbeam.quoddy;

public class FriendRequest 
{
	User owner;
	User unconfirmedFriend;
	
	public FriendRequest( User owner, User unconfirmedFriend )
	{
		this.owner = owner;
		this.unconfirmedFriend = unconfirmedFriend;
	}
}
