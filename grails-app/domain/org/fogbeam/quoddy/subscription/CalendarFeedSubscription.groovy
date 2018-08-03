package org.fogbeam.quoddy.subscription

import org.fogbeam.quoddy.User;

class CalendarFeedSubscription extends BaseSubscription implements Serializable
{

	
	static constraints =
	{}

	// feedType?  iCal, SyncML, CalDav, etc??
	
	String	url;
	
}
