package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.stream.constants.EventTypes
import org.springframework.beans.factory.annotation.Autowired

import grails.core.GrailsApplication

class StatusUpdateService 
{
	@Autowired
	GrailsApplication grailsApplication;
	
	def eventStreamService;
	def userService;
	def jmsService;
	
	public StatusUpdate save( final StatusUpdate statusToSave )
	{
		if( !statusToSave.save(flush:true) )
		{
			log.error( "Save StatusUpdate FAILED!");
			statusToSave.errors.allErrors.each { log.error( it.toString() ) };
		}
	}
	
	public void save( final StatusUpdate newStatusUpdate, final User currentUser )
	{
		
		// save the newStatus
		if( !newStatusUpdate.save(flush:true) )
		{
			log.error(( "Saving newStatus FAILED"));
			newStatusUpdate.errors.allErrors.each { log.error( it.toString() ) };
		}
		
		// put the old "currentStatus" in the oldStatusUpdates collection
		// addToComments
		if( currentUser.currentStatus != null )
		{
			StatusUpdate previousStatus = currentUser.currentStatus;
			// TODO: do we need to detach this or something?
			currentUser.addToOldStatusUpdates( previousStatus );
		}
		
		// set the current status
		log.debug( "setting currentStatus");
		currentUser.currentStatus = newStatusUpdate;
		
		// TODO: call userService to do this
		if( !currentUser.save(flush:true) )
		{
			log.debug( "Saving user FAILED");
			user.errors.allErrors.each { log.error( it.toString() ) };
		}
		else
		{
			// handle failure to update User
		}
		
		// TODO: if the user update was successful
		ActivityStreamItem activity = new ActivityStreamItem(content:newStatusUpdate.text);
		ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );

		
		activity.title = "Internal ActivityStreamItem";
		activity.url = new URL( "http://www.example.com" );
		activity.verb = "quoddy_status_update";
		activity.actorObjectType = "User";
		activity.actorUuid = currentUser.uuid;
		activity.targetObjectType = "STREAM_PUBLIC";
		activity.published = new Date(); // set published to "now"
		activity.targetUuid = streamPublic.uuid;
		activity.owner = currentUser;
		activity.streamObject = newStatusUpdate;
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
	}
	
	public void update( final StatusUpdate statusUpdateToUpdate )
	{
		
	}
}
