package org.fogbeam.quoddy.subscription

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

import org.fogbeam.quoddy.User;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class BusinessEventSubscription extends BaseSubscription implements Serializable
{
	@XmlElement
	String 	xQueryExpression;

		
	public String toString()
	{
		return "BusinessEventSubscription[ uuid: ${uuid}, name: ${name}, owner: ${owner?.userId}, xQuery: ${xQueryExpression}]";	
	}		
}
