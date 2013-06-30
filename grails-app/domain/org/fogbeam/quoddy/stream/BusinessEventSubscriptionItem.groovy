package org.fogbeam.quoddy.stream

import org.fogbeam.quoddy.subscription.BusinessEventSubscription;

class BusinessEventSubscriptionItem extends StreamItemBase
{
	static constraints = {
		summary( size: 0..1024, nullable:true);
	}
	
	static transients = ['templateName', 'xmlDoc'];
	
	transient org.w3c.dom.Node xmlDoc;
	
	
	// we'll read this from the XML database, using the UUID stored in our DB
	// and populate this just-in-time for rendering in the stream.
	String xmlUuid;
	
	String summary;
	
	public String getTemplateName()
	{
		println "returning /renderSubscriptionEvent";
		return "/renderSubscriptionEvent";
	}
}
