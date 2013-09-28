package org.fogbeam.quoddy;

import org.fogbeam.quoddy.profile.Profile
import org.fogbeam.quoddy.social.FriendRequest
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.stream.StreamItemBase

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
	
	public User findUserByUuid( final String uuid )
	{
		User user = User.findByUuid( uuid );
		
		return user;
			
	}
	
	
	public AccountRole findAccountRoleByName( final String name )
	{
		println "searching for AccountRole named ${name}";
		
		List<AccountRole> roles = AccountRole.executeQuery( "select role from AccountRole as role where role.name = :name", [name:name]);
		
		AccountRole role = null;
		if( roles.size == 1 )
		{
			role = roles[0];
		}

		println "returning role ${role}";
		return role;
	}
	
	public AccountRole createAccountRole( AccountRole role )
	{
		
		println "UserService.createAccountRole() - about to create role: ${role.toString()}";
	
		if( !role.save(flush: true))
		{
			role.errors.each { println it };
			throw new RuntimeException( "couldn't create AccountRole: ${role.toString()}" );
		}
		
		println "returning role: ${role}";
		return role;
	}
	
	
	public void createUser( User user ) 
	{
		
		
		println "UserService.createUser() - about to create user: ${user.toString()}";
		/* save the user into the uzer table, we need that for associations with other
		* "system things"
		*/
		if( user.profile == null )
		{
			user.profile = new Profile();	
		}
		
		if( user.save() )
		{
			accountService.createUser( user );
			// ldapPersonService.createUser( user );
			// create system defined Stream entries for this newly created user
			UserStreamDefinition defaultStream = new UserStreamDefinition();
			defaultStream.name = UserStreamDefinition.DEFAULT_STREAM;
			defaultStream.definedBy = UserStreamDefinition.DEFINED_SYSTEM;
			defaultStream.owner = user;
			defaultStream.includeAllEventTypes = true;
			defaultStream.includeAllUsers = true;
			
			
			if( !defaultStream.save())
			{
				defaultStream.errors.allErrors.each { println it };
				throw new RuntimeException( "couldn't create Default UserStream record for user: ${user.userId}" );
			}
		}
		else
		{
			user.errors.allErrors.each { println it };
			throw new RuntimeException( "couldn't create User record for user: ${user.userId}" );
			
		}
		
	}

	public void importUser( User user )
	{
		// this is a User with an external authSource, so all we create is the entry in the uzer table
		// we don't create an account here.
		
		if( user.profile == null )
		{
			user.profile = new Profile();
		}
		
		if( !user.save() )
		{
			user.errors.allErrors.each { println it };
			throw new RuntimeException( "couldn't create User record for user: ${user.userId}" );
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

	/* note: page is 1 indexed */
	public List<User> findAllUsers(final int max, final int page )
	{
		List<User> users = new ArrayList<User>();
		int offset = ( (page * max) -max);
		println "calculated offset as $offset";
		List<User> temp = User.executeQuery( "select user from User as user order by user.fullName", ['offset':offset, 'max':max]);
		if( temp )
		{
			users.addAll( temp );
		}
	
		return users;
	}

	
		
	/* NOTE: we really need a custom comparator here, since we want to
	 * avoid duplicate members in the resulting list, based on entity id (database key)
	 * and the default behavior is using object identity.  It probably won't matter
	 * at least in the short-term, but fix this ASAP.
	 */
	public List<User> findEligibleUsersForUser( final User user ) 
	{
		println "findEligibleUsersForUser()";
		List<User> eligibleUsers = new ArrayList<User>();
		// Set<User> tempUsers = new TreeSet<User>();
		List<User> tempUsers = new ArrayList<User>();
		
		List<User> friends = friendService.listFriends( user );
		// println "got friends list: ${friends}";
		// println "${friends.class.name}";
		
		List<User> iFollow = friendService.listIFollow( user );
		// println "got iFollow list: ${iFollow}";
		// println "${iFollow.class.name}";

		if( friends && !(friends.isEmpty()) )
		{
			// println "adding friends";
			for( User friend : friends )
			{
				if( !tempUsers.contains( friend ))
				{
					tempUsers.add( friend );
				}
			}
			// println "added friends to tempUsers";
		}
		
		if( iFollow && !(iFollow.isEmpty()))
		{
			// println "adding iFollow";
			for( User iFollowUser : iFollow )
			{
				if( !tempUsers.contains( iFollowUser ))
				{
					tempUsers.add( iFollowUser );
				}	
			}
			// println "added iFollow to tempUsers";
		}
		
		friends.clear();
		friends = null;
		iFollow.clear();
		iFollow = null;
		println "cleared temporary lists";
		
		eligibleUsers.addAll( tempUsers );
		println "added tempUsers to eligibleUsers list";
		
		tempUsers.clear();
		tempUsers = null;
		
		println "returning eligibleUsers from findEligibleUsersForUser";
		return eligibleUsers;
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
	
	public void deleteUser( final User user )
	{

		StatusUpdate oldStatus = user.currentStatus;
		if( oldStatus != null )
		{
			println "found a current status with id: ${oldStatus.id}, deleting it...";

			
			
			user.currentStatus = null;
			
			if( !user.save(flush:true) )
			{
				user.errors.allErrors.each { println it; }
			}
			else
			{
				println "save()'d User after nulling out currentStatus";
			}
			
						
			List<ActivityStreamItem> itemsToDelete = 
							ActivityStreamItem.executeQuery( "select asi from ActivityStreamItem as asi where asi.streamObject = :update", [update:oldStatus] );
			
			for( ActivityStreamItem itemToDelete : itemsToDelete )
			{
				itemToDelete.delete( flush:true );	
			}
			
			oldStatus.delete(flush:true);
		}
		else
		{
			println "no current status found to delete";	
		}
		

		
		friendService.removeFriendRelations( user );

				
		List<StatusUpdate> statusUpdates = new ArrayList<StatusUpdate>();
		statusUpdates.addAll( user.oldStatusUpdates );
		for( StatusUpdate update : statusUpdates )
		{
			println "removing old status with id: ${update.id}";
			user.removeFromOldStatusUpdates( update );
			
			List<ActivityStreamItem> itemsToDelete = 
							ActivityStreamItem.executeQuery( "select asi from ActivityStreamItem as asi where asi.streamObject = :update", [update:update] );
	
			for( ActivityStreamItem itemToDelete : itemsToDelete )
			{
				itemToDelete.delete(flush:true);
			}				
									
			List<StatusUpdate> statusesToDelete = StatusUpdate.executeQuery( "select su from StatusUpdate as su where su = :statusupdate",[statusupdate:update]);
			for( StatusUpdate statusToDelete : statusesToDelete )
			{
				statusToDelete.delete( flush: true );
			}
			
			println "retCode: ${retCode}";
		}
	
		
		List<StreamItemBase> streamItems = StreamItemBase.executeQuery( "select sib from StreamItemBase as sib where sib.owner = :owner", [owner:user] );
		for( StreamItemBase sib : streamItems )
		{
			sib.delete( flush: true );
		}
		
		List<ActivityStreamItem> items = ActivityStreamItem.executeQuery( "select asi from ActivityStreamItem as asi where asi.owner.id = :ownerid", [ownerid:user.id] );
		for( ActivityStreamItem item : items )
		{
			item.delete(flush:true);
		}
		
					
		if( !user.save(flush:true) )
		{
			user.errors.allErrors.each { println it; }
		}
		else
		{
			println "save()'d User after nixing old StatusUpdates";
		}
		
		List<AccountRole> roles = new ArrayList<AccountRole>();
		roles.addAll( user.roles );
		for( AccountRole role : roles )
		{
			println "removing role with id: ${role.id}";
			user.removeFromRoles( role );
		}

		if( !user.save(flush:true) )
		{
			user.errors.allErrors.each { println it; }
		}

				
		user.permissions.removeAll();
		
		List<UserStreamDefinition> streams = new ArrayList<UserStreamDefinition>();
		println "adding all stream definitions to temporary collection";
		streams.addAll( user.streams);
		for( UserStreamDefinition streamDef : streams )
		{
			println "removing streamDef with id: ${streamDef.id}";
			user.removeFromStreams( streamDef );
		
			println "deleting streamDef now";
			streamDef.delete( flush:true);	
		}
		
		if( !user.save(flush:true) )
		{
			user.errors.allErrors.each { println it; }
		}
		
		// User.executeUpdate( "delete User u where u.id = :userId", [userId:user.id]);
		user.delete( flush: true );
	}
	
	public void disableUser( final User user )
	{
		user.disabled = true;
		user.save( flush:true);
		
	}	
	
	public void enableUser( final User user )
	{
		user.disabled = false;
		user.save( flush:true);
		
	}
	
}
