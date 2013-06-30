package org.fogbeam.quoddy.subscription

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

import org.fogbeam.quoddy.User

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class BaseSubscription implements Serializable
{
	public BaseSubscription()
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
	User 	owner;
	
	static mapping = {
		owner lazy:false; // eagerly fetch the owner
	}
	
	static constraints = {
		description( nullable:true)
	}
}
