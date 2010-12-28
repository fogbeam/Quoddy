package org.fogbeam.quoddy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.fogbeam.quoddy.ldap.LDAPPerson 
import org.fogbeam.quoddy.ldap.PersonAttributeMapper 
import org.springframework.ldap.core.DistinguishedName 
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.NameNotFoundException

import sun.misc.BASE64Encoder;

class LoginController {

	
	def userService;
	
    def index = { }
    
    def login = {
    	
    	def userId = params.username;
    	def password = params.password;
    	
		User user = null;
		
		try {
			
			user = userService.findUserByUserId( userId );
		}
		catch( NameNotFoundException e )
		{
			e.printStackTrace();
		}
		
		boolean loginSucceeded = false;
		if( user )
		{
			println "found user: ${userId} in LDAP";
			String md5HashSubmitted = LoginController.digestMd5( password );
			println "md5HashSubmitted: ${md5HashSubmitted}";
			if( md5HashSubmitted.equals( user.password ))
			{
				loginSucceeded = true;	
			}
			else
			{
				println "login failed on password match.  "	
			}
		}
		else 
		{
		}
		
		if( loginSucceeded )
    	{
    		session.user = user;
    		redirect( controller:'home', action:'index')
    	}
    	else
    	{
    		flash.message = "Login Failed";
    		redirect( action:'index');
    	}
    }
    
    def logout = {
    	session.user = null;
    	redirect( uri:'/');
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
