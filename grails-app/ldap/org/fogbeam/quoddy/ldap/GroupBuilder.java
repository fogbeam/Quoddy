package org.fogbeam.quoddy.ldap;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.springframework.ldap.core.DistinguishedName;

public class GroupBuilder 
{
	public static Attributes buildAttributes(Group g) 
	{
	   Attributes attrs = new BasicAttributes();
	   BasicAttribute ocattr = new BasicAttribute("objectclass");
	   ocattr.add("top");
	   ocattr.add("groupOfUniqueNames");
	   attrs.put(ocattr);
	   attrs.put( "owner", g.owner );
	   
	   /* TODO: members at group bind time? */
	   attrs.put( "uniquemember", "cn=hidden" );
		   return attrs;
	}
	
	public static Name buildFollowGroupDn(Group g, String BaseDN) 
	{
		DistinguishedName dn = new DistinguishedName(BaseDN);
		dn.add("ou", "groups" );
		dn.add("ou", "followgroups" );
		dn.add("cn", g.name );
		
		return dn;
	}

	public static Name buildConfirmedFriendsGroupDn(Group g, String BaseDN) 
	{
		DistinguishedName dn = new DistinguishedName(BaseDN);
		dn.add("ou", "groups" );
		dn.add("ou", "confirmedfriends" );
		dn.add("cn", g.name );
		
		return dn;
	}

	public static Name buildUnconfirmedFriendsGroupDn(Group g, String BaseDN) 
	{
		DistinguishedName dn = new DistinguishedName(BaseDN);
		dn.add("ou", "groups" );
		dn.add("ou", "unconfirmedfriends" );
		dn.add("cn", g.name );
		
		return dn;
	}

}
