package org.fogbeam.quoddy;

import java.util.List;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import javax.naming.Name;

import org.fogbeam.quoddy.User;
import org.fogbeam.quoddy.ldap.Group 
import org.fogbeam.quoddy.ldap.GroupAttributeMapper 
import org.fogbeam.quoddy.ldap.GroupBuilder 
import org.fogbeam.quoddy.ldap.Person 
import org.fogbeam.quoddy.ldap.PersonAttributeMapper 
import org.fogbeam.quoddy.ldap.PersonBuilder 
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter 
import org.springframework.ldap.NameNotFoundException;

class UserService {

	def ldapTemplate;
	
	public User findUserByUserId( String userId )
	{
		
		User user = null;
		
		AndFilter memberFilter = new AndFilter();
		memberFilter.and(new EqualsFilter("objectclass", "person"));
		memberFilter.and(new EqualsFilter("uid", userId ));
		
		List<Person> persons = ldapTemplate.search("ou=people,o=quoddy", memberFilter.encode(),
				 new PersonAttributeMapper());
		
			 
		if( persons != null && persons.size() > 0 ) 
		{
			Person p = persons.get(0);
			println "using userId ${userId}";
			user = User.findByUserId( userId );
			println "found user with id = ${user.id}";
			user = copyPersonToUser(p, user );	
			println "user.id = ${user.id}";
		}
		
		return user;
	}	
	
	public void createUser( User user ) 
	{
		
		/* save the user into the uzer table, we need that for associations with other
		 * "system things"
		 */
		if( user.save() )
		{
		
			Person person = copyUserToPerson( user );
			Name dn = PersonBuilder.buildDn( person, "o=quoddy" );
		
			ldapTemplate.bind(dn, null, PersonBuilder.buildAttributes(person));

			// create corresponding groups...
		
			// and a group for followees
			// create a "follow" group for this user, populate it with somebody or other...
			Group followGroup = new Group();
			followGroup.owner = dn.toString();
			followGroup.name = "FollowGroup/" + person.uid + "/" + person.givenName + " " + person.lastName;
			Name followGroupDn = GroupBuilder.buildFollowGroupDn( followGroup, "o=quoddy" );
			ldapTemplate.bind( followGroupDn, null, GroupBuilder.buildAttributes(followGroup));
		
			// and a group for confirmed friends
			Group confirmedFriendsGroup = new Group();
			confirmedFriendsGroup.owner = dn.toString();
			confirmedFriendsGroup.name = "ConfirmedFriendsGroup/" + person.uid + "/" + person.givenName + " " + person.lastName;
			Name confirmedFriendsGroupDn = GroupBuilder.buildConfirmedFriendsGroupDn( confirmedFriendsGroup, "o=quoddy" );
			ldapTemplate.bind( confirmedFriendsGroupDn, null, GroupBuilder.buildAttributes(confirmedFriendsGroup));
		
			// and a group for pending friend requests?
			Group unconfirmedFriendsGroup = new Group();
			unconfirmedFriendsGroup.owner = dn.toString();
			unconfirmedFriendsGroup.name = "UnconfirmedFriendsGroup/" + person.uid + "/" + person.givenName + " " + person.lastName;
			Name unconfirmedFriendsGroupDn = GroupBuilder.buildUnconfirmedFriendsGroupDn( unconfirmedFriendsGroup, "o=quoddy" );
			ldapTemplate.bind( unconfirmedFriendsGroupDn, null, GroupBuilder.buildAttributes(unconfirmedFriendsGroup));
		}
		else
		{
			throw new RuntimeException( "couldn't create User record for user: ${user.userId}" );
			user.errors.allErrors.each { println it };
		}
	}
	
	public User updateUser( User user )
	{
		println "about to update user...";
		
		// update using ldapTemplate
		Attribute displayNameAttr = new BasicAttribute("displayName");
		displayNameAttr.add( user.displayName );
		ModificationItem displayNameItem = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, displayNameAttr);
				
