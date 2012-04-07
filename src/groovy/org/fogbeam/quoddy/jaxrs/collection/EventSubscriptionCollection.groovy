package org.fogbeam.quoddy.jaxrs.collection

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement

import org.fogbeam.quoddy.EventSubscription

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class EventSubscriptionCollection
{
	@XmlElementWrapper(name="subscriptions")
	@XmlElement(name="subscription")
	private List<EventSubscription> subscriptions = new ArrayList<EventSubscription>();
	
	public List<EventSubscription> getSubscriptions()
	{
		return this.subscriptions;
	}
	
	public void setSubscriptions( final List<EventSubscription> subscriptions )
	{
		this.subscriptions = subscriptions;	
	}
	
	public void addAll( final List<EventSubscription> subscriptions)
	{
		this.subscriptions.addAll( subscriptions ); 	
	}
}
