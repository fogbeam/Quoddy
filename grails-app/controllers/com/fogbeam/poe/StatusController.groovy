package com.fogbeam.poe

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
			StatusUpdate newStatus = new StatusUpdate( text: params.statusText, creator: user );
			
			// move the old "current status" into the "old status" list
			StatusUpdate oldStatus = user.currentStatus;
			if( oldStatus != null )
			{
				println "adding oldStatus to old status list...";
				user.addToOldStatusUpdates( oldStatus );
			}
			
			// set the current status
			println "setting currentStatus";
			user.currentStatus = newStatus;
			
			println "updating user";
			userService.updateUser( user );
			
			// TODO: if the user update was successful
			Activity activity = new Activity(text:newStatus.text);
			activity.creator = user;
			activityStreamService.saveActivity( activity );
			
		}
		
		println "redirecting to home:index";
		redirect( controller:"home", action:"index", params:[userId:user.userId]);
	}
}
