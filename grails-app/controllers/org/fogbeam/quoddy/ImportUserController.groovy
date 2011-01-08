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
		List<LDAPPerson> persons = ldapTemplate.search("ou=people,o=quoddy", memberFilter.encode(),
			new PersonAttributeMapper());
		
	
		[ldapPersons: persons];		
	}

	def addImportedUsers = 
	{
		
		// println params;
		List<String> usersToAdd = new ArrayList<String>();
		params.each { 
		
			String entry = it;
			if( entry.startsWith( "importUser." ) && entry.endsWith("=on" ))
			{
				String temp1 = entry.replace( "importUser.", "" );
				String temp2 = temp1.replace( "=on", "" );	
				usersToAdd.add( temp2 );
			}	
		};
		
		// TODO: lookup each of these users in LDAP, and create a 
		// corresponding record in the Uzer table
		// note: now we really kinda have to deal with things like
		// authSource and domain, etc.
		usersToAdd.each 
		{ 
			LDAPPerson person = ldapPersonService.findPersonByCn( it );
			User user = LdapPersonService.copyPersonToUser( person );
			userService.importUser( user );
		};
		
		render( "<html><head></head><body onload=\"window.close();\"></body></html>");
	}	
	
}
