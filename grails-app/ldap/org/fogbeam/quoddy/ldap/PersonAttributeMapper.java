package org.fogbeam.quoddy.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

public class PersonAttributeMapper implements AttributesMapper
{
	public Object mapFromAttributes(Attributes attrs) throws NamingException 
   	{		
		LDAPPerson person = new LDAPPerson();

		NamingEnumeration<?> attrEnumeration = attrs.getAll();
		while( attrEnumeration.hasMore() )
		{
			Attribute att = (Attribute) attrEnumeration.next();
			System.out.println( "attribute: " + att.getID() + ", " + att.toString());
		}
		
		Attribute dnAttr = attrs.get("dn");
   		if( dnAttr != null )
   		{
   			person.dn = (String)dnAttr.get();
   		}
		
		Attribute emailAttr = attrs.get("mail");
   		if( emailAttr != null )
   		{
   			person.email = (String)emailAttr.get();
   		}
   		
		Attribute cnAttr = attrs.get("cn");
   		if( cnAttr != null )
   		{
   			person.fullName = (String)cnAttr.get();
   		}
   		
   		Attribute mailAttr = attrs.get("mail");
   		if( mailAttr != null )
   		{
   			person.mail = (String)mailAttr.get();
   		}
   		
   		Attribute uidAttr = attrs.get("uid");
   		if( uidAttr != null )
   		{
   			person.uid = (String)uidAttr.get();
   		}
   		
   		Attribute givenNameAttr = attrs.get("gn");
   		if( givenNameAttr != null )
   		{
   			person.givenName = (String)givenNameAttr.get();
   		}
   		
   		Attribute snAttr = attrs.get("sn");
   		if( snAttr != null )
   		{
   			person.lastName = (String)snAttr.get();
   		}
   		
   		Attribute displayNameAttr = attrs.get("displayName");
   		if( displayNameAttr != null)
   		{
   			person.displayName = (String)displayNameAttr.get();
   		}
   		
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