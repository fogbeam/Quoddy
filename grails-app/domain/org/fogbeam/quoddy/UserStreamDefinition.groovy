package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.EventType;

/* a UserStream object defines one specific named "stream" of entries that will
 * appear in the user's feed.  The UserStream controls which users, which groups, etc.
 * are included, as well as an filters or other preferences that will affect which items
 * are included in the stream.
 */
class UserStreamDefinition implements Serializable
{
	public static final String DEFINED_USER = "DEFINED_BY_USER";
	public static final String DEFINED_SYSTEM = "DEFINED_BY_SYSTEM";
	public static final String DEFAULT_STREAM = "Default";
	
	
	public UserStreamDefinition()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	static constraints = {
		
		description(nullable:true);
			
	}
	
	static hasMany = [ userUuidsIncluded:String, 
					   userListUuidsIncluded:String, 
					   userGroupUuidsIncluded:String,
					   subscriptionUuidsIncluded:String,
					   eventTypesIncluded:EventType];
	
	String 	name;
	String 	uuid;
	String 	definedBy;
	User 	owner;
	Date 	dateCreated;
	String 	description;
	

	// include:
	// users
	Boolean includeSelfOnly = false;
	Boolean includeAllUsers = false;
	
	// user lists
	Boolean includeAllLists = false;
	
	// groups
	Boolean includeAllGroups = false;

	
	// subscriptions
	Boolean includeAllSubscriptions = false;
	

	// event types
	Boolean includeAllEventTypes = false;

	
	
	// exclude (filters)
		// event types
		// users
		// user lists
		// groups
		// subscriptions
		// content

	
	public void addEventTypesToInclude( final Set<EventType> eventTypesToInclude ) 
	{
		this.eventTypesIncluded.addAll( eventTypesToInclude ); 
	}	
	
	public Set<EventType> getEventTypesToInclude()
	{
		return this.eventTypesIncluded
	}
	
	
	public String toString()
	{
		return "id: ${id}, uuid: ${uuid}, name: ${name}, owner: ${owner}, dateCreated: ${dateCreated}";
	}
}