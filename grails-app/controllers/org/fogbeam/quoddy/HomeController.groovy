package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin

@Mixin(SidebarPopulatorMixin)
class HomeController {

	def userService;
	def eventStreamService;
	def userStreamService;
	def userListService;
	def userGroupService;
	def eventSubscriptionService;
	
    def index = {
    		
    	def userId = params.userId;
    	User user = null;
		def activities = new ArrayList<Activity>();
		
		if( userId != null )
    	{
			println "getting User by userId: ${userId}";
    		user = userService.findUserByUserId( userId );
    	}
    	else
    	{
			println "Looking up User in session";
			
    		if( session.user != null )
    		{
				println "Found User in Session";
    			user = userService.findUserByUserId( session.user.userId );
    		}
			else
			{
				println "No user in Session";
			}
    	}
		
		Map model = [:];
		if( user )
		{
			// TODO: this should take the selected UserStream into account when
			// determining what activities to include in the activities list
			UserStream selectedStream = null;
			if( params.streamId )
			{
				Long streamId = Long.valueOf( params.streamId );
				selectedStream = userStreamService.findStreamById( streamId );
			}
			else 
			{
				selectedStream = userStreamService.getStreamForUser( user, UserStream.DEFAULT_STREAM );	
			}
			
			
			activities = eventStreamService.getRecentActivitiesForUser( user, 25, selectedStream );
			model.putAll( [user:user, activities:activities] );
			
			Map sidebarCollections = populateSidebarCollections( this, user );
			model.putAll( sidebarCollections );
			
		}	
		
		return model;
    }
}
