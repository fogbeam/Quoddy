package org.fogbeam.quoddy;

import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.TEXT
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.stream.constants.EventTypeNames

class StatusController {

	def userService;
	def eventStreamService;
	def jmsService;
	
	def updateStatus = {
		
		User user = null;
		
		if( !session.user )
		{
			flash.message = "Must be logged in before updating status";
		}
		else
		{
			println "logged in; so proceeding...";
			
			// get our user
			user = userService.findUserByUserId( session.user.userId );
			
			println "constructing our new StatusUpdate object...";
			// construct a status object
			println "statusText: ${params.statusText}";
			StatusUpdate newStatus = new StatusUpdate( text:params.statusText,creator : user);
			newStatus.effectiveDate = new Date(); // now
			newStatus.targetUuid = "ABC123";
			newStatus.name = "321CBA";
			
			
			// Hit Stanbol to get enrichmentData
			// call Stanbol REST API to get enrichment data
			RESTClient restClient = new RESTClient( "http://localhost:8080" )
		
			// println "content submitted: ${content}";
			def restResponse = restClient.post(	path:'enhancer',
											body: params.statusText,
											requestContentType : TEXT );
		
			String restResponseText = restResponse.getData();
			
			println "restResponseText: ${restResponseText}";
			newStatus.enhancementJSON = restResponseText;
			
			// save the newStatus 
			if( !newStatus.save() )
			{
				println( "Saving newStatus FAILED");
				newStatus.errors.allErrors.each { println it };
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
			println "setting currentStatus";
			user.currentStatus = newStatus;
			if( !user.save() )
			{
				println( "Saving user FAILED");
				user.errors.allErrors.each { println it };
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
			activity.objectClass = EventTypeNames.STATUS_UPDATE.name;    
			

			// NOTE: we added "name" to StreamItemBase, but how is it really going
			// to be used?  Do we *really* need this??
			activity.name = activity.title;
			// activity.effectiveDate = activity.published;
			
			eventStreamService.saveActivity( activity );
			
			
			def newContentMsg = [msgType:'NEW_STATUS_UPDATE', activityId:activity.id, activityUuid:activity.uuid ];
				
			println "sending message to JMS";
			// jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
			sendJMSMessage("quoddySearchQueue", newContentMsg );
			
			jmsService.send( queue: 'uitestActivityQueue', activity, 'standard', null );
			
		}
		
		println "redirecting to home:index";
		redirect( controller:"home", action:"index", params:[userId:user.userId]);
	}

	def listUpdates =
	{
		User user = null;
		List<StatusUpdate> updates = new ArrayList<StatusUpdate>();
		
		if( !session.user )
		{
			flash.message = "Must be logged in before updating status";
		}
		else
		{
			println "logged in; so proceeding...";
			
			// get our user
			user = userService.findUserByUserId( session.user.userId );
			
			updates.addAll( user.oldStatusUpdates.sort { it.dateCreated }.reverse() );
		}
		
		[updates:updates]
	}
	
}
