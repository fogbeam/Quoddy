package org.fogbeam.quoddy

import java.util.List;

class LocalAccountService 
{
	public User findAccountByUserId( final String userId )
	{
		LocalAccount account = LocalAccount.findByUsername( userId );
		
		return account;
	}
	
	public void createUser( final User user )
	{
		throw new RuntimeException( "not implemented yet" );
	}
	
	public User updateUser( final User user )
	{
		throw new RuntimeException( "not implemented yet" );
	}
	
	public void addToFollow( final User destinationUser, final User targetUser )
	{
		throw new RuntimeException( "not implemented yet" );
	}

	/* note: this is a "two way" operation, so to speak.  That is, the initial
	 * request was half of the overall operation of adding a friend... now that
	 * the requestee has confirmed, we have to update *both* users to show the
	 * new confirmed friend connection.  We also have to remove the "pending" request.
	 */
	public void confirmFriend( final User currentUser, final User newFriend )
	{
		
	throw new RuntimeException( "not implemented yet" );
		
	}
	
	public void addToFriends( final User currentUser, final User newFriend )
	{
		throw new RuntimeException( "not implemented yet" );
		
	}
		
	public List<User> findAllUsers()
	{
		throw new RuntimeException( "not implemented yet" );
	}

	public List<User> listFriends( final User user )
	{
		throw new RuntimeException( "not implemented yet" );
	}
	
	public List<User> listFollowers( final User user )
	{
		throw new RuntimeException( "not implemented yet" );
	}
	
	public List<User> listIFollow( final User user )
	{
		throw new RuntimeException( "not implemented yet" );
	}
	
	public List<FriendRequest> listOpenFriendRequests( final User user )
	{
		throw new RuntimeException( "not implemented yet" );
	}
}
