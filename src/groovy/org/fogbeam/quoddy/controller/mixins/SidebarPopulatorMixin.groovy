package org.fogbeam.quoddy.controller.mixins

import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.UserGroup
import org.fogbeam.quoddy.UserList
import org.fogbeam.quoddy.UserStreamDefinition
import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription
import org.fogbeam.quoddy.subscription.BusinessEventSubscription
import org.fogbeam.quoddy.subscription.CalendarFeedSubscription
import org.fogbeam.quoddy.subscription.RssFeedSubscription

class SidebarPopulatorMixin
{
	Map populateSidebarCollections( def controller, User user )
	{
		def systemDefinedStreams = new ArrayList<UserStreamDefinition>();
		def userDefinedStreams = new ArrayList<UserStreamDefinition>();
		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		def businessEventSubscriptions = new ArrayList<BusinessEventSubscription>();
		def calendarFeedSubscriptions = new ArrayList<CalendarFeedSubscription>();
		def activitiUserTaskSubscriptions = new ArrayList<ActivitiUserTaskSubscription>();
		def rssFeedSubscriptions = new ArrayList<RssFeedSubscription>();
		
		
		def tempSysStreams = controller.userStreamDefinitionService.getSystemDefinedStreamsForUser( user );
		systemDefinedStreams.addAll( tempSysStreams );
		def tempUserStreams = controller.userStreamDefinitionService.getUserDefinedStreamsForUser( user );
		userDefinedStreams.addAll( tempUserStreams );
	
		def tempUserLists = controller.userListService.getListsForUser( user );
		userLists.addAll( tempUserLists );
	
		def tempUserGroups = controller.userGroupService.getAllGroupsForUser( user );
		userGroups.addAll( tempUserGroups );
		
		def tempBusinessEventSubscriptions = controller.businessEventSubscriptionService.getAllSubscriptionsForUser( user );
		businessEventSubscriptions.addAll( tempBusinessEventSubscriptions );
		
		def tempCalendarFeedSubscriptions = controller.calendarFeedSubscriptionService.getAllSubscriptionsForUser( user );
		calendarFeedSubscriptions.addAll( tempCalendarFeedSubscriptions );
		
		def tempActivitiUserTaskSubscriptions = controller.activitiUserTaskSubscriptionService.getAllSubscriptionsForUser( user );
		activitiUserTaskSubscriptions.addAll( tempActivitiUserTaskSubscriptions );
		
		def tempRssFeedSubscriptions = controller.rssFeedSubscriptionService.getAllSubscriptionsForUser( user );
		rssFeedSubscriptions.addAll( tempRssFeedSubscriptions );
		
		[sysDefinedStreams:systemDefinedStreams,
			userDefinedStreams:userDefinedStreams,
			userLists:userLists,
			userGroups:userGroups,
			businessEventSubscriptions:businessEventSubscriptions,
			calendarFeedSubscriptions:calendarFeedSubscriptions,
			activitiUserTaskSubscriptions:activitiUserTaskSubscriptions,
			rssFeedSubscriptions:rssFeedSubscriptions];
		
	}
}