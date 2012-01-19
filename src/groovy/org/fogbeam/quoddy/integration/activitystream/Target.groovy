package org.fogbeam.quoddy.integration.activitystream

class Target
{
	String url;
	String objectType;
	String id;
	String displayName;
	
	public String toString()
	{
		return "Target: url: ${url}, objectType: ${objectType}, id: ${id}, displayName: ${displayName}";	
	}
}
