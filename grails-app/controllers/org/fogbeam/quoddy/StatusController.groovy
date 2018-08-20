package org.fogbeam.quoddy;

import static groovyx.net.http.ContentType.TEXT

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
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def updateStatus() 
    {
		
		User user = springSecurityService.currentUser;
	

		log.debug( "logged in; so proceeding...");
		
		// get our user
		// user = userService.findUserByUserId( session.user.userId );
		
		log.debug( "constructing our new StatusUpdate object...");
		// construct a status object
		log.debug( "statusText: ${params.statusText}");
		StatusUpdate newStatus = new StatusUpdate( text:params.statusText,creator : user);
		newStatus.effectiveDate = new Date(); // now
		newStatus.targetUuid = "ABC123";
		newStatus.name = "321CBA";
		
		
		// Hit Stanbol to get enrichmentData
		// call Stanbol REST API to get enrichment data
		String stanbolServerUrl = grailsApplication.config.urls.stanbol.endpoint;
		log.debug( "using stanbolServerUrl: ${stanbolServerUrl}");
		
		RESTClient restClient = new RESTClient( stanbolServerUrl )
		// def restClient = null;
		
		boolean enhancementEnabled = Boolean.parseBoolean( grailsApplication.config.features.enhancement.enabled ? grailsApplication.config.features.enhancement.enabled : "false" );
		log.info( "enhancementEnabled: ${enhancementEnabled}" );
        
		if( enhancementEnabled )
		{
            // TODO: move this to a Service instead of using restClient directly in this Controller
            
            try
            {
    			log.debug( "content submitted to Stanbol: ${params.statusText}");
    			def restResponse = restClient.post(	path:'enhancer',
    										body: params.statusText,
    										requestContentType : TEXT );
    									
    			// log.debug( "restResponse.class: ${restResponse.class}");
    			// log.debug( "restResponse.status: ${restResponse.status}");
    			// log.trace( "restResponse.statusLine: ${restResponse.statusLine}");
    			// log.debug( "restResponse.success: ${restResponse.isSuccess()}");
    
    			Object restResponseData = restResponse.getData();
    		
    			if( restResponseData instanceof InputStream )
    			{
    		
    				log.debug( "restResponseData.class: ${restResponseData.class}");
    		
    				java.util.Scanner s = new java.util.Scanner((InputStream)restResponseData).useDelimiter("\\A");
    
    				String restResponseText = s.next();
    		
    				log.debug( "using Scanner: ${restResponseText}");		
                    log.info( "got InputStream, using Scanner to extract" );
    				log.info( "restResponseText:\n\n ${restResponseText}\n\n");
    		
    				newStatus.enhancementJSON = restResponseText;
    		
    			}
    			else if( restResponseData instanceof net.sf.json.JSONObject )
    			{
                    String restResponseText = restResponseData.toString();
                    log.info( "got JSONObject, using toString() to extract" );
                    log.info( "restResponseText:\n\n ${restResponseText}\n\n");
    				newStatus.enhancementJSON = restResponseText;
    			}
    			else if( restResponseData instanceof java.lang.String )
    			{
                    log.info( "got String, no extraction required" );
                    log.info( "restResponseData:\n\n ${restResponseData}\n\n");
    				newStatus.enhancementJSON = new String( restResponseData );
    			}
    			else
    			{
                    log.info( "got Other (${restResponseData.getClass().getName()}), using toString() to extract" );
                    String restResponseText = new JsonBuilder(restResponseData).toPrettyString();
                    log.info( "restResponseText:\n\n ${restResponseText}\n\n");
    				newStatus.enhancementJSON = restResponseText;
    			}
            }
            catch( ConnectException ce )
            {
                log.error( "Could not connect to Stanbol server: ", ce );
                newStatus.enhancementJSON = "";
            }
            catch( Exception e )
            {
                log.error( "Error doing semantic enhancement: ", e );
                newStatus.enhancementJSON = "";
            }
		}
		else
		{
            log.info( "semantic enhancement is NOT enabled" );
			newStatus.enhancementJSON = "";
		}
		
		// save the newStatus 
		if( !newStatus.save(flush:true) )
		{
			log.error(( "Saving newStatus FAILED"));
			newStatus.errors.allErrors.each { log.error( it ) };
		}
		
		// put the old "currentStatus" in the oldStatusUpdates collection
		// addToComments
		if( user.currentStatus != null )
		{
			StatusUpdate previousStatus = user.currentStatus;
			// TODO: do we need to detach this or something?
			user.addToOldStatusUpdates( previousStatus );
		}
		
		// set the current status
		log.debug( "setting currentStatus");
		user.currentStatus = newStatus;
		if( !user.save(flush:true) )
		{
			log.debug( "Saving user FAILED");
			user.errors.allErrors.each { log.debug( it ) };
		}
		else
		{
			// handle failure to update User
		}
		
		session.user = user;
		
		// TODO: if the user update was successful
		ActivityStreamItem activity = new ActivityStreamItem(content:newStatus.text);
		ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );

		
		activity.title = "Internal ActivityStreamItem";
		activity.url = new URL( "http://www.example.com" );
		activity.verb = "quoddy_status_update";
		activity.actorObjectType = "User";
		activity.actorUuid = user.uuid;
		activity.targetObjectType = "STREAM_PUBLIC";
		activity.published = new Date(); // set published to "now"
		activity.targetUuid = streamPublic.uuid;
		activity.owner = user;
		activity.streamObject = newStatus;
		activity.objectClass = EventTypes.STATUS_UPDATE.name;    
		

		// NOTE: we added "name" to StreamItemBase, but how is it really going
		// to be used?  Do we *really* need this??
		activity.name = activity.title;
		// activity.effectiveDate = activity.published;
		
		eventStreamService.saveActivity( activity );
		
        log.debug( "sending message to JMS");
        
		def newContentMsg = [msgType:'NEW_STATUS_UPDATE', activityId:activity.id, activityUuid:activity.uuid ];
        jmsService.send("quoddySearchQueue", newContentMsg );		
 		
        def activityMsg = [msgType: 'ACTIVITY_STREAM_ITEM', activityId:activity.id, activityUuid:activity.uuid ];
        jmsService.send( 'uitestActivityQueue', activityMsg );
		
		log.debug( "redirecting to home:index");
		redirect( controller:"home", action:"index", params:[userId:user.userId]);
	}

    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def listUpdates()
	{
		User user = null;
		List<StatusUpdate> updates = new ArrayList<StatusUpdate>();

			
		// get our user
		user = userService.findUserByUserId( session.user.userId );
		
		updates.addAll( user.oldStatusUpdates.sort { it.dateCreated }.reverse() );

		
		[updates:updates]
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def deleteStatus()
	{
		String delItemUuid = params.item;
		ActivityStreamItem item = ActivityStreamItem.findByUuid( delItemUuid );
		
		if( item != null )
		{
			log.debug( "found it!" );
			item.delete();
			
		}
		else
		{
			log.debug( "nope" );
		}
		
		render( status: 200 );
	}
}