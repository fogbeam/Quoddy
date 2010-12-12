package org.fogbeam.quoddy.ldap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.springframework.ldap.core.DistinguishedName;

import sun.misc.BASE64Encoder;

public class PersonBuilder 
{
	
	protected static Name buildDn(Person p, String BaseDN) 
	{
		DistinguishedName dn = new DistinguishedName(BaseDN);
		dn.add("ou", "people" );
		dn.add("cn", p.uuid );
		
		return dn;
	}
	
	public static Attributes buildAttributes(Person p) 
	{
	   Attributes attrs = new BasicAttributes();
	   BasicAttribute ocattr = new BasicAttribute("objectclass");
	   ocattr.add("top");
	   ocattr.add("person");
	   ocattr.add("inetOrgPerson");
	   ocattr.add("organizationalPerson");
	   attrs.put(ocattr);
	   
	   attrs.put( "givenName", p.givenName );
	   attrs.put( "displayName", p.displayName );
	   attrs.put("sn", p.lastName );
	   attrs.put("uid", p.uid );
	   attrs.put("mail", p.mail );
	   attrs.put( "userpassword", digestMd5( p.userpassword ) );
	   attrs.put( "description", p.description );
	   return attrs;
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