		Attribute snAttr = new BasicAttribute("sn");
		snAttr.add( user.lastName );
		ModificationItem snItem = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, snAttr);
		
		Attribute givenNameAttr = new BasicAttribute("givenName");
		givenNameAttr.add( user.firstName );
		ModificationItem givenNameItem = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, givenNameAttr);
		
		Attribute mailAttr = new BasicAttribute("mail");
		mailAttr.add( user.email );
		ModificationItem mailItem = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mailAttr);
		
		Attribute descriptionAttr = new BasicAttribute("description");
		descriptionAttr.add( user.bio );
		ModificationItem descriptionItem = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, descriptionAttr);

		ModificationItem[] modificationItems = 
			[displayNameItem, snItem, givenNameItem, mailItem, descriptionItem] as ModificationItem[];
		
		Name userToModifyDn = PersonBuilder.buildDn( copyUserToPerson( user ), "o=quoddy" );
		ldapTemplate.modifyAttributes( userToModifyDn, modificationItems );
				
		Person person =  (Person)ldapTemplate.lookup(userToModifyDn, new PersonAttributeMapper() );
		
		user = User.findByUserId( user.userId );
		User modifiedUser = copyPersonToUser( person, user );
		return modifiedUser;
		
	}
	
	public void addToFollow( User destinationUser, User targetUser )
	{
		// get the dn of the destination user
		Name destinationUserDn = PersonBuilder.buildDn( copyUserToPerson(destinationUser), "o=quoddy" );
		
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
		attr.add( PersonBuilder.buildDn( copyUserToPerson(targetUser), "o=quoddy").toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
		ldapTemplate.modifyAttributes(followGroupDn, [item] as ModificationItem[] );
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
		Name currentUserDn = PersonBuilder.buildDn( copyUserToPerson(currentUser), "o=quoddy" );
		Name newFriendDn = PersonBuilder.buildDn( copyUserToPerson(newFriend), "o=quoddy" );
		
		// search for the group in the unconfirmedfriends tree, with  currentUser as the owner
		AndFilter unconfirmedGroupOwnerFilter = new AndFilter();
		unconfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		unconfirmedGroupOwnerFilter.and(new EqualsFilter("owner", currentUserDn.toString() ));
		
		List<Group> unconfirmedGroups = ldapTemplate.search( "ou=unconfirmedfriends,ou=groups,o=quoddy", unconfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group unconfirmedFriendsGroup = unconfirmedGroups.get(0);
		
		Name unconfirmedFriendsGroupDn = GroupBuilder.buildUnconfirmedFriendsGroupDn( unconfirmedFriendsGroup, , "o=quoddy" );
		println "unconfirmedFriendsGroupDn: ${unconfirmedFriendsGroupDn}";
		
		Attribute removePendingAttr = new BasicAttribute("uniquemember");
		removePendingAttr.add( newFriendDn.toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem removePendingItem = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, removePendingAttr);
		
		println "remove pending friend request";
		ldapTemplate.modifyAttributes(unconfirmedFriendsGroupDn, [removePendingItem] as ModificationItem[] );
		
		
		// search for the group in the confirmedfriends tree, with currentUser as the owner
		AndFilter currentUserConfirmedGroupOwnerFilter = new AndFilter();
		currentUserConfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		currentUserConfirmedGroupOwnerFilter.and(new EqualsFilter("owner", currentUserDn.toString() ));
		
		List<Group> currentUserConfirmedGroups = ldapTemplate.search( "ou=confirmedfriends,ou=groups,o=quoddy", currentUserConfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group currentUserConfirmedFriendsGroup = currentUserConfirmedGroups.get(0);
		
		Name currentUserConfirmedFriendsGroupDn = GroupBuilder.buildConfirmedFriendsGroupDn( currentUserConfirmedFriendsGroup, , "o=quoddy" );
		println "currentUserConfirmedFriendsGroupDn: ${currentUserConfirmedFriendsGroupDn}";
		
		// add a new uniquemember attribute with the dn of newFriend
		Attribute currentUserConfirmedAttr = new BasicAttribute("uniquemember");
		currentUserConfirmedAttr.add( newFriendDn.toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem currentUserConfirmedItem = new ModificationItem(DirContext.ADD_ATTRIBUTE, currentUserConfirmedAttr);
		
		println "calling modifyAttributes";
		ldapTemplate.modifyAttributes(currentUserConfirmedFriendsGroupDn, [currentUserConfirmedItem] as ModificationItem[] );
		
		
		// search for the group in the confirmedFriends tree, with newFriend as the owner
		AndFilter newFriendConfirmedGroupOwnerFilter = new AndFilter();
		newFriendConfirmedGroupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		newFriendConfirmedGroupOwnerFilter.and(new EqualsFilter("owner", newFriendDn.toString() ));
		
		List<Group> newFriendConfirmedGroups = ldapTemplate.search( "ou=confirmedfriends,ou=groups,o=quoddy", newFriendConfirmedGroupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group newFriendConfirmedFriendsGroup = newFriendConfirmedGroups.get(0);
		
		Name newFriendConfirmedFriendsGroupDn = GroupBuilder.buildConfirmedFriendsGroupDn( newFriendConfirmedFriendsGroup, , "o=quoddy" );
		println "newFriendConfirmedFriendsGroupDn: ${newFriendConfirmedFriendsGroupDn}";
		
		// add a new uniquemember attribute with the dn of currentUser
		Attribute newFriendConfirmedAttr = new BasicAttribute("uniquemember");
		newFriendConfirmedAttr.add( currentUserDn.toString() );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem newFriendConfirmedItem = new ModificationItem(DirContext.ADD_ATTRIBUTE, newFriendConfirmedAttr);
		
		println "calling modifyAttributes";
		ldapTemplate.modifyAttributes(newFriendConfirmedFriendsGroupDn, [newFriendConfirmedItem] as ModificationItem[] );
		
	}
	
	public void addToFriends( User currentUser, User newFriend )
	{
		
		println "UserService.addTofriends: ${currentUser.userId} / ${newFriend.userId}";
		
		// get the dn of the destination user
		Name newFriendDn = PersonBuilder.buildDn( copyUserToPerson(newFriend), "o=quoddy" );
		
		String dnString = newFriendDn.toString();
		println "dnString: ${dnString}";
		
		// search for the group in the unconfirmedfriends tree, with that user as the owner
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search( "ou=unconfirmedfriends,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group friendsGroup = groups.get(0);
		
		Name friendsGroupDn = GroupBuilder.buildUnconfirmedFriendsGroupDn( friendsGroup, , "o=quoddy" );
		println "friendsGroupDn: ${friendsGroupDn}";
		
		// add a new uniquemember attribute with the dn of the currentuser
		Attribute attr = new BasicAttribute("uniquemember");
		String name = PersonBuilder.buildDn( copyUserToPerson(currentUser), "o=quoddy").toString();
		println "name: ${name}";
		attr.add( name );
		
		// create a modificationitem and update the attributes to add the new
		// uniquemember attribute...
		ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
		
		println "calling modifyAttributes";
		ldapTemplate.modifyAttributes(friendsGroupDn, [item] as ModificationItem[] );
		
	}
		
	public List<User> findAllUsers() 
	{
		List<Person> persons = null;
		try {
		
			AndFilter memberFilter = new AndFilter();
			memberFilter.and(new EqualsFilter("objectclass", "person"));
			
			persons = ldapTemplate.search("ou=people,o=quoddy", memberFilter.encode(),
					 new PersonAttributeMapper());
			
		}
		catch( NameNotFoundException e )
		{
			throw new RuntimeException( e );
		}	
		
		
		List<User> allUsers = new ArrayList<User>();
		if( persons != null && persons.size() > 0 )
		{
			for( Person person : persons )
			{
				User user = User.findByUserId( person.uid );
				user = copyPersonToUser( person, user );	
				allUsers.add( user );	
			}	
		}	
		
		
		return allUsers;
	}

	public List<User> listFriends( User user ) 
	{
		List<User> friends = new ArrayList<User>();
		
		Person p = copyUserToPerson( user );
		String dnString = PersonBuilder.buildDn( p, "o=quoddy" );
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search("ou=confirmedfriends,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group friendsGroup = groups.get(0);
		
		List<Person> members = friendsGroup.members;
		for( Person person: members ) 
		{
			println "working with userId: ${person.uid}";
			User aUser = User.findByUserId( person.uid );
			println "Found aUser with id = ${aUser.id}";
			aUser = copyPersonToUser( person, aUser );
			println "after copy, aUser.id = ${aUser.id}";
			friends.add( aUser );
		}
		
		println "returning friends: ${friends}";
		return friends;
	}
	
	public List<User> listFollowers( User user )
	{
		/* list the users who follow the supplied user */	
		List<User> followers = new ArrayList<User>();
		
		// get every follow group where this user is a member of the follow group, then
		// retrieve the owner ID.
		Person p = copyUserToPerson( user );
		String dnString = PersonBuilder.buildDn( p, "o=quoddy" );
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("uniquemember", dnString));
		
		System.out.println( "looking for followers of: ${dnString}" );
		
		List<Group> groups = ldapTemplate.search( "ou=followgroups,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		
		for( Group group : groups )
		{
			System.out.println( "Follow Group Owner: " + group.owner );
			
			
			AndFilter memberFilter = new AndFilter();
			memberFilter.and(new EqualsFilter("objectclass", "person"));
			String ownerString = group.owner;
			String[] parts = ownerString.split( "," );
			String memberCn = parts[0];
			println "memberCn: ${memberCn}";
			parts = memberCn.split("=" );
			memberCn = parts[1];
			println "memberCn: ${memberCn}";
			memberFilter.and(new EqualsFilter("cn", memberCn  ));
			
			List<Person> persons = ldapTemplate.search("ou=people,o=quoddy", memberFilter.encode(),
					 new PersonAttributeMapper());
			
				 
			if( persons != null && persons.size() > 0 )
			{
				println "Found follower";
				
				Person person = persons.get(0);
				user = User.findByUserId( person.uid );	
				user = copyPersonToUser(person, user );
			
				followers.add( user );	
			}	
			else
			{
				println "Ok, this is wonky";	
			}
		}
		
		return followers;
	}
	
	/* TODO: load the User object from the db, so we can use it for associations */
	public List<User> listIFollow( User user )
	{
		List<User> iFollow = new ArrayList<User>();
		
		Person p = copyUserToPerson( user );
		String dnString = PersonBuilder.buildDn( p, "o=quoddy" );
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search( "ou=followgroups,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group followGroup = groups.get(0);
		
		List<Person> members = followGroup.members;
		for( Person person: members ) 
		{

			User aUser = User.findByUserId( person.uid );
			aUser = copyPersonToUser( person );
			iFollow.add( aUser );
		}
		
		return iFollow;
	}
	
	public List<FriendRequest> listOpenFriendRequests( User user )
	{
		List<FriendRequest> openFriendRequests = new ArrayList<FriendRequest>();
		
		Person p = copyUserToPerson( user );
		String dnString = PersonBuilder.buildDn( p, "o=quoddy" );
		AndFilter groupOwnerFilter = new AndFilter();
		groupOwnerFilter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
		groupOwnerFilter.and(new EqualsFilter("owner", dnString));
		
		List<Group> groups = ldapTemplate.search( "ou=unconfirmedfriends,ou=groups,o=quoddy", groupOwnerFilter.encode(),
				 new GroupAttributeMapper(ldapTemplate));
		
		Group unconfirmedFriendsGroup = groups.get(0);
		
		List<Person> members = unconfirmedFriendsGroup.members;
		for( Person person: members )
		{
			User unconfirmedFriend = copyPersonToUser( person );
			FriendRequest friendRequest = new FriendRequest( user, unconfirmedFriend);
			openFriendRequests.add( friendRequest );
		}
		
		return openFriendRequests;
	}
	
	
	public User copyPersonToUser( Person person, User user )
	{
		println "got user with id: ${user.id}";
		user.uuid = person.uuid;
		user.firstName = person.givenName;
		user.lastName = person.lastName;
		user.displayName = person.displayName;
		user.email = person.mail;
		user.userId = person.uid;
		user.password = person.userpassword;
		println "returning user with id: ${user.id}";
		return user;
		
	}
	
	public User copyPersonToUser( Person person )
	{
		User user = new User();
		user.uuid = person.uuid;
		user.firstName = person.givenName;
		user.lastName = person.lastName;
		user.displayName = person.displayName;
		user.email = person.mail;
		user.userId = person.uid;
		user.password = person.userpassword;
		
		return user;
	}	
	
	public Person copyUserToPerson( User user )	
	{
		Person person = new Person();
		person.uuid = user.uuid;
		person.givenName = user.firstName;
		person.lastName = user.lastName;
		person.displayName = user.displayName;
		person.mail = user.email;
		person.uid = user.userId;
		person.userpassword = user.password;
		person.description = user.bio;
		
		return person;	
	}
	
}
