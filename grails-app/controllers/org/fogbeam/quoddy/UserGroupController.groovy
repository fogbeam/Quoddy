package org.fogbeam.quoddy

import java.util.List;

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin


@Mixin(SidebarPopulatorMixin)
class UserGroupController
{
	def eventStreamService;
	def userService;
	def userStreamService;
	def userListService;
	def userGroupService;
	def eventSubscriptionService;
	
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
			
			// def activities = new ArrayList<Activity>();
			def events = new ArrayList<EventBase>();
			
			
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
			
				// activities = userGroupService.getRecentActivitiesForGroup( group, 25 ); 
				events = userGroupService.getRecentEventsForGroup( group, 25 );
				
				model.putAll( [ group:group,
								user: user,
								userIsGroupMember:userIsGroupMember,
								activities:events] );
				
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
			
			println "constructing our new StatusUpdate object...";
			// construct a status object
			println "statusText: ${params.statusText}";
			StatusUpdate newStatus = new StatusUpdate( text: params.statusText, creator: user );
			
			if( !newStatus.save() )
			{
				println "Save StatusUpdate FAILED!";
					
			}
			
			Activity activity = new Activity(content:newStatus.text);
			activity.title = "Internal Activity";
			activity.url = new URL( "http://www.example.com" );
			activity.verb = "status_update";
			activity.owner = user;
			activity.published = new Date(); // set published to "now"
			activity.targetUuid = group.uuid;
						
			// NOTE: we added "name" to EventBase, but how is it really going
			// to be used?  Do we *really* need this??
			activity.name = activity.title;
			activity.effectiveDate = activity.published;
			
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
		else
		{
				
		}
		redirect( controller:"userGroup", action:"display", params:['groupId':groupId]);
	}
				
}
