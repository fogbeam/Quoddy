package org.fogbeam.quoddy

// import org.apache.shiro.SecurityUtils
import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

import grails.plugin.springsecurity.annotation.Secured

@Mixin(SidebarPopulatorMixin)
class HomeController 
{

	def userService;
	def eventStreamService;
	def userStreamDefinitionService;
	def userListService;
	def userGroupService;
	def businessEventSubscriptionService;
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
		
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def index()
    {
    		
    	def userId = params.userId;
    	User user = null;
		List<ActivityStreamItem> activities = new ArrayList<ActivityStreamItem>();
		
        
        
        // Note: How do we handle the case where the currentUser is somebody other than
        // the user identified by userId if a userId is supplied? We need to check authorization
        // settings for the "target" user in this case to see if the currentUser is allowed

        
		if( userId != null )
    	{
			log.debug( "getting User by userId: ${userId}");
    		user = userService.findUserByUserId( userId );
    	}
    	else
    	{            
			user = userService.getLoggedInUser();
    	}
		
        log.trace( "got currentUser as ${user}" );
        
		Map model = [:];
		if( user )
		{
			// TODO: this should take the selected UserStream into account when
			// determining what activities to include in the activities list
			UserStreamDefinition selectedStream = null;
			if( params.streamId )
			{
				Long streamId = Long.valueOf( params.streamId );
				selectedStream = userStreamDefinitionService.findStreamById( streamId );
			}
			else 
			{
				selectedStream = userStreamDefinitionService.getStreamForUser( user, UserStreamDefinition.DEFAULT_STREAM );	
			}
			
			
			activities = eventStreamService.getRecentActivitiesForUser( user, 25, selectedStream );
			model.putAll( [user:user, activities:activities, streamId:params.streamId] );
			
			Map sidebarCollections = populateSidebarCollections( this, user );
			model.putAll( sidebarCollections );
			
		}	
		
		return model;
    }
}
