package org.fogbeam.quoddy

class HomeController {

	def userService;
	def activityStreamService;
	def userStreamService;
	def userListService;
	def userGroupService;
	
    def index = {
    		
    	def userId = params.userId;
    	def user = null;
		def activities = new ArrayList<Activity>();
		def systemDefinedStreams = new ArrayList<UserStream>();
		def userDefinedStreams = new ArrayList<UserStream>(); 
		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		
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
				
				def tempSysStreams = userStreamService.getSystemDefinedStreamsForUser( user );
				systemDefinedStreams.addAll( tempSysStreams );
				def tempUserStreams = userStreamService.getUserDefinedStreamsForUser( user );
				userDefinedStreams.addAll( tempUserStreams );
				
				def tempUserLists = userListService.getListsForUser( user );
				userLists.addAll( tempUserLists );
				
				def tempUserGroups = userGroupService.getAllGroupsForUser( user );
				userGroups.addAll( tempUserGroups );
				
				
			}
    	}
    
    	[user:user, 
		  activities:activities, 
		  sysDefinedStreams:systemDefinedStreams, 
		  userDefinedStreams:userDefinedStreams,
		  userLists:userLists,
		  userGroups:userGroups];
    }
}
