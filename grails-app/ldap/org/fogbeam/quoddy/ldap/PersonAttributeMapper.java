package org.fogbeam.quoddy.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

public class PersonAttributeMapper implements AttributesMapper
{
	public Object mapFromAttributes(Attributes attrs) throws NamingException 
   	{		
		LDAPPerson person = new LDAPPerson();
   	
   		person.uuid = (String)attrs.get("cn").get();
   		person.mail = (String)attrs.get("mail").get();
   		person.uid = (String)attrs.get("uid").get();
   		person.givenName = (String)attrs.get("givenName").get();
   		person.lastName = (String)attrs.get("sn").get();
   		person.displayName = (String)attrs.get("displayName").get();
   		
   		// System.out.println( "userpassword: " + attrs.get("userpassword").get().getClass().getCanonicalName() + ", " +
   		//		new String( (byte[])attrs.get("userpassword").get() ) );
   		
   		person.userpassword = new String( (byte[])attrs.get("userpassword").get());
   		
   		return person; 
   
   	}
}

/*
	
NamingEnumeration<? extends Attribute> enumer = attrs.getAll();
while( enumer.hasMoreElements() )
{
	Attribute attr = enumer.nextElement();
	System.out.println( attr.toString() );
}
*/