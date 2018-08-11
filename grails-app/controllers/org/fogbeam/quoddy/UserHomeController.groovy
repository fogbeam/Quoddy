package org.fogbeam.quoddy

import grails.plugin.springsecurity.annotation.Secured

class UserHomeController 
{
	def userService;
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{		
		String userId = params.id;
		
		User user = userService.findUserByUserId( userId );		
		
		[user:user];	
	}
}
