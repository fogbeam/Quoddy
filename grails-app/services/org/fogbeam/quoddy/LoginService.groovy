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
			trySecondAuthSource = false;  // this is a local user
			// verify credentials, verify is existing User, load User	
			String md5HashSubmitted = digestMd5( password );
			println "md5HashSubmitted: ${md5HashSubmitted}";
			if( md5HashSubmitted.equals( account.password ))
			{
				println "login successful";
				
				// now find a User that matches this account
				user = userService.findUserByUserId( account.userName );
				
			}
			else
			{
				println "login failed on password match.  "
			}
			
		}
		
		if( trySecondAuthSource )
		{
			LDAPPerson person = ldapPersonService.findPersonByUserId( userId );
			if( person )
			{
				// verify credentials, verify is existing User, load User
				String md5HashSubmitted = digestMd5( password );
				println "md5HashSubmitted: ${md5HashSubmitted}";
				if( md5HashSubmitted.equals( person.userpassword ))
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
	  
		return "{MD5}" + base64;
	}
}
