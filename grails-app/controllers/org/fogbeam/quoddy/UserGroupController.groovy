package org.fogbeam.quoddy

class UserGroupController
{
	def userService;
	def userStreamService;
	def userListService;
	def userGroupService;
	
	def index =
	{
		User user = null;
		
		def userGroups = new ArrayList<UserGroup>();
		
		if( session.user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
		
		
			def tempUserGroups = userGroupService.getGroupsForUser( user );
			userGroups.addAll( tempUserGroups );
			
			[user:user, userGroups:userGroups ];
	  
			
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
			groupToCreate.owner = user;
			
			groupToCreate.save();
		
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
		groupToEdit.save();
		
		redirect(controller:"userGroup", action:"index");
	}
}
