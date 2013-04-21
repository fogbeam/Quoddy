package org.fogbeam.quoddy.stream

import org.fogbeam.quoddy.subscription.BusinessEventSubscription;

class BusinessEventSubscriptionItem extends StreamItemBase implements Serializable
{
	static constraints = {
		owningSubscription(nullable:false);
	}
	
	
	BusinessEventSubscription owningSubscription;
	
	
	static transients = ['templateName', 'xmlDoc'];
	
	transient org.w3c.dom.Node xmlDoc;
	
	// we'll read this from the XML database, using the UUID stored in our DB
	// and populate this just-in-time for rendering in the stream.
	String xmlUuid;
	
	public String getTemplateName()
	{
		return "/renderSubscriptionEvent";
	}
}
