package org.fogbeam.quoddy.social;

import org.fogbeam.quoddy.User;

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
