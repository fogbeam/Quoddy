package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem

import grails.plugin.springsecurity.annotation.Secured

class SpecialController
{   
	
	def eventQueueService;
	
	@Secured(["ROLE_USER", "ROLE_ADMIN"])
	def queues()
	{
		Map<String, Deque<ActivityStreamItem>> eventQueues = eventQueueService.eventQueues;
		
		
		Set<String> queueKeys = eventQueues.keySet();
		
		[queueKeys : queueKeys ];
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
