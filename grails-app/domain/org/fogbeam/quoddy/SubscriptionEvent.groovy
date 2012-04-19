package org.fogbeam.quoddy

class SubscriptionEvent extends EventBase
{
	static constraints = {
	}
	
	static transients = ['templateName'];
	
	// we'll read this from the XML database, using the UUID stored in our DB
	// and populate this just-in-time for rendering in the stream.
	String xmlUuid;
	
	public String getTemplateName()
	{
		return "/renderSubscriptionEvent";
	}
	
}
