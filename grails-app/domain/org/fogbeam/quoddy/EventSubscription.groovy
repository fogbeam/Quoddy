package org.fogbeam.quoddy

class EventSubscription implements Serializable
{
	public EventSubscription()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	String 	uuid;
	Date 	dateCreated;
	String 	name;
	String 	description;
	String 	xQueryExpression;
	User 	owner;
	
}
