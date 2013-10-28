package org.fogbeam.quoddy

import java.security.MessageDigest 
import java.security.NoSuchAlgorithmException 
import org.fogbeam.quoddy.ldap.LDAPPerson 
import sun.misc.BASE64Encoder 

class LoginService 
{
	def userService;
	def ldapPersonService;
	def localAccountService;
	
	public User doUserLogin( final String userId, final String password )
	{
		User user = null;
		
		// TODO: deal with authsource stuff, conver this stuff to use JAAS?
		
		LocalAccount account = localAccountService.findAccountByUserId( userId );
		boolean trySecondAuthSource = true;
		if( account )
		{
			println "local account FOUND for userId: ${userId}";
			
			trySecondAuthSource = false;  // this is a local user
			// verify credentials, verify is existing User, load User	
			String md5HashSubmitted = digestMd5( password );
			// println "md5HashSubmitted: ${md5HashSubmitted}";
			md5HashSubmitted = "{MD5}" + md5HashSubmitted;
			// println "md5HashSubmitted: ${md5HashSubmitted}";
			
			String userPassword = account.password;
			// println "userPassword: ${userPassword}";
			
			if( md5HashSubmitted.equals( userPassword ))
			{
				println "login successful";
				
				// now find a User that matches this account
				user = userService.findUserByUserId( account.username );
				
			}
			else
			{
				println "login failed on password match.  "
			}
			
		}
		
		if( trySecondAuthSource )
		{
			println "NO local account found for userId: ${userId}";
			println "trying LDAP for user ${userId}";
			
			LDAPPerson person = ldapPersonService.findPersonByUserId( userId );
			if( person )
			{
				println "found matching user in LDAP, verifying password";
				// verify credentials, verify is existing User, load User
				String md5HashSubmitted = digestMd5( password );
				
				println "md5HashSubmitted: ${md5HashSubmitted}";
				println "person.userpassword: ${person.userpassword}";
				md5HashSubmitted = md5HashSubmitted.replace( "{md5}", "");
				
				println "md5HashSubmitted: ${md5HashSubmitted}";
				
				String personPassword = person.userpassword
				if( personPassword.startsWith( "{md5}"))
				{
					personPassword = personPassword.replace( "{md5}", "" );
				}
				
				// or if there's an uppercased version, nix that as well
				if( personPassword.startsWith( "{MD5}"))
				{
					personPassword = personPassword.replace( "{MD5}", "" );
				}
				
				if( md5HashSubmitted.equals( personPassword ))
				{
					println "login successful";
					
					// now find a User that matches this account
					user = userService.findUserByUserId( person.uid );
			
					user = LdapPersonService.copyPersonToUser( person, user );
							
				}
				else
				{
					println "login failed on password match.  "
				}
				
			}
			
		}
		
		return user;	
	}	

	private static String digestMd5(final String password)
	{
		String base64;
		try
		{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes());
			base64 = new BASE64Encoder().encode(digest.digest());
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	  
		// return "{MD5}" + base64;
		return base64;
	}
}
