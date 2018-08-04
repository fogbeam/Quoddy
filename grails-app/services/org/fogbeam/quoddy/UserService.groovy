package org.fogbeam.quoddy;

import org.fogbeam.quoddy.profile.Profile
import org.fogbeam.quoddy.social.FriendCollection
import org.fogbeam.quoddy.social.FriendRequest
import org.fogbeam.quoddy.social.FriendRequestCollection
import org.fogbeam.quoddy.social.IFollowCollection
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.stream.StreamItemBase
import org.fogbeam.quoddy.subscription.BaseSubscription

class UserService {

	// injected by Spring, might be backed by LDAP version OR by local dB version;
	// but we should be able to not care which is configured
	def friendService;
	
	// same deal as the friendService...
	def groupService; // TODO: figure out if we even use this anymore...
	
	def userGroupService;
	def userListService;
	
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
		log.debug( "searching for AccountRole named ${name}");
		
		List<AccountRole> roles = AccountRole.executeQuery( "select role from AccountRole as role where role.name = :name", [name:name]);
		
		AccountRole role = null;
		if( roles.size == 1 )
		{
			role = roles[0];
		}

		log.debug( "returning role ${role}");
		return role;
	}
	
	public AccountRole createAccountRole( AccountRole role )
	{
		
		log.debug( "UserService.createAccountRole() - about to create role: ${role.toString()}");
	
		if( !role.save(flush: true))
		{
			role.errors.each { log.debug( it ) };
			throw new RuntimeException( "couldn't create AccountRole: ${role.toString()}" );
		}
		
		log.debug( "returning role: ${role}" );
		return role;
	}
	
	
	public User createUser( User user ) 
	{
		
		
		log.debug( "UserService.createUser() - about to create user: ${user.toString()}");
		/* save the user into the uzer table, we need that for associations with other
		* "system things"
		*/
		if( user.profile == null )
		{
			user.profile = new Profile();	
		}
		
		if( user.dateCreated == null )
		{
			user.dateCreated = new Date();
		}
		
		if( user.save(flush:true) )
		{
			log.info( "Saving User object.");
			
			accountService.createUser( user );
			// ldapPersonService.createUser( user );
			// create system defined Stream entries for this newly created user
			UserStreamDefinition defaultStream = new UserStreamDefinition();
			defaultStream.name = UserStreamDefinition.DEFAULT_STREAM;
			defaultStream.definedBy = UserStreamDefinition.DEFINED_SYSTEM;
			defaultStream.owner = user;
			defaultStream.includeAllEventTypes = true;
			// defaultStream.includeAllUsers = true;
			
			
			if( !defaultStream.save())
			{
				defaultStream.errors.allErrors.each { log.error( it ) };
				throw new RuntimeException( "couldn't create Default UserStream record for user: ${user.userId}" );
			}
			
			FriendCollection friendCollection = new FriendCollection( ownerUuid: user.uuid );
			friendCollection.save();
			IFollowCollection iFollowCollection = new IFollowCollection( ownerUuid: user.uuid );
			iFollowCollection.save();
			FriendRequestCollection friendRequestCollection = new FriendRequestCollection( ownerUuid: user.uuid );
			friendRequestCollection.save();
		
		}
		else
		{
			user.errors.allErrors.each { log.error( it ) };
			throw new RuntimeException( "couldn't create User record for user: ${user.userId}" );
			
		}
		
		return user;
	}

	public void importUser( User user )
	{
		// this is a User with an external authSource, so all we create is the entry in the uzer table
		// we don't create an account here.
		
		if( user.profile == null )
		{
			user.profile = new Profile();
		}
		
		if( user.save() )
		{
			// create system defined Stream entries for this newly created user
			UserStreamDefinition defaultStream = new UserStreamDefinition();
			defaultStream.name = UserStreamDefinition.DEFAULT_STREAM;
			defaultStream.definedBy = UserStreamDefinition.DEFINED_SYSTEM;
			defaultStream.owner = user;
			defaultStream.includeAllEventTypes = true;
			// defaultStream.includeAllUsers = true;
			
			
			if( !defaultStream.save())
			{
				defaultStream.errors.allErrors.each { log.debug( it ) };
				throw new RuntimeException( "couldn't create Default UserStream record for user: ${user.userId}" );
			}
			
			
			FriendCollection friendCollection = new FriendCollection( ownerUuid: user.uuid );
			friendCollection.save();
			IFollowCollection iFollowCollection = new IFollowCollection( ownerUuid: user.uuid );
			iFollowCollection.save();
			FriendRequestCollection friendRequestCollection = new FriendRequestCollection( ownerUuid: user.uuid );
			friendRequestCollection.save();
			
			
		}
		else
		{
			user.errors.allErrors.each { log.debug( it ); }
			throw new RuntimeException( "couldn't create User record for user: ${user.userId}" );
		}	
	}
		
	public User updateUser( User user )
	{
		if( ! user.save( flush: true ) )
		{
			user.errors.allErrors.each { log.debug( it ); }
		}
		
		return user;
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
		log.debug( "calculated offset as $offset");
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
		log.debug( "findEligibleUsersForUser()");
		List<User> eligibleUsers = new ArrayList<User>();
		// Set<User> tempUsers = new TreeSet<User>();
		List<User> tempUsers = new ArrayList<User>();
		
		List<User> friends = friendService.listFriends( user );
		log.debug( "got friends list: ${friends}");
		// log.debug( "${friends.class.name}" );
		
		List<User> iFollow = friendService.listIFollow( user );
		log.debug( "got iFollow list: ${iFollow}" );
		// log.debug( "${iFollow.class.name}" );

		if( friends && !(friends.isEmpty()) )
		{
			for( User friend : friends )
			{
				if( !tempUsers.contains( friend ))
				{
					tempUsers.add( friend );
				}
			}
		}
		
		if( iFollow && !(iFollow.isEmpty()))
		{
			for( User iFollowUser : iFollow )
			{
				if( !tempUsers.contains( iFollowUser ))
				{
					tempUsers.add( iFollowUser );
				}	
			}
		}
		
		friends.clear();
		friends = null;
		iFollow.clear();
		iFollow = null;
		
		eligibleUsers.addAll( tempUsers );
		
		tempUsers.clear();
		tempUsers = null;
		
		log.debug( "returning eligibleUsers from findEligibleUsersForUser" );
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
			log.debug( "found a current status with id: ${oldStatus.id}, deleting it...");

			
			
			user.currentStatus = null;
			
			if( !user.save(flush:true) )
			{
				user.errors.allErrors.each { log.debug( it ); }
			}
			else
			{
				log.debug( "save()'d User after nulling out currentStatus" );
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
			log.debug( "no current status found to delete");	
		}
		

		
		friendService.removeFriendRelations( user );

				
		List<StatusUpdate> statusUpdates = new ArrayList<StatusUpdate>();
		statusUpdates.addAll( user.oldStatusUpdates );
		for( StatusUpdate update : statusUpdates )
		{
			log.debug( "removing old status with id: ${update.id}");
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
			
			log.debug( "retCode: ${retCode}" );
		}
	

		List<ActivityStreamItem> items = ActivityStreamItem.executeQuery( "select asi from ActivityStreamItem as asi where asi.owner.id = :ownerid", [ownerid:user.id] );
		for( ActivityStreamItem item : items )
		{
			item.delete(flush:true);
		}
		
				
		List<StreamItemBase> streamItems = StreamItemBase.executeQuery( "select sib from StreamItemBase as sib where sib.owner = :owner", [owner:user] );
		for( StreamItemBase sib : streamItems )
		{
			sib.delete( flush: true );
		}
		
			
		if( !user.save(flush:true) )
		{
			user.errors.allErrors.each { log.debug( it ); }
		}
		else
		{
			log.debug( "save()'d User after nixing old StatusUpdates");
		}
		
		/* delete subscriptions */
		List<BaseSubscription> subscriptions = 
					BaseSubscription.executeQuery( "select sub from BaseSubscription as sub where sub.owner = :owner", [owner:user] );
		
		for( BaseSubscription sub : subscriptions )
		{
			sub.delete( flush: true );
		}
					
					
		List<AccountRole> roles = new ArrayList<AccountRole>();
		roles.addAll( user.roles );
		for( AccountRole role : roles )
		{
			log.debug( "removing role with id: ${role.id}");
			user.removeFromRoles( role );
		}

		if( !user.save(flush:true) )
		{
			user.errors.allErrors.each { log.debug( it ); }
		}

				
		user.permissions.removeAll();
		
		List<UserStreamDefinition> streams = new ArrayList<UserStreamDefinition>();
		log.debug( "adding all stream definitions to temporary collection");
		streams.addAll( user.streams);
		for( UserStreamDefinition streamDef : streams )
		{
			log.debug( "removing streamDef with id: ${streamDef.id}");
			user.removeFromStreams( streamDef );
		
			log.debug( "deleting streamDef now" );
			streamDef.delete( flush:true);	
		}
		
		if( !user.save(flush:true) )
		{
			user.errors.allErrors.each { log.debug( it ); }
		}
		
		
		User sysGhostUser = this.findUserByUserId( "SYS_ghost_user" );
		// find any groups this User owns, and change the ownership to the System "Ghost" User
		List<UserGroup> usersOwnedGroups = userGroupService.getGroupsOwnedByUser( user );
		for( UserGroup group : usersOwnedGroups )
		{
			group.owner = sysGhostUser;
			group.save( flush:true );
		}
		
		// remove this user from any groups that he/she is a (non-owning) member of
		List<UserGroup> userMemberOfGroups = userGroupService.getGroupsWhereUserIsMember( user );
		for ( UserGroup group : userMemberOfGroups )
		{
			// remove user from this group
			group.removeFromGroupMembers( user );
		}
		
		List<UserList> usersLists = userListService.getListsForUser( user );
		for( UserList listToDelete : usersLists )
		{
			listToDelete.delete( flush: true );
		} 
		
		
	
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
