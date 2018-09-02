package org.fogbeam.quoddy;

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.stream.constants.EventTypes

import grails.plugin.springsecurity.annotation.Secured
import groovy.json.JsonBuilder
import groovyx.net.http.RESTClient

class StatusController 
{	
	def userService;
	def eventStreamService;
	def jmsService;
    def springSecurityService;
    def stanbolService;
	def statusUpdateService;
	
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def updateStatus() 
    {	
		User currentUser = userService.getLoggedInUser();
		
		log.debug( "constructing our new StatusUpdate object...");
		
		// construct a status object
		log.debug( "statusText: ${params.statusText}");
		
		StatusUpdate newStatus = new StatusUpdate( text:params.statusText,creator : currentUser);
		newStatus.effectiveDate = new Date(); // now
		
		// TODO: fix this
		newStatus.targetUuid = "ABC123";
		
		// TODO: fix this
		newStatus.name = "321CBA";
		
		
		// Hit Stanbol to get enrichmentData
				
		boolean enhancementEnabled = Boolean.parseBoolean( grailsApplication.config.features.enhancement.enabled ? grailsApplication.config.features.enhancement.enabled : "false" );
		log.info( "enhancementEnabled: ${enhancementEnabled}" );
        
		if( enhancementEnabled )
		{
			String enhancementJson = stanbolService.getEnhancementData( params.statusText );
			newStatus.enhancementJSON = enhancementJson;
		}
		else
		{
            log.info( "semantic enhancement is NOT enabled" );
			newStatus.enhancementJSON = "";
		}
		
		
		// use statusUpdateService here to save our new StatusUpdate
		statusUpdateService.save( newStatus, currentUser );
				
		log.debug( "redirecting to home:index");
		redirect( controller:"home", action:"index", params:[userId:currentUser.userId]);
	}

    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def listUpdates()
	{
		List<StatusUpdate> updates = new ArrayList<StatusUpdate>();

		User user = userService.getLoggedInUser();
				
		updates.addAll( user.oldStatusUpdates.sort { it.dateCreated }.reverse() );
		
		[updates:updates]
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def deleteStatus()
	{
		String delItemUuid = params.item;
		
		eventStreamService.deleteByUuid( delItemUuid );
		
		render( status: 200 );
	}
}