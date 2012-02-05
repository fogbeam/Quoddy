package org.fogbeam.quoddy

class CalendarEvent
{
	Date startDate;
	Date endDate;
	String status;
	String summary;
	String description;
	Date dateEventCreated;
	float geoLat;
	float geoLong;
	String location;
	String url;
	Date lastModified;
	String uid;
	
	public LocalAccount()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	String uuid;
	Date dateCreated;
	
}
