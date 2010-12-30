package org.fogbeam.quoddy;

import java.util.List;



import org.fogbeam.quoddy.User;

class UserService {

	// injected by Spring, might be backed by LDAP version OR by local dB version;
	// but we should be able to not care which is configured
	def friendService;
	
	// same deal as the friendService...
	def groupService;
	
	// and again...  when finished, this will either be LdapPersonService or LocalAccountService, but we don't care.
	def accountService;
	
	public User findUserByUserId( String userId )
	{
		User user = User.findByUserId( userId );
		
		return user;
	}	
	
	public void createUser( User user ) 
	{
		
		/* save the user into the uzer table, we need that for associations with other
		* "system things"
		*/
		if( user.save() )
		{
			accountService.createUser( user );
			// ldapPersonService.createUser( user );
		}
		else
		{
			throw new RuntimeException( "couldn't create User record for user: ${user.userId}" );
			user.errors.allErrors.each { println it };
		}
		
	}
	
	public User updateUser( User user )
	{
		throw new RuntimeException( "not implemented yet" );
	}
	
	public void addToFollow( User destinationUser, User targetUser )
	{
		friendService.addToFollow( destinationUser, targetUser );	
	}

	/* note: this is a "two way" operation, so to speak.  That is, the initial
	 * request was half of the overall operation of adding a friend... now that
	 * the requestee has confirmed, we have to update *both* users to show the
	 * new confirmed friend connection.  We also have to remove the "pending" request.
	 */
	public void confirmFriend( User currentUser, User newFriend )
	{
		friendService.confirmFriend( currentUser, newFriend );
		
	}
	
	public void addToFriends( User currentUser, User newFriend )
	{
		friendService.addToFriends( currentUser, newFriend );
	}
		
	public List<User> findAllUsers() 
	{
		List<User> users = new ArrayList<User>();
		List<User> temp = User.findAll();
		if( temp )
		{
			users.addAll( temp );	
		}
	
		return users;	
	}

	public List<User> listFriends( User user ) 
	{
		List<User> friends = new ArrayList<User>();
		List<User> temp = friendService.listFriends( user );
		if( temp )
		{
			friends.addAll( temp );
		}
	
		return friends;	
	}
		// ---
	public List<User> listFollowers( User user )
	{
		List<User> followers = new ArrayList<User>();
		List<User> temp = friendService.listFollowers( user );
		if( temp )
		{
			followers.addAll( temp );
		}
	
		return followers;
	}
	
	public List<User> listIFollow( User user )
	{
		List<User> iFollow = new ArrayList<User>();
		List<User> temp = friendService.listIFollow( user );
		if( temp )
		{
			iFollow.addAll( temp );
		}
	
		return iFollow;
	}
	
	public List<FriendRequest> listOpenFriendRequests( User user )
	{
		List<FriendRequest> openRequests = new ArrayList<FriendRequest>();
		
		List<FriendRequest> temp = friendService.listOpenFriendRequests( user );
		if( temp )
		{
			openRequests.addAll( temp );	
		}
		
		return openRequests;
	}	
}
