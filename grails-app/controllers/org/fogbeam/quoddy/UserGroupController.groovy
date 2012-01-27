package org.fogbeam.quoddy

class UserGroupController
{
	def activityStreamService;
	def userService;
	def userStreamService;
	def userListService;
	def userGroupService;
	
	def index =
	{
		User user = null;
		
		def userOwnedGroups = new ArrayList<UserGroup>();
		def userMembershipGroups = new ArrayList<UserGroup>();
		
		def systemDefinedStreams = new ArrayList<UserStream>();
		def userDefinedStreams = new ArrayList<UserStream>();
		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		
		if( session.user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
		
		
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
		
			def tempSysStreams = userStreamService.getSystemDefinedStreamsForUser( user );
			systemDefinedStreams.addAll( tempSysStreams );
			def tempUserStreams = userStreamService.getUserDefinedStreamsForUser( user );
			userDefinedStreams.addAll( tempUserStreams );
			
			def tempUserLists = userListService.getListsForUser( user );
			userLists.addAll( tempUserLists );
			
			def tempUserGroups = userGroupService.getGroupsOwnedByUser( user );
			userGroups.addAll( tempUserGroups );
			
				
			[user:user, 
			  userOwnedGroups:userOwnedGroups, 
			  userMembershipGroups:userMembershipGroups,
			  sysDefinedStreams:systemDefinedStreams, 
			  userDefinedStreams:userDefinedStreams,
			  userLists:userLists,
			  userGroups:userGroups ];
	  
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
		
		// TODO: implement this...
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
		
		// println "Doing display with params: ${params}";
		def activities = new ArrayList<Activity>();
		
		UserGroup group = UserGroup.findById( params.groupId );
		
		activities = userGroupService.getRecentActivitiesForGroup( group, 25 ); 
		
		
		[group:group, activities:activities];	
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
			activity.userActor = user;
			activity.published = new Date(); // set published to "now"
			activity.targetUuid = group.uuid;
			activityStreamService.saveActivity( activity );
			
			// Map msg = new HashMap();
			// msg.creator = activity.userActor.userId;
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
