package org.fogbeam.quoddy

import java.security.MessageDigest 
import java.security.NoSuchAlgorithmException 
import org.fogbeam.quoddy.ldap.LDAPPerson 

import java.util.Base64;

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
			log.debug( "local account FOUND for userId: ${userId}");
			
			trySecondAuthSource = false;  // this is a local user
			// verify credentials, verify is existing User, load User	
			String md5HashSubmitted = digestMd5( password );
			// log.debug( "md5HashSubmitted: ${md5HashSubmitted}");
			md5HashSubmitted = "{MD5}" + md5HashSubmitted;
			// log.debug( "md5HashSubmitted: ${md5HashSubmitted}" );
			
			String userPassword = account.password;
			// log.debug( "userPassword: ${userPassword}");
			
			if( md5HashSubmitted.equals( userPassword ))
			{
				log.info( "login successful" );
				
				// now find a User that matches this account
				user = userService.findUserByUserId( account.username );
				
			}
			else
			{
				log.info( "login failed on password match.");
			}
			
		}
		
		if( trySecondAuthSource )
		{
			log.info( "NO local account found for userId: ${userId}");
			log.info( "trying LDAP for user ${userId}");
			
			LDAPPerson person = ldapPersonService.findPersonByUserId( userId );
			if( person )
			{
				log.debug(  "found matching user in LDAP, verifying password");
				// verify credentials, verify is existing User, load User
				String md5HashSubmitted = digestMd5( password );
				
				log.debug( "md5HashSubmitted: ${md5HashSubmitted}");
				log.debug( "person.userpassword: ${person.userpassword}");
				md5HashSubmitted = md5HashSubmitted.replace( "{md5}", "");
				
				log.debug( "md5HashSubmitted: ${md5HashSubmitted}");
				
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
					log.info( "login successful" );
					
					// now find a User that matches this account
					user = userService.findUserByUserId( person.uid );
			
					user = LdapPersonService.copyPersonToUser( person, user );
							
				}
				else
				{
					log.info( "login failed on password match." );
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
			// base64 = new BASE64Encoder().encode(digest.digest());
			base64 = new String( Base64.getEncoder().encode( digest.digest() ) );
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	  
		// return "{MD5}" + base64;
		return base64;
	}
}
