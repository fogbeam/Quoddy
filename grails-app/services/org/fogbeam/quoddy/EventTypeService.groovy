package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.EventType;

class EventTypeService
{
	public Set<EventType> findAllEventTypes()
	{
		Set<EventType> allEventTypes = new HashSet<EventType>();
		Collection eventTypeQueryResults = EventType.findAll();
		
		allEventTypes.addAll( eventTypeQueryResults );
		
		return allEventTypes; 	
	}
	
	public EventType findEventTypeById( final Long eventTypeId )
	{
		EventType eventType = EventType.findById( eventTypeId );
		
		return eventType;	
	}
	
}
