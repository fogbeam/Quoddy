package org.fogbeam.quoddy.jms

public class NewEntryListenerService {

	static int count = 0;
	static expose = ['jms'];
	static destination = "quoddyEntryQueue"
	
	def sessionFactory;

	def onMessage(msg)
	{
		count++;
		log.debug( "NewEntryListenerService.onMessage: received message number: ${count}" );	
		
	}
	
	
}
