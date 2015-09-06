package org.fogbeam.quoddy.stream.constants;


public enum EventTypes
{
	ACTIVITI_USER_TASK("ActivitiUserTask", EventTypeScopes.EVENT_TYPE_SUBSCRIPTION.name ),
	BUSINESS_EVENT_SUBSCRIPTION_ITEM("BusinessEventSubscriptionItem", EventTypeScopes.EVENT_TYPE_SUBSCRIPTION.name),
	CALENDAR_FEED_ITEM("CalendarFeedItem", EventTypeScopes.EVENT_TYPE_SUBSCRIPTION.name),
	RSS_FEED_ITEM("RssFeedItem", EventTypeScopes.EVENT_TYPE_SUBSCRIPTION.name),

	
	QUESTION( "Question", EventTypeScopes.EVENT_TYPE_USER.name ),
	STATUS_UPDATE("StatusUpdate", EventTypeScopes.EVENT_TYPE_USER.name),
	
		
	// NOTE: not sure yet exactly which scope this should get, or even if we
	// need to invent a new scope for the way this is used.  Examine how the
	// Neddick integration works and figure this out.
	ACTIVITY_STREAM_ITEM( "ActivityStreamItem", EventTypeScopes.EVENT_TYPE_USER.name),

	
	String name;
	String scope;
	
	EventTypes( String name, String scope )
	{
		this.name = name;
		this.scope = scope;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public static EventTypes fromString(String text)
	{
		if( text != null )
		{
			for( EventTypes eventTypeName : EventTypes.values() )
			{
				if (text.equalsIgnoreCase(eventTypeName.name))
				{
					return eventTypeName;
				}
			}
		}
		
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}
	
}