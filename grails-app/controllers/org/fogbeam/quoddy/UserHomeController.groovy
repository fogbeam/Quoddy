package org.fogbeam.quoddy

class UserHomeController 
{
	def userService;
	
	def index = 
	{		
		String userId = params.id;
		
		User user = userService.findUserByUserId( userId );		
		
		[user:user];	
	}
}
