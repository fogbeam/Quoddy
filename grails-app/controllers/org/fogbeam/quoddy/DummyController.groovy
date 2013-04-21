package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.StreamItemBase;

class DummyController
{
	def index = {
	
		def events = 
			StreamItemBase.executeQuery( 
				"select event from StreamItemBase as event where " 
				+ " event.class = BusinessEventSubscriptionItem " 
				+ " or " 
				+ " event.class = ActivityStreamItem" );
		
		[events:events];	
	}
}
