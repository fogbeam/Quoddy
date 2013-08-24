package org.fogbeam.quoddy.stream.constants;


public enum EventTypeNames
{
	ACTIVITI_USER_TASK("ActivitiUserTask"),
	ACTIVITY_STREAM_ITEM( "ActivityStreamItem"),
	BUSINESS_EVENT_SUBSCRIPTION_ITEM("BusinessEventSubscriptionItem"),
	CALENDAR_FEED_ITEM("CalendarFeedItem"),
	RSS_FEED_ITEM("RssFeedItem"),
	QUESTION( "Question" ),
	STATUS_UPDATE("StatusUpdate")

	
	String name;
	
	EventTypeNames( String val )
	{
		this.name = val;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public static EventTypeNames fromString(String text)
	{
		if( text != null )
		{
			for( EventTypeNames eventTypeName : EventTypeNames.values() )
			{
				if (text.equalsIgnoreCase(eventTypeName.value))
				{
					return eventTypeName;
				}
			}
		}
		
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}
	
}