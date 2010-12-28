package org.fogbeam.quoddy.ldap;

public class LDAPPerson 
{
	public String uuid;
	public String givenName;
	public String lastName;
	public String displayName;
	public String mail;
	public String uid;	
	public String userpassword;
	public String description;
	
	public String toString()
	{
		return( "Name: " + (displayName != null ? displayName : givenName + " " + lastName ) + ", uid: " + uid + ", email: " + mail );
	}
}
