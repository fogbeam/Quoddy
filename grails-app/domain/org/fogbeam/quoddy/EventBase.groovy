package org.fogbeam.quoddy

public class EventBase implements Serializable
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
