package org.fogbeam.quoddy

import org.fogbeam.quoddy.Activity;
import org.fogbeam.quoddy.StatusUpdate;
import org.fogbeam.quoddy.User;

class StatusController {

	def userService;
	def activityStreamService;
	
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
			
			// set the current status
			println "setting currentStatus";
			user.currentStatus = newStatus;
			user.save();
			session.user = user;
			
			// TODO: if the user update was successful
			Activity activity = new Activity(text:newStatus.text);
			activity.creator = user;
			activityStreamService.saveActivity( activity );
			
		}
		
		println "redirecting to home:index";
		redirect( controller:"home", action:"index", params:[userId:user.userId]);
	}
}
