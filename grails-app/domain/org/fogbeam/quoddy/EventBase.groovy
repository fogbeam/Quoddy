package org.fogbeam.quoddy

public class EventBase implements Serializable
{
	static mapping = 
	{
		tablePerHierarchy false
		comments sort:'dateCreated', order:'asc'
	}
		
	static constraints = 
	{
		owner(nullable:true);
	}
	
	static hasMany = [ comments: Comment /* votes : Vote, savers: User, hiders: User, tagEntryLinks:TagEntryLink */  ];
	
	
	User owner;
	Date dateCreated;
	Date effectiveDate; // TODO: should be Timestamp?
	String name; // NOTE: do we really need this???
	String	targetUuid;
	
}
