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
	
	public EventType findEventTypeByName( final String name )
	{
		EventType eventType = EventType.findByName( name );
		
		return eventType;
	}
	
	public Set<EventType> findEventTypesByScope( final String scope )
	{
		println "findEventTypeByScope() called, scope = ${scope}";
		
		Set<EventType> eventTypes = EventType.executeQuery( "select et from EventType as et where et.scope = :scope", [scope:scope] );
		
		return eventTypes;
	}
	
}
