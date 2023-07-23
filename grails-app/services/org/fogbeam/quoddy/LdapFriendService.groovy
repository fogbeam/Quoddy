package org.fogbeam.quoddy

import java.util.List;

import javax.naming.Name 
import javax.naming.directory.Attribute 
import javax.naming.directory.BasicAttribute 
import javax.naming.directory.DirContext 
import javax.naming.directory.ModificationItem 
import org.fogbeam.quoddy.ldap.Group 
import org.fogbeam.quoddy.ldap.GroupAttributeMapper 
import org.fogbeam.quoddy.ldap.GroupBuilder 
import org.fogbeam.quoddy.ldap.LDAPPerson 
import org.fogbeam.quoddy.ldap.PersonAttributeMapper 
import org.fogbeam.quoddy.ldap.PersonBuilder 
import org.fogbeam.quoddy.social.FriendRequest;
import org.springframework.ldap.filter.AndFilter 
import org.springframework.ldap.filter.EqualsFilter 

class LdapFriendService 
{
	def ldapTemplate;
	
	public void addToFollow( User destinationUser, User targetUser )
	{
		// get the dn of the destination user
		Name destinationUserDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(destinationUser), "o=quoddy" );
		
		String dnString = destinationUserDn.toString();
		
		// search for the group in the followgroups tree, with that user as the owner
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search( "ou=followgroups,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group followGroup = groups.get(0);
		
		Name followGroupDn = GroupBuilder.buildFollowGroupDn( followGroup, "o=quoddy" );
		
		// add a new uniquemember attribute with the dn of the targetuser
		Attribute attr = new BasicAttribute("uniquemember");
		attr.add( PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(targetUser), "o=quoddy").toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
		ldapTemplate.modifyAttributes(followGroupDn, [item] as ModificationItem[] );
	}

