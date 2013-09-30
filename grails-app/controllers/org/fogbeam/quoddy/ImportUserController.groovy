package org.fogbeam.quoddy

import org.fogbeam.quoddy.ldap.LDAPPerson 
import org.fogbeam.quoddy.ldap.PersonAttributeMapper 
import org.springframework.ldap.filter.AndFilter 
import org.springframework.ldap.filter.EqualsFilter 
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter 

class ImportUserController 
{
	def ldapTemplate;
	def ldapPersonService;
	def userService;
	
	def importUserSearch =
	{
		String queryString = params.queryString;
	
		AndFilter displayNameFilter = new AndFilter();
		displayNameFilter.and(new EqualsFilter("objectclass", "person"));
		displayNameFilter.and(new LikeFilter("displayName", queryString));
		
		AndFilter givenNameFilter = new AndFilter();
		givenNameFilter.and(new EqualsFilter("objectclass", "person"));
		givenNameFilter.and(new LikeFilter("givenName", queryString));
		
		AndFilter surnameFilter = new AndFilter();
		surnameFilter.and(new EqualsFilter("objectclass", "person"));
		surnameFilter.and(new LikeFilter("sn", queryString));
		
		OrFilter memberFilter = new OrFilter();
		memberFilter.or( displayNameFilter );
		memberFilter.or( givenNameFilter );
		memberFilter.or( surnameFilter );

				
		// lookup LDAP Person records that match the query
		// and return a list of those records
		List<LDAPPerson> persons = ldapTemplate.search("ou=people,o=quoddy,dc=fogbeam,dc=com", memberFilter.encode(),
			new PersonAttributeMapper());
		
	
		[ldapPersons: persons];		
	}

	def addImportedUsers = 
	{
		
		println params;
		List<String> usersToAdd = new ArrayList<String>();
		params.each { 
		
			String entry = it;
			if( entry.startsWith( "importUser." ) && entry.endsWith( "=on"))
			{
				println "entry: ${entry}";
				String uid = entry.split("\\.")[1];
				uid = uid.replace( "=on", "" );	
				
				usersToAdd.add( uid );
			}	
		};
		
		// lookup each of these users in LDAP, and create a 
		// corresponding record in the Uzer table
		// note: now we really kinda have to deal with things like
		// authSource and domain, etc.
		usersToAdd.each 
		{ 
			
			AndFilter uidFilter = new AndFilter();
			uidFilter.and(new EqualsFilter("objectclass", "person"));
			uidFilter.and(new LikeFilter("uid", it ));
			
			List<LDAPPerson> persons = ldapTemplate.search("ou=people,o=quoddy,dc=fogbeam,dc=com", uidFilter.encode(),
				new PersonAttributeMapper());
			if( persons.size() == 1 )
			{
				User user = LdapPersonService.copyPersonToUser( persons[0] );
				userService.importUser( user );
			}
			else
			{
				println "Wrong number of matching uids!";
			}
		};
		
		redirect( controller: "user", action: "manageUsers" );
	}	
	
}
