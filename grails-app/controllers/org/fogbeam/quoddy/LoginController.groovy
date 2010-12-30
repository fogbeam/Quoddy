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

	
	def loginService;
	
    def index = { }
    
    def login = {
    	
    	def userId = params.username;
    	def password = params.password;
    	
		User user = null;
		

			
		// do login through login service, which might ultimately be authenticating
		// against an LDAP backend, a local DB backend, or something else.
		// NOTE: if we want to allow flexibility in configuring authentication
		// backends, using JAAS or whatever, how do we manage acquiring the
		// proper credentials when we don't know all the steps in the auth
		// process in advance?  We'd need to hand in a Handler that can return
		// results to the browser (kinda ugly) or get a list of required
		// credentials in advance, drive the user through any multi-step stuff
		// then submit the credentials.
		
		user = loginService.doUserLogin( userId, password );

				
		if( user )
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
}
