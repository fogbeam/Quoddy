package org.fogbeam.quoddy.spring.listeners

import org.fogbeam.quoddy.EventQueueService
import org.fogbeam.quoddy.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.Authentication

class SuccessfulLoginListener implements ApplicationListener<AuthenticationSuccessEvent> 
{
	Logger log = LoggerFactory.getLogger( SuccessfulLoginListener.class );
	
	@Autowired
	EventQueueService eventQueueService
	
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) 
	{
		log.info( "onApplicationEvent called: ${event}" );
		
		
		Authentication authentication = event.getAuthentication();
		String userId = ((User)authentication.principal).userId;
		
		eventQueueService.registerEventQueueForUser( userId );		
	}
}
