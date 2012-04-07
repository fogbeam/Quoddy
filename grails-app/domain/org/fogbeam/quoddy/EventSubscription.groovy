package org.fogbeam.quoddy

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class EventSubscription implements Serializable
{
	public EventSubscription()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	@XmlElement
	String 	uuid;
	@XmlElement
	Date 	dateCreated;
	@XmlElement
	String 	name;
	@XmlElement
	String 	description;
	@XmlElement
	String 	xQueryExpression;
	@XmlElement
	User 	owner;
	
	static mapping = {
		owner lazy:false; // eagerly fetch the owner
	}

	public String toString()
	{
		return "EventSubscription[ uuid: ${uuid}, name: ${name}, owner: ${owner.userId}, xQuery: ${xQueryExpression}]";	
	}	
}
