package org.fogbeam.quoddy

public class LocalAccount 
{
	// we'll just need username/password for now.  And a uuid or whatever
	// hangs it all together.
	
	public LocalAccount()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	String uuid;
	String username;
	String password;
}
