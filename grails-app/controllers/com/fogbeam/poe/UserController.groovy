package com.fogbeam.poe

class UserController {

	def userService;
	def scaffold = true;

	def viewUser = {
		
		def userId = params.userId;
		def user = null;
		if( null != userId )
		{
			// lookup this specific user by params and put in the model for display	
			user = userService.findUserByUserId( userId );
		}
		else 
		{
			flash.message = "invalid userId";
		}
		
		[user:user];
		
	}

	def addToFriends = {
		
		def user = null;
		if( !session.user ) {
			flash.message = "Must be logged in before you can add friends";
		}
		else
		{
			println "addToFriends: ${params.userId}";
		
			def currentUser = userService.findUserByUserId( session.user.userId );
			
			user = userService.findUserByUserId( params.userId );
		
			currentUser.addToFriends( user );
			
		}
		
		render(view:'viewUser', model:[user:user]);
	}

	def listFriends = {

		def user = null;
		if( !session.user ) {
			flash.message = "Must be logged in before you can list friends";
		}
		else
		{
			user = userService.findUserByUserId( session.user.userId )
		}
	
		[user:user];
	}
		
}
