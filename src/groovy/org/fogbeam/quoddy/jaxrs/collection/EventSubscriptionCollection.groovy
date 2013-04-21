package org.fogbeam.quoddy.jaxrs.collection

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement

import org.fogbeam.quoddy.subscription.BusinessEventSubscription;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class EventSubscriptionCollection
{
	@XmlElementWrapper(name="subscriptions")
	@XmlElement(name="subscription")
	private List<BusinessEventSubscription> subscriptions = new ArrayList<BusinessEventSubscription>();
	
	public List<BusinessEventSubscription> getSubscriptions()
	{
		return this.subscriptions;
	}
	
	public void setSubscriptions( final List<BusinessEventSubscription> subscriptions )
	{
		this.subscriptions = subscriptions;	
	}
	
	public void addAll( final List<BusinessEventSubscription> subscriptions)
	{
		this.subscriptions.addAll( subscriptions ); 	
	}
}
