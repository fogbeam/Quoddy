package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin

@Mixin(SidebarPopulatorMixin)
class UserListController
{
	def userService;
	def userStreamService;
	def userListService;
	def userGroupService;
	
	def index =
	{
		User user = null;
		
		if( session.user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
			
			Map model = [:];
			if( user )
			{
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
	
	def display = 
	{
		
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			// println "Doing display with params: ${params}";
			def activities = new ArrayList<Activity>();
								
			
			UserList list = UserList.findById( params.listId );
			
			Map model = [:];
			if( user )
			{
				activities = userListService.getRecentActivitiesForList( list, 25 );
				model.putAll( [ activities:activities] );
				
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
	
	def createWizardFlow =
	{
		start {
			action {
				[];
			}
			on("success").to("createWizardOne")
		  }
		
		/* a view state to bring up our GSP */
		createWizardOne {
			on("stage2") {
				
				println "transitioning to stage2";
			   
				UserList listToCreate = new UserList();
				listToCreate.name = params.listName;
				listToCreate.description = params.listDescription;
			   
				def user = userService.findUserByUserId( session.user.userId );
				listToCreate.owner = user;
				
				flow.listToCreate = listToCreate;
				
				// TODO: lookup all users who aren't already part of this UserList
				// and return them
				List<User> availableUsers = new ArrayList<User>();
				def queryResults = User.findAll();
				if( queryResults )
				{
					availableUsers.addAll( queryResults );
				}
				
				[availableUsers:availableUsers]
			}.to("createWizardTwo")
		}
		
		createWizardTwo {
			on("finishWizard"){
				println "finishing Wizard";
			   [];
			}.to("finish")
		}
		
		/* an action state to do the final save/update on the object */
		finish {
			action {
				println "create using params: ${params}"
				UserList listToCreate = flow.listToCreate;
			
				/* deal with usersToAdd here */
				println( "dealing with users to add" );
				def usersToAdd = params.list( 'usersToAdd');
				for( String userToAdd : usersToAdd )
				{
					log.debug( "adding user: ${userToAdd}" );
					User addMeUser = User.findById( userToAdd );
					listToCreate.addToMembers( addMeUser );
				}
			
				if( !listToCreate.save() )
				{
					println( "Saving UserList FAILED");
					listToCreate.errors.allErrors.each { println it };
				}
			}
			on("success").to("exitWizard");
	   }
		
	   exitWizard {
			redirect(controller:"userList", action:"index");
	   }
		
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
				[];
			 }.to("finish")
		 }
		 
		 /* an action state to do the final save/update on the object */
		 finish {
			 action {
				 println "update using params: ${params}"
				 def listId = params.listId;
				 UserList listToEdit = flow.listToEdit;
			 
				 /* deal with usersToRemove and usersToAdd here */
				 println "dealing with usersToRemove";
				 def usersToRemove = params.list('usersToRemove');
				 for( String userToRemove : usersToRemove )
				 {
					 
					 log.debug( "removing user: ${userToRemove}" );
					 
					 User removeMeUser = listToEdit.members.find { it.id == Integer.parseInt(userToRemove) }
					 if( removeMeUser )
					 {
						 log.debug( "calling removeFromMembers using user: ${removeMeUser}");
						 listToEdit.removeFromMembers( removeMeUser );
					 }
					 else
					 {
						 log.warn( "problem finding user instance for ${userToRemove}" );
					 }
				 }
				 
				 
				 if( !listToEdit.save() )
				 {
					 println( "Saving UserList FAILED");
					 listToEdit.errors.allErrors.each { println it };
				 }
				 
				 println( "dealing with users to add" );
				 def usersToAdd = params.list( 'usersToAdd');
				 for( String userToAdd : usersToAdd )
				 {
					 log.debug( "adding user: ${userToAdd}" );
					 User addMeUser = User.findById( userToAdd );
					 listToEdit.addToMembers( addMeUser );
				 }
			 
				 if( !listToEdit.save() )
				 {
					 println( "Saving UserList FAILED");
					 listToEdit.errors.allErrors.each { println it };
				 }
			 }
			 on("success").to("exitWizard");
		}
		 
		exitWizard {
			 redirect(controller:"userList", action:"index");
		}
	}	
}
