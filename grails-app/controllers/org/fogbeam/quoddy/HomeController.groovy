package org.fogbeam.quoddy

class HomeController {

	def userService;
	def activityStreamService;
	
    def index = {
    		
    	def userId = params.userId;
    	def user = null;
		def activities = new ArrayList<Activity>();
		if( userId != null )
    	{
    		user = userService.findUserByUserId( userId );
    	}
    	else
    	{
    		if( session.user != null )
    		{
    			user = userService.findUserByUserId( session.user.userId );
				// activities = activityStreamService.getRecentFriendActivitiesForUser( user );
				activities = activityStreamService.getRecentActivitiesForUser( user, 25 );
			}
    	}
    
    	[user:user, activities:activities];
    }
}