	public void unFollow( final User currentUser, final User userToUnfollow )
	{		
		// get the DNs of the Users
		Name currentUserDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(currentUser), "o=quoddy" );
		Name userToUnfollowDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(userToUnfollow), "o=quoddy" );
		
		
		
		String dnString = currentUserDn.toString();
		
		// search for the group in the followgroups tree, with that user as the owner
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search( "ou=followgroups,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group followGroup = groups.get(0);
		
		Name followGroupDn = GroupBuilder.buildFollowGroupDn( followGroup, "o=quoddy" );

		// delete the uniquemember attribute with the dn of userToUnfollow
		Attribute userToUnfollowAttr = new BasicAttribute("uniquemember");
		userToUnfollowAttr.remove( userToUnfollowDn.toString() );
				
		// create a modificationitem and update the attributes
		ModificationItem userToUnfollowItem = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, userToUnfollowAttr);
		
		log.debug( "calling modifyAttributes");
		ldapTemplate.modifyAttributes(followGroupDn, [userToUnfollowItem] as ModificationItem[] );
	}
	
	public void deleteFriendRequest( User currentUser, User newFriend )
	{
		// get the DNs of the Users
		Name currentUserDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(currentUser), "o=quoddy" );
		Name newFriendDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(newFriend), "o=quoddy" );
		
		// search for the group in the unconfirmedfriends tree, with  currentUser as the owner
		AndFilter unconfirmedGroupOwnerFilter = new AndFilter();
		unconfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		unconfirmedGroupOwnerFilter.and(new EqualsFilter("owner", currentUserDn.toString() ));
		
		List<Group> unconfirmedGroups = ldapTemplate.search( "ou=unconfirmedfriends,ou=groups,o=quoddy", unconfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group unconfirmedFriendsGroup = unconfirmedGroups.get(0);
		
		Name unconfirmedFriendsGroupDn = GroupBuilder.buildUnconfirmedFriendsGroupDn( unconfirmedFriendsGroup, , "o=quoddy" );
		log.debug( "unconfirmedFriendsGroupDn: ${unconfirmedFriendsGroupDn}");
		
		Attribute removePendingAttr = new BasicAttribute("uniquemember");
		removePendingAttr.add( newFriendDn.toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem removePendingItem = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, removePendingAttr);
		
		log.debug( "remove pending friend request");
		ldapTemplate.modifyAttributes(unconfirmedFriendsGroupDn, [removePendingItem] as ModificationItem[] );
		
	}
	
	/* NOTE: as of 07-06-2023 - this is untested and not confirmed to work. Need a setup 
	 * of the smoketest suite that tests with the LDAP backend enabled. 
	 */
	public void removeFriend( final User currentUser, final User friendToRemove )
	{

		// get the DNs of the Users
		Name currentUserDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(currentUser), "o=quoddy" );
		Name friendToRemoveDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(friendToRemove), "o=quoddy" );
		
		
		// search for the group in the confirmedfriends tree, with currentUser as the owner
		AndFilter currentUserConfirmedGroupOwnerFilter = new AndFilter();
		currentUserConfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		currentUserConfirmedGroupOwnerFilter.and(new EqualsFilter("owner", currentUserDn.toString() ));
		
		List<Group> currentUserConfirmedGroups = ldapTemplate.search( "ou=confirmedfriends,ou=groups,o=quoddy", currentUserConfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group currentUserConfirmedFriendsGroup = currentUserConfirmedGroups.get(0);
		
		Name currentUserConfirmedFriendsGroupDn = GroupBuilder.buildConfirmedFriendsGroupDn( currentUserConfirmedFriendsGroup, "o=quoddy" );
		log.debug( "currentUserConfirmedFriendsGroupDn: ${currentUserConfirmedFriendsGroupDn}");
		
		// delete the uniquemember attribute with the dn of friendToRemove
		Attribute currentUserConfirmedAttr = new BasicAttribute("uniquemember");
		currentUserConfirmedAttr.remove( friendToRemoveDn.toString() );
		
		// create a modificationitem and update the attributes
		ModificationItem currentUserConfirmedItem = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, currentUserConfirmedAttr);
		
		log.debug( "calling modifyAttributes");
		ldapTemplate.modifyAttributes(currentUserConfirmedFriendsGroupDn, [currentUserConfirmedItem] as ModificationItem[] );
		
		
		
		// now do the same thing for the group with friendToRemove as the owner, and remove "currentUser" from the group
		// search for the group in the confirmedfriends tree, with currentUser as the owner
		AndFilter friendToRemoveConfirmedGroupOwnerFilter = new AndFilter();
		friendToRemoveConfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		friendToRemoveConfirmedGroupOwnerFilter.and(new EqualsFilter("owner", friendToRemoveDn.toString() ));
		
		List<Group> friendToRemoveConfirmedGroups = ldapTemplate.search( "ou=confirmedfriends,ou=groups,o=quoddy", friendToRemoveConfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group friendToRemoveConfirmedFriendsGroup = friendToRemoveConfirmedGroups.get(0);
		
		Name friendToRemoveConfirmedFriendsGroupDn = GroupBuilder.buildConfirmedFriendsGroupDn( friendToRemoveConfirmedFriendsGroup, "o=quoddy" );
		log.debug( "ConfirmedFriendsGroupDn: ${friendToRemoveConfirmedFriendsGroupDn}");
		
		// delete the uniquemember attribute with the dn of friendToRemove
		Attribute friendToRemoveConfirmedAttr = new BasicAttribute("uniquemember");
		friendToRemoveConfirmedAttr.remove( currentUserDn.toString() );
		
		// create a modificationitem and update the attributes
		ModificationItem friendToRemoveConfirmedItem = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, friendToRemoveConfirmedAttr);
		
		log.debug( "calling modifyAttributes");
		ldapTemplate.modifyAttributes(friendToRemoveConfirmedFriendsGroupDn, [friendToRemoveConfirmedItem] as ModificationItem[] );
	
	}
	
	
	/* note: this is a "two way" operation, so to speak.  That is, the initial
	 * request was half of the overall operation of adding a friend... now that
	 * the requestee has confirmed, we have to update *both* users to show the
	 * new confirmed friend connection.  We also have to remove the "pending" request.
	 */
	public void confirmFriend( User currentUser, User newFriend )
	{
		// currentUser is the one confirming a request, newFriend is the one
		// who requested it originally.  So, remove the "pending" request from
		// currentUser, and then insert an entry for newUser into currentUser's
		// "confirmed friends" group and an entry for currentUser into newUser's
		// "confirmed friends" group.
		
		// get the DNs of the Users
		Name currentUserDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(currentUser), "o=quoddy" );
		Name newFriendDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(newFriend), "o=quoddy" );
		
		// search for the group in the unconfirmedfriends tree, with  currentUser as the owner
		AndFilter unconfirmedGroupOwnerFilter = new AndFilter();
		unconfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		unconfirmedGroupOwnerFilter.and(new EqualsFilter("owner", currentUserDn.toString() ));
		
		List<Group> unconfirmedGroups = ldapTemplate.search( "ou=unconfirmedfriends,ou=groups,o=quoddy", unconfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group unconfirmedFriendsGroup = unconfirmedGroups.get(0);
		
		Name unconfirmedFriendsGroupDn = GroupBuilder.buildUnconfirmedFriendsGroupDn( unconfirmedFriendsGroup, , "o=quoddy" );
		log.debug( "unconfirmedFriendsGroupDn: ${unconfirmedFriendsGroupDn}");
		
		Attribute removePendingAttr = new BasicAttribute("uniquemember");
		removePendingAttr.add( newFriendDn.toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem removePendingItem = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, removePendingAttr);
		
		log.debug( "remove pending friend request");
		ldapTemplate.modifyAttributes(unconfirmedFriendsGroupDn, [removePendingItem] as ModificationItem[] );
		
		
		// search for the group in the confirmedfriends tree, with currentUser as the owner
		AndFilter currentUserConfirmedGroupOwnerFilter = new AndFilter();
		currentUserConfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		currentUserConfirmedGroupOwnerFilter.and(new EqualsFilter("owner", currentUserDn.toString() ));
		
		List<Group> currentUserConfirmedGroups = ldapTemplate.search( "ou=confirmedfriends,ou=groups,o=quoddy", currentUserConfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group currentUserConfirmedFriendsGroup = currentUserConfirmedGroups.get(0);
		
		Name currentUserConfirmedFriendsGroupDn = GroupBuilder.buildConfirmedFriendsGroupDn( currentUserConfirmedFriendsGroup, , "o=quoddy" );
		log.debug( "currentUserConfirmedFriendsGroupDn: ${currentUserConfirmedFriendsGroupDn}");
		
		// add a new uniquemember attribute with the dn of newFriend
		Attribute currentUserConfirmedAttr = new BasicAttribute("uniquemember");
		currentUserConfirmedAttr.add( newFriendDn.toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem currentUserConfirmedItem = new ModificationItem(DirContext.ADD_ATTRIBUTE, currentUserConfirmedAttr);
		
		log.debug( "calling modifyAttributes");
		ldapTemplate.modifyAttributes(currentUserConfirmedFriendsGroupDn, [currentUserConfirmedItem] as ModificationItem[] );
		
		
		// search for the group in the confirmedFriends tree, with newFriend as the owner
		AndFilter newFriendConfirmedGroupOwnerFilter = new AndFilter();
		newFriendConfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		newFriendConfirmedGroupOwnerFilter.and(new EqualsFilter("owner", newFriendDn.toString() ));
		
		List<Group> newFriendConfirmedGroups = ldapTemplate.search( "ou=confirmedfriends,ou=groups,o=quoddy", newFriendConfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group newFriendConfirmedFriendsGroup = newFriendConfirmedGroups.get(0);
		
		Name newFriendConfirmedFriendsGroupDn = GroupBuilder.buildConfirmedFriendsGroupDn( newFriendConfirmedFriendsGroup, , "o=quoddy" );
		log.debug( "newFriendConfirmedFriendsGroupDn: ${newFriendConfirmedFriendsGroupDn}");
		
		// add a new uniquemember attribute with the dn of currentUser
		Attribute newFriendConfirmedAttr = new BasicAttribute("uniquemember");
		newFriendConfirmedAttr.add( currentUserDn.toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem newFriendConfirmedItem = new ModificationItem(DirContext.ADD_ATTRIBUTE, newFriendConfirmedAttr);
		
		log.debug( "calling modifyAttributes" );
		ldapTemplate.modifyAttributes(newFriendConfirmedFriendsGroupDn, [newFriendConfirmedItem] as ModificationItem[] );
		
	}
	
	public void addToFriends( User currentUser, User newFriend )
	{
		
		log.debug( "UserService.addTofriends: ${currentUser.userId} / ${newFriend.userId}");
		
		// get the dn of the destination user
		Name newFriendDn = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(newFriend), "o=quoddy" );
		
		String dnString = newFriendDn.toString();
		log.debug( "dnString: ${dnString}" );
		
		// search for the group in the unconfirmedfriends tree, with that user as the owner
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search( "ou=unconfirmedfriends,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group friendsGroup = groups.get(0);
		
		Name friendsGroupDn = GroupBuilder.buildUnconfirmedFriendsGroupDn( friendsGroup, , "o=quoddy" );
		log.debug( "friendsGroupDn: ${friendsGroupDn}");
		
		// add a new uniquemember attribute with the dn of the currentuser
		Attribute attr = new BasicAttribute("uniquemember");
		String name = PersonBuilder.buildDn( LdapPersonService.copyUserToPerson(currentUser), "o=quoddy").toString();
		log.debug( "name: ${name}");
		attr.add( name );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
		
		log.debug( "calling modifyAttributes");
		ldapTemplate.modifyAttributes(friendsGroupDn, [item] as ModificationItem[] );
		
	}

	
	public List<User> listFriends( User user )
	{
		List<User> friends = new ArrayList<User>();
		
		LDAPPerson p = LdapPersonService.copyUserToPerson( user );
		String dnString = PersonBuilder.buildDn( p, "o=quoddy" );
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search("ou=confirmedfriends,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group friendsGroup = groups.get(0);
		
		List<LDAPPerson> members = friendsGroup.members;
		for( LDAPPerson person: members )
		{
			log.debug( "working with userId: ${person.uid}" );
			User aUser = User.findByUserId( person.uid );
			log.debug( "Found aUser with id = ${aUser.id}" );
			aUser = LdapPersonService.copyPersonToUser( person, aUser );
			log.debug( "after copy, aUser.id = ${aUser.id}");
			friends.add( aUser );
		}
		
		log.debug( "returning friends: ${friends}");
		return friends;
	}
	
	public List<User> listFollowers( User user )
	{
		/* list the users who follow the supplied user */
		List<User> followers = new ArrayList<User>();
		
		// get every follow group where this user is a member of the follow group, then
		// retrieve the owner ID.
		LDAPPerson p = LdapPersonService.copyUserToPerson( user );
		String dnString = PersonBuilder.buildDn( p, "o=quoddy" );
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("uniquemember", dnString));
		
		log.debug( "looking for followers of: ${dnString}" );
		
		List<Group> groups = ldapTemplate.search( "ou=followgroups,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		
		for( Group group : groups )
		{
			log.debug( "Follow Group Owner: " + group.owner );
			
			
			AndFilter memberFilter = new AndFilter();
			memberFilter.and(new EqualsFilter("objectclass", "person"));
			String ownerString = group.owner;
			String[] parts = ownerString.split( "," );
			String memberCn = parts[0];
			log.debug( "memberCn: ${memberCn}");
			parts = memberCn.split("=" );
			memberCn = parts[1];
			log.debug( "memberCn: ${memberCn}");
			memberFilter.and(new EqualsFilter("cn", memberCn  ));
			
			List<LDAPPerson> persons = ldapTemplate.search("ou=people,o=quoddy", memberFilter.encode(),
					 new PersonAttributeMapper());
			
				 
			if( persons != null && persons.size() > 0 )
			{
				log.debug( "Found follower" );
				
				LDAPPerson person = persons.get(0);
				user = User.findByUserId( person.uid );
				user = LdapPersonService.copyPersonToUser(person, user );
			
				followers.add( user );
			}
			else
			{
				log.warn( "Ok, this is wonky" );
			}
		}
		
		return followers;
	}
	
	/* TODO: load the User object from the db, so we can use it for associations */
	public List<User> listIFollow( User user )
	{
		List<User> iFollow = new ArrayList<User>();
		
		LDAPPerson p = LdapPersonService.copyUserToPerson( user );
		String dnString = PersonBuilder.buildDn( p, "o=quoddy" );
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search( "ou=followgroups,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group followGroup = groups.get(0);
		
		List<LDAPPerson> members = followGroup.members;
		for( LDAPPerson person: members )
		{

			User aUser = User.findByUserId( person.uid );
			aUser = LdapPersonService.copyPersonToUser( person );
			iFollow.add( aUser );
		}
		
		return iFollow;
	}
	
	public List<FriendRequest> listOpenFriendRequests( User user )
	{
		List<FriendRequest> openFriendRequests = new ArrayList<FriendRequest>();
		
		LDAPPerson p = LdapPersonService.copyUserToPerson( user );
		String dnString = PersonBuilder.buildDn( p, "o=quoddy" );
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search( "ou=unconfirmedfriends,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group unconfirmedFriendsGroup = groups.get(0);
		
		List<LDAPPerson> members = unconfirmedFriendsGroup.members;
		for( LDAPPerson person: members )
		{
			User unconfirmedFriend = LdapPersonService.copyPersonToUser( person );
			FriendRequest friendRequest = new FriendRequest( user, unconfirmedFriend);
			openFriendRequests.add( friendRequest );
		}
		
		return openFriendRequests;
	}


	/* NOTE to self, 07-06-2023: what were we using this for? The other (LocalDB) version seems to remove
	 * all friends for a given user. Is that something that was just put in for testing purposes? Don't think
	 * that would be an operation we'd expose to Users directly?? Or ... dunno. Check on this. SPR
	 */
	public void removeFriendRelations( final User user )
	{
		// NOP for right now...
		
	}
		
}
