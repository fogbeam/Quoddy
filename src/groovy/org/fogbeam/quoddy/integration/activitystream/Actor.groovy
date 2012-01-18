package org.fogbeam.quoddy.integration.activitystream

class Actor {

    static constraints = {
    }

	String displayName;
	String objectType;
	String id;
	String url;
	Image image;
	
	public String toString()
	{
		return "Actor: displayName: ${displayName}, objectType: ${objectType}, id: ${id}, url: ${url}, image: ${image}";	
	}		
}
