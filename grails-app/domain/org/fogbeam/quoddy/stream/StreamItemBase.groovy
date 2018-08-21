package org.fogbeam.quoddy.stream

import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.subscription.BaseSubscription

public abstract class StreamItemBase implements Serializable
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
		owningSubscription(nullable:true);
	}
	
	static hasMany = [ comments: StreamItemComment /* votes : Vote, savers: User, hiders: User, tagEntryLinks:TagEntryLink */  ];
	
	
	User owner;
	String uuid;
	Date dateCreated;
	Date effectiveDate; // TODO: should be Timestamp?
	String name; // NOTE: do we really need this???
	
	
	// TODO: figure out if we can delete this now, since we always attach these items to
	// an ActivityStreamItem instance which already has a targetUuid.  
	String	targetUuid; // UUID of the "thing" this stream item was targeted at.  A group, etc.
						// question is, do we really need this for internal events if we have the
						// notion of a ShareTarget domain object?  How would this
						// be used any differently?
	
	BaseSubscription owningSubscription; 
	
    abstract String getTemplateName();
}
