package org.fogbeam.quoddy

class DummyController
{
	def index = {
	
		def events = 
			EventBase.executeQuery( 
				"select event from EventBase as event where " 
				+ " event.class = SubscriptionEvent " 
				+ " or " 
				+ " event.class = Activity" );
		
		[events:events];	
	}
}
