package org.fogbeam.quoddy.stream.constants;

public enum EventTypeScopes 
{

	EVENT_TYPE_USER("EventTypeUser"),
	EVENT_TYPE_SUBSCRIPTION("EventTypeSubscription")
	
	String name;
	
	EventTypeScopes( String val )
	{
		this.name = val;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	
	public static EventTypeScopes fromString(String text)
	{
		if( text != null )
		{
			for( EventTypeScopes eventTypeScope : EventTypeScopes.values() )
			{
				if (text.equalsIgnoreCase(eventTypeScope.name))
				{
					return eventTypeScope;
				}
			}
		}
		
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}

	
}
