package org.fogbeam.quoddy.subscription

import org.fogbeam.quoddy.User;

class CalendarFeedSubscription
{
	public CalendarFeedSubscription()
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
