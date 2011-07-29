package org.fogbeam.quoddy

import org.fogbeam.quoddy.Activity;
import org.fogbeam.quoddy.StatusUpdate;
import org.fogbeam.quoddy.User;

class StatusController {

	def userService;
	def activityStreamService;
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
			def originTime = new Date().getTime();
			Activity activity = new Activity(text:newStatus.text);
			activity.creator = user;
			activity.originTime = originTime;
			activityStreamService.saveActivity( activity );
			
			
			Map msg = new HashMap();
			msg.creator = activity.creator.userId;
			msg.text = newStatus.text;
			msg.originTime = originTime;
			
			println "sending message to JMS";
			jmsService.send( queue: 'uitestActivityQueue', msg, 'standard', null );
			
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
