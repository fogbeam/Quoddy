package org.fogbeam.quoddy

class UserListController
{
	def userService;
	def userStreamService;
	def userListService;
	def userGroupService;
	
	def index =
	{
		User user = null;
		
		def systemDefinedStreams = new ArrayList<UserStream>();
		def userDefinedStreams = new ArrayList<UserStream>(); 
		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		
		if( session.user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
		
		
			def tempSysStreams = userStreamService.getSystemDefinedStreamsForUser( user );
			systemDefinedStreams.addAll( tempSysStreams );
			def tempUserStreams = userStreamService.getUserDefinedStreamsForUser( user );
			userDefinedStreams.addAll( tempUserStreams );
				
			def tempUserLists = userListService.getListsForUser( user );
			userLists.addAll( tempUserLists );
				
			def tempUserGroups = userGroupService.getGroupsOwnedByUser( user );
			userGroups.addAll( tempUserGroups );
			
			[user:user, 
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
			UserList listToCreate = new UserList();
		
			listToCreate.name = params.listName;
			listToCreate.description = params.listDescription;
			listToCreate.owner = user;
			
			if( !listToCreate.save() )
			{
				println( "Saving UserList FAILED");
				listToCreate.errors.allErrors.each { println it };
			}
		
			redirect(controller:"userList", action:"index");
		}
		else
		{
			// not logged in, deal with this...	
		}
	}
	
	def edit =
	{
		def listId = params.id;
		println "Editing UserList with id: ${listId}";
		UserList listToEdit = null;
		
		listToEdit = UserList.findById( listId );
		
		
		[listToEdit:listToEdit];	
	}
	
	def update = 
	{
		println "update using params: ${params}"
		def listId = params.listId;
		UserList listToEdit = null;
		
		listToEdit = UserList.findById( listId );
		
		listToEdit.name = params.listName;
		listToEdit.description = params.listDescription;
		
		if( !listToEdit.save() )
		{
			println( "Saving UserList FAILED");
			listToEdit.errors.allErrors.each { println it };
		}
		
		
		
		redirect(controller:"userList", action:"index");
	}

	def editWizardFlow =
	{
		start {
			action {
				def listId = params.listId;
				println "Editing UserList with id: ${listId}";
				UserList listToEdit = null;
				listToEdit = UserList.findById( listId );
		
				[listToEdit:listToEdit];	
			}
			on("success").to("editWizardOne")
		  }
		
		  /* a view state to bring up our GSP */
		 editWizardOne {
			 on("stage2") {
			 	
				 println "transitioning to stage2";
				
				 UserList listToEdit = flow.listToEdit;
				 listToEdit.name = params.listName;
				 listToEdit.description = params.listDescription;
				
				 // TODO: lookup all users who aren't already part of this UserList
				 // and return them
				 List<User> availableUsers = new ArrayList<User>();
				 def queryResults = userListService.getEligibleUsersForList( listToEdit );
				 if( queryResults )
				 {
				 	availableUsers.addAll( queryResults );
				 }
				 
				 [availableUsers:availableUsers]
			 }.to("editWizardTwo")
		 }
		 
		 editWizardTwo {
			 on("finishWizard"){
			 	println "finishing Wizard";
			 }.to("finish")
		 }
		 
		 /* an action state to do the final save/update on the object */
		 finish {
			 action {
				 println "update using params: ${params}"
				 def listId = params.listId;
				 UserList listToEdit = flow.listToEdit;
			 
				 if( !listToEdit.save() )
				 {
					 println( "Saving UserList FAILED");
					 listToEdit.errors.allErrors.each { println it };
				 }
			 }
			 
			 redirect(controller:"userList", action:"index");
			 	 
		}
	}	
	
	
}
