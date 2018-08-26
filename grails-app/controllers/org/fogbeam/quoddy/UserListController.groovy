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
		User currentUser = userService.getLoggedInUser();
		
		Map model = [:];
		Map sidebarCollections = populateSidebarCollections( this, currentUser );
		model.putAll( sidebarCollections );
	  
	  	return model;	  
	}	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def display()
	{
		User currentUser = userService.getLoggedInUser();
		        
        // log.debug( "Doing display with params: ${params}");
        def activities = new ArrayList<ActivityStreamItem>();                    
        
        UserList list = UserList.findById( params.listId );
        
        Map model = [:];
        activities = userListService.getRecentActivitiesForList( list, 25 );
        model.putAll( [ activities:activities] );
        
        Map sidebarCollections = populateSidebarCollections( this, currentUser );
        model.putAll( sidebarCollections );
        
        return model;
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
        
		User currentUser = userService.getLoggedInUser();
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
        log.debug( "UserList.createWizard.finish" );
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
            listToCreate.errors.allErrors.each { log.debug( it.toString() ) };
        }

        redirect(controller:"userList", action:"index");
    }
    
    
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardOne()
    {
        log.debug( "UserList.editWizard.stage1" );
        
        def listId = params.listId;
        
        log.info( "Editing UserList with id: ${listId}");
        
        UserList listToEdit = UserList.findById( listId, [fetch:[members:"eager"]] );

        log.debug( "found listToEdit: ${listToEdit}" );
        
        // detach from the Hibernate session until the end of the Wizard
        listToEdit.discard();
        session.listToEdit = listToEdit;
        
        [listToEdit:listToEdit];
    }

    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardTwo()
    {
        log.debug( "UserList.editWizard.stage2" );
          
        UserList listToEdit = session.listToEdit;
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
           
        [listToEdit:listToEdit, availableUsers:availableUsers]   
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardFinish() 
    {
        log.debug( "UserList.editWizard.finish" );
        log.debug( "update using params: ${params}");
        def listId = params.listId;
        UserList listToEdit = session.listToEdit;
    
        if( !listToEdit.isAttached())
        {
            listToEdit.attach();
        }
    
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
            listToEdit.errors.allErrors.each { log.error( it.toString() ) };
        }
        
        redirect(controller:"userList", action:"index");
    }	
}
