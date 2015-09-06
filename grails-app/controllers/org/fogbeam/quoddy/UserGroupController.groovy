package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.stream.constants.EventTypes


@Mixin(SidebarPopulatorMixin)
class UserGroupController
{
	def eventStreamService;
	def userService;
	def userStreamDefinitionService;
	def userListService;
	def userGroupService;
	def businessEventSubscriptionService;
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
	
	def index =
	{
		User user = null;
		
		def userOwnedGroups = new ArrayList<UserGroup>();
		def userMembershipGroups = new ArrayList<UserGroup>();
		
		
		if( session.user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
						
			Map model = [:];
			if( user ) 
			{
				def tempUserOwnedGroups = userGroupService.getGroupsOwnedByUser( user );
				if( tempUserOwnedGroups )
				{
					userOwnedGroups.addAll( tempUserOwnedGroups );
				}
				
				def tempUserMembershipGroups = userGroupService.getGroupsWhereUserIsMember(user);
				if( tempUserMembershipGroups )
				{
					userMembershipGroups.addAll( tempUserMembershipGroups );
				}
				
				model.putAll( [user:user, 
								userOwnedGroups:userOwnedGroups, 
								userMembershipGroups:userMembershipGroups] );
							
				Map sidebarCollections = populateSidebarCollections( this, user );
				model.putAll( sidebarCollections );
			}
			
			return model;
		}
		else
		{
			// TODO: not logged in, deal with this...	
		}
	}
	
	def create = 
	{
		[];	
	}
	
