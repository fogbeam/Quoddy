package org.fogbeam.quoddy.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class GroupAttributeMapper implements AttributesMapper
{
	private LdapTemplate ldapTemplate;
	
	public GroupAttributeMapper( LdapTemplate ldapTemplate )
	{
		this.ldapTemplate = ldapTemplate;
	}
	
	public Object mapFromAttributes(Attributes attrs) throws NamingException 
	{
		Group group = new Group();
   	
		group.name = (String)attrs.get("cn").get();
		group.owner = (String)attrs.get("owner").get();
		NamingEnumeration<?> names = attrs.get("uniquemember").getAll();
   	
		while( names.hasMoreElements() ) 
		{
			String memberName = (String) names.nextElement();
			
			if( memberName.equalsIgnoreCase("cn=hidden"))
			{
				continue;
			}
			
			group.memberNames.add(memberName);

			// System.out.println( "looking for dn: " + memberName );
			Person person =  (Person)ldapTemplate.lookup(memberName, new PersonAttributeMapper() );
   	
			group.members.add( person );   	
		}
   	
		return group;      
	}
}
