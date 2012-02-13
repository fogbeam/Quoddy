package org.fogbeam.quoddy

class EventBase
{
	
	static mapping = 
	{
		tablePerHierarchy false
	}
	
	static constraints = 
	{
		owner(nullable:true);
	}
	
	User owner;
	Date dateCreated;
	Date effectiveDate;
	String name; // NOTE: do we really need this???
	String	targetUuid;
	
}
