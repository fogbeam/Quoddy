package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

import grails.plugin.springsecurity.annotation.Secured

@Mixin(SidebarPopulatorMixin)
class UserListController
{
	def userService;
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
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )

		Map model = [:];
		Map sidebarCollections = populateSidebarCollections( this, currentUser );
		model.putAll( sidebarCollections );
	  
	  	return model;	  
	}	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def display()
	{
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			// log.debug( "Doing display with params: ${params}");
			def activities = new ArrayList<ActivityStreamItem>();
								
			
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
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardOne()
    {
        [:];
    }

        
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardTwo()
    {
        log.debug( "transitioning to stage2");
        
        UserList listToCreate = new UserList();
        listToCreate.name = params.listName;
        listToCreate.description = params.listDescription;
        
        // get current User from SecurityContextHolder
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
        listToCreate.owner = currentUser;
        
        session.listToCreate = listToCreate;
         
        // TODO: lookup all users who aren't already part of this UserList
        // and return them
        List<User> availableUsers = new ArrayList<User>();
        def queryResults = User.findAll();
        if( queryResults )
        {
            availableUsers.addAll( queryResults );
        }
        
        [availableUsers:availableUsers]
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardFinish()
    {
        log.trace( "create using params: ${params}");
        UserList listToCreate = session.listToCreate;
    
        /* deal with usersToAdd here */
        log.debug( "dealing with users to add" );
        def usersToAdd = params.list( 'usersToAdd');
        for( String userToAdd : usersToAdd )
        {
            log.debug( "adding user: ${userToAdd}" );
            User addMeUser = User.findById( userToAdd );
            listToCreate.addToMembers( addMeUser );
        }
    
        if( !listToCreate.save(flush:true) )
        {
            log.error( "Saving UserList FAILED" );
            listToCreate.errors.allErrors.each { log.debug( it ) };
        }

        redirect(controller:"userList", action:"index");
    }
    
        
    /* TODO: replace this */
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def editWizardFlow()
	{
		start {
			action {
				def listId = params.listId;
				log.debug( "Editing UserList with id: ${listId}");
				UserList listToEdit = null;
				listToEdit = UserList.findById( listId );
		
				[listToEdit:listToEdit];	
			}
			on("success").to("editWizardOne")
		  }
		
		  /* a view state to bring up our GSP */
		 editWizardOne {
			 on("stage2") {
			 	
				 log.debug( "transitioning to stage2");
				
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
			 }.to("editWizardTwo");
		 }
		 
		 editWizardTwo {
			 on("finishWizard"){
			 	log.debug( "finishing Wizard" );
				[];
			 }.to("finish")
		 }
		 
		 /* an action state to do the final save/update on the object */
		 finish {
			 action {
				 log.debug( "update using params: ${params}");
				 def listId = params.listId;
				 UserList listToEdit = flow.listToEdit;
			 
				 /* deal with usersToRemove and usersToAdd here */
				 log.debug( "dealing with usersToRemove");
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
				 
				 
				 if( !listToEdit.save(flush:true) )
				 {
					 log.error( "Saving UserList FAILED");
					 listToEdit.errors.allErrors.each { log.debug( it ) };
				 }
				 
				 log.debug( "dealing with users to add" );
				 def usersToAdd = params.list( 'usersToAdd');
				 for( String userToAdd : usersToAdd )
				 {
					 log.debug( "adding user: ${userToAdd}" );
					 User addMeUser = User.findById( userToAdd );
					 listToEdit.addToMembers( addMeUser );
				 }
			 
				 if( !listToEdit.save(flush:true) )
				 {
					 log.error( "Saving UserList FAILED");
					 listToEdit.errors.allErrors.each { log.error( it ) };
				 }
			 }
			 on("success").to("exitWizard");
		}
		 
		exitWizard {
			 redirect(controller:"userList", action:"index");
		}
	}	
}