	def save = 
	{
		
		println "save using params: ${params}"
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			UserGroup groupToCreate = new UserGroup();
		
			groupToCreate.name = params.groupName;
			groupToCreate.description = params.groupDescription;
			groupToCreate.owner = user;
			
			if( ! groupToCreate.save() )
			{
				println( "Saving UserGroup FAILED");
				groupToCreate.errors.allErrors.each { println it };
			}
		
			redirect(controller:"userGroup", action:"index");
		}
		else
		{
			// not logged in, deal with this...	
		}
	}
	
	def edit =
	{
		def groupId = params.id;
		println "Editing UserGroup with id: ${groupId}";
		UserGroup groupToEdit = null;
		
		groupToEdit = UserGroup.findById( groupId );
		
		
		[groupToEdit:groupToEdit];	
	}
	
	def update = 
	{
		println "update using params: ${params}"
		def groupId = params.groupId;
		UserGroup groupToEdit = null;
		
		groupToEdit = UserGroup.findById( groupId );
		
		groupToEdit.name = params.groupName;
		groupToEdit.description = params.groupDescription;
		if( !groupToEdit.requireJoinConfirmation )
		{
			groupToEdit.requireJoinConfirmation = false;
		}
		if( ! groupToEdit.save(flush:true) )
		{
			println( "Saving UserGroup FAILED");
			groupToEdit.errors.allErrors.each { println it };
		}
		
		// TODO: deal with requireJoinConfirmation
		
		
		redirect(controller:"userGroup", action:"index");
	}

	def display = 
	{
		
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			// println "Doing display with params: ${params}";
			
			// def items = new ArrayList<StreamItemBase>();
			List<ActivityStreamItem> activities = new ArrayList<ActivityStreamItem>();
			
			Map model = [:];
			if( user )
			{			
				UserGroup group = UserGroup.findById( params.groupId );
			
				// check that this group is not one of the ones that the user either
				// owns or is a member of
				List<UserGroup> userGroups = userGroupService.getAllGroupsForUser( user );
				
				boolean userIsGroupMember = false;
				userGroups.each {
					if( it.id == group.id ){
						userIsGroupMember = true;
						return;
					}
				}
			
				activities = userGroupService.getRecentActivitiesForGroup( group, 25 ); 
				// items = userGroupService.getRecentEventsForGroup( group, 25 );
				
				model.putAll( [ group:group,
								user: user,
								userIsGroupMember:userIsGroupMember,
								activities:activities] );
				
				Map sidebarCollections = populateSidebarCollections( this, user );
				model.putAll( sidebarCollections );
			}
			
			return model;	
		}
		else
		{
			redirect( controller:"home", action:"index");	
		}
	}	

	def joinGroup =
	{
		// TODO: find group, see if joinConfirmation is required, 
		// and add user to group OR add pending group request
		// for the group owner / admin
		
		// TODO: create group Membership
		String groupId = params.groupId;
		String userId = session.user.id;
		
		println "doing joinGroup with groupId = ${groupId} and userId = ${userId}";
		User user = User.findById( userId );
		UserGroup group = UserGroup.findById( groupId );
		
		group.addToGroupMembers( user );
		
		redirect( controller:"userGroup", action:"display", params:['groupId':groupId]);	
	}
	
	
	def list =
	{
		List<UserGroup> allGroups = userGroupService.getAllGroups();
		
		[allGroups: allGroups];	
	}

	def postToGroup =
	{
		println "Posting to group: ${params.groupId}, with statusText: ${params.statusText}";		
		def groupId = params.groupId;
		
		if( session.user )
		{
			
			println "logged in; so proceeding...";
			
			// get our user
			User user = userService.findUserByUserId( session.user.userId );
			
			// get our UserGroup
			UserGroup group = userGroupService.findByGroupId( Integer.parseInt( groupId ) ); 
			
			
			/* test to see if the user is a member of the group before allowing them to post */
			// check that this group is not one of the ones that the user either
			// owns or is a member of
			
			// TODO: move this blurb of code into UserGroupService or somewhere, with a signature
			// like boolean isUserInGroup( User user, UserGroup group )
			List<UserGroup> userGroups = userGroupService.getAllGroupsForUser( user );
			
			boolean userIsGroupMember = false;
			userGroups.each {
				if( it.id == group.id ){
					userIsGroupMember = true;
					return;
				}
			}
			
			if( !userIsGroupMember )
			{
				flash.message = "You can only post to a group if you are a member of the group";
			}
			else
			{
				println "constructing our new StatusUpdate object...";
				// construct a status object
				println "statusText: ${params.statusText}";
				StatusUpdate newStatus = new StatusUpdate( text: params.statusText, creator: user );
				newStatus.effectiveDate = new Date();
				newStatus.targetUuid = group.uuid; // NOTE: can we take 'targetUuid' out of StatusUpdate??
				newStatus.name = "321BCA";
				
				
				/* TODO: add call to Stanbol to get our enhancement JSON */
				newStatus.enhancementJSON = "";
				
				if( !newStatus.save() )
				{
					println "Save StatusUpdate FAILED!";
					newStatus.errors.allErrors.each { println it };	
				}
				
				ActivityStreamItem activity = new ActivityStreamItem(content:newStatus.text);
				activity.title = "Internal Activity";
				activity.url = new URL( "http://www.example.com" );
				activity.verb = "quoddy_group_ytstatus_update";
				activity.actorObjectType = "User";
				activity.actorUuid = user.uuid;
				activity.targetObjectType = "UserGroup";
				
				activity.owner = user;
				activity.published = new Date(); // set published to "now"
				activity.targetUuid = group.uuid;
				activity.streamObject = newStatus;
				activity.objectClass = EventTypes.STATUS_UPDATE.name;
							
				// NOTE: we added "name" to EventBase, but how is it really going
				// to be used?  Do we *really* need this??
				activity.name = activity.title;
				activity.published = activity.published;
				
				eventStreamService.saveActivity( activity );
				
				
				// Map msg = new HashMap();
				// msg.creator = activity.owner.userId;
				// msg.text = newStatus.text;
				// msg.published = activity.published;
				// msg.originTime = activity.dateCreated.time;
				// msg.targetUuid = activity.targetUuid;
				
				// println "sending message to JMS";
				// jmsService.send( queue: 'uitestActivityQueue', msg, 'standard', null );
			}
		
		}
		else
		{
				
		}
		
		redirect( controller:"userGroup", action:"display", params:['groupId':groupId]);
	}		
}