package org.fogbeam.quoddy.controller.mixins

import org.fogbeam.quoddy.EventSubscription
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.UserGroup
import org.fogbeam.quoddy.UserList
import org.fogbeam.quoddy.UserStream

class SidebarPopulatorMixin
{
	Map populateSidebarCollections( def controller, User user )
	{
		def systemDefinedStreams = new ArrayList<UserStream>();
		def userDefinedStreams = new ArrayList<UserStream>();
		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		def eventSubscriptions = new ArrayList<EventSubscription>();
		
		def tempSysStreams = controller.userStreamService.getSystemDefinedStreamsForUser( user );
		systemDefinedStreams.addAll( tempSysStreams );
		def tempUserStreams = controller.userStreamService.getUserDefinedStreamsForUser( user );
		userDefinedStreams.addAll( tempUserStreams );
	
		def tempUserLists = controller.userListService.getListsForUser( user );
		userLists.addAll( tempUserLists );
	
		def tempUserGroups = controller.userGroupService.getAllGroupsForUser( user );
		userGroups.addAll( tempUserGroups );
		
		def tempEventSubscriptions = controller.eventSubscriptionService.getAllSubscriptionsForUser( user );
		eventSubscriptions.addAll( tempEventSubscriptions );
		
		[sysDefinedStreams:systemDefinedStreams,
			userDefinedStreams:userDefinedStreams,
			userLists:userLists,
			userGroups:userGroups,
			eventSubscriptions:eventSubscriptions];
		
	}
}