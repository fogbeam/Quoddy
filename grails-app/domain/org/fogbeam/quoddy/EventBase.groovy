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
	Date effectiveDate; // TODO: should be Timestamp?
	String name; // NOTE: do we really need this???
	String	targetUuid;
	
}
