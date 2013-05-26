package org.fogbeam.quoddy.stream

import org.fogbeam.quoddy.User;

public class StreamItemBase implements Serializable
{
	
	public StreamItemBase()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	static mapping = 
	{
		tablePerHierarchy false
		comments sort:'dateCreated', order:'asc'
	}
		
	static constraints = 
	{
		owner(nullable:true);
	}
	
	static hasMany = [ comments: StreamItemComment /* votes : Vote, savers: User, hiders: User, tagEntryLinks:TagEntryLink */  ];
	
	
	User owner;
	String uuid;
	Date dateCreated;
	Date effectiveDate; // TODO: should be Timestamp?
	String name; // NOTE: do we really need this???
	String	targetUuid; // UUID of the "thing" this stream item was targeted at.  A group, etc.
						// question is, do we really need this for internal events if we have the
						// notion of a ShareTarget domain object?  How would this
						// be used any differently? 
	
}
