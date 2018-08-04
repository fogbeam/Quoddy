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
import org.fogbeam.quoddy.ldap.LDAPPerson;
import org.fogbeam.quoddy.ldap.PersonAttributeMapper 
import org.fogbeam.quoddy.ldap.PersonBuilder 
import org.springframework.ldap.filter.AndFilter 
import org.springframework.ldap.filter.EqualsFilter
import org.springframework.ldap.NameNotFoundException; 

class LdapPersonService
{
	def ldapTemplate;
	
	public LDAPPerson findPersonByUserId( final String userId )
	{
		
		LDAPPerson person = null;
		
		AndFilter memberFilter = new AndFilter();
		memberFilter.and(new EqualsFilter("objectclass", "person"));
		memberFilter.and(new EqualsFilter("uid", userId ));
		
		List<LDAPPerson> persons = ldapTemplate.search("ou=people,o=quoddy,dc=fogbeam,dc=com", memberFilter.encode(),
				 new PersonAttributeMapper());
		
			 
		if( persons != null && persons.size() > 0 )
		{
			person = persons.get(0);
		}
		
		return person;
	}
	
	public LDAPPerson findPersonByCn( final String cn )
	{
		
		LDAPPerson person = null;
		
		AndFilter memberFilter = new AndFilter();
		memberFilter.and(new EqualsFilter("objectclass", "person"));
		memberFilter.and(new EqualsFilter("cn", cn ));
		
		List<LDAPPerson> persons = ldapTemplate.search("ou=people,o=quoddy,dc=fogbeam,dc=com", memberFilter.encode(),
				 new PersonAttributeMapper());
		
			 
		if( persons != null && persons.size() > 0 )
		{
			person = persons.get(0);
		}
		
		return person;
	}
	
	
	public void createUser( User user )
	{		
		LDAPPerson person = copyUserToPerson( user );
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
	
	public User updateUser( User user )
	{
		log.debug( "about to update user...");
		
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
				
		LDAPPerson person =  (LDAPPerson)ldapTemplate.lookup(userToModifyDn, new PersonAttributeMapper() );
		
		user = User.findByUserId( user.userId );
		User modifiedUser = copyPersonToUser( person, user );
		return modifiedUser;
		
	}
				
	public static User copyPersonToUser( final LDAPPerson person, final User user )
	{
		// log.debug( "got user with id: ${user.id}");
		user.uuid = person.uuid;
		user.firstName = person.givenName;
		user.lastName = person.lastName;
		user.displayName = person.displayName;
		user.email = person.mail;
		user.userId = person.uid;
		user.password = person.userpassword;
		// log.debug( "returning user with id: ${user.id}");
		return user;
		
	}
	
	public static User copyPersonToUser( final LDAPPerson person )
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
	
	public static LDAPPerson copyUserToPerson( final User user )
	{
		LDAPPerson person = new LDAPPerson();
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
