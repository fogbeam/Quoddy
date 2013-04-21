package org.fogbeam.quoddy;

import org.fogbeam.quoddy.StatusUpdate;
import org.fogbeam.quoddy.User;
import org.fogbeam.quoddy.stream.ActivityStreamItem;
import org.fogbeam.quoddy.stream.ShareTarget;

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
			StatusUpdate newStatus = new StatusUpdate( text: params.statusText, creator: user );
			
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
			activity.published = new Date(); // set published to "now"
			activity.targetUuid = streamPublic.uuid;
			activity.owner = user;
			
			// NOTE: we added "name" to StreamItemBase, but how is it really going
			// to be used?  Do we *really* need this??
			activity.name = activity.title;
			activity.effectiveDate = activity.published;
			
			eventStreamService.saveActivity( activity );
			
			
			def msg = [msgType:'NEW_STATUS_UPDATE', activityId:activity.id, activityUuid:activity.uuid ];
				
			println "sending message to JMS";
			// jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
			sendJMSMessage("quoddySearchQueue", msg );
			
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
