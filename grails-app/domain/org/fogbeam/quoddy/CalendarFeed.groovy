package org.fogbeam.quoddy

class CalendarFeed
{
	public CalendarFeed()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	static constraints =
	{}

	// feedType?  iCal, SyncML, CalDav, etc??
	
	String	url;
	User 	owner;
	String 	name;
	String 	uuid;
	Date 	dateCreated;
	
}
