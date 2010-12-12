package org.fogbeam.quoddy

class HomeController {

	def userService;
	def activityStreamService;
	
    def index = {
    		
    	def userId = params.userId;
    	def user = null;
    	if( userId != null )
    	{
    		user = userService.findUserByUserId( userId );
    	}
    	else
    	{
    		if( session.user != null )
    		{
    			user = userService.findUserByUserId( session.user.userId );
    		}
    	}
    
    	def activities = activityStreamService.getRecentFriendActivitiesForUser( user );
    	
    	[user:user, activities:activities];
    }
}
