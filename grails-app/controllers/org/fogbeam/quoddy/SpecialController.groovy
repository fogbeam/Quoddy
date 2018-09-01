package org.fogbeam.quoddy

import java.util.Map.Entry

import org.fogbeam.quoddy.stream.ActivityStreamItem

import grails.plugin.springsecurity.annotation.Secured

class SpecialController
{   
	
	def eventQueueService;
	
	@Secured(["ROLE_USER", "ROLE_ADMIN"])
	def queues()
	{
		Map<String, Deque<ActivityStreamItem>> eventQueues = eventQueueService.eventQueues;
		
		log.info( "eventQueues: ${eventQueues.size()}");
		
		Set<Entry<String, Deque<ActivityStreamItem>>> entries = eventQueues.entrySet();
		
		List<Tuple> entryKeysAndSizes = new ArrayList<Tuple>();
		
		for( Entry<String, Deque<ActivityStreamItem>> entry : entries )
		{
			log.info( "found entry: ${entry}");
			
			Tuple entryTuple = new Tuple( entry.key, entry.value.size() );
			entryKeysAndSizes.add( entryTuple );	
		}
		
		log.info( "returning entryKeysAndSizes with size = " + entryKeysAndSizes.size() );
		
		[entryKeysAndSizes:entryKeysAndSizes ];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{   
        println( "BrokerUrl: ${grailsApplication.config.spring.activemq.brokerUrl}" );
        println( "InMemory: ${grailsApplication.config.spring.activemq.'in-memory'}");        
        println( "EmailServiceBackend: ${grailsApplication.config.emailservice.backend}");
        
        
		switch( request.method )
		{
			case "POST":
				log.debug( "POST: ${params}");
				log.debug( "text:\n\n ${request.reader.text}\n\n");
				break;
			default:
				log.debug( "OTHER: ${params}");
				log.debug( "text:\n\n ${request.reader.text}\n\n");
				break;
		}
		
		render(text: "OK", contentType: "text/plain", encoding: "UTF-8")
	}
}
