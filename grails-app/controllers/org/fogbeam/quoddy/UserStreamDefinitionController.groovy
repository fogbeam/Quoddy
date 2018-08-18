package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.stream.EventType
import org.fogbeam.quoddy.stream.constants.EventTypeScopes
import org.fogbeam.quoddy.subscription.BaseSubscription
import org.fogbeam.quoddy.subscription.BusinessEventSubscription
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

import grails.plugin.springsecurity.annotation.Secured

@Mixin(SidebarPopulatorMixin)
class UserStreamDefinitionController
{
	def userService;
	def eventStreamService;
	def userStreamDefinitionService;
	def userListService;
	def userGroupService;
	def businessEventSubscriptionService;
	def eventTypeService;
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
	def eventSubscriptionService;
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{	
		def systemDefinedStreams = new ArrayList<UserStreamDefinition>();
		def userDefinedStreams = new ArrayList<UserStreamDefinition>();

		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		
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
    def createWizardOne()
    {
        log.debug( "UserStreamDefinition.createWizard.stage1");
        // just render the initial view
        [:];    
    }
    
    // receive parameters from stage1 and begin creating the UserStreamDefinition
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardTwo()
    {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = null;
        currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
        
        log.debug( "UserStreamDefinition.createWizard.stage2");
       
        UserStreamDefinition streamToCreate = new UserStreamDefinition();
        streamToCreate.name = params.streamName;
        streamToCreate.description = params.streamDescription;
        
        streamToCreate.owner = currentUser;
        streamToCreate.definedBy = UserStreamDefinition.DEFINED_USER;
        session.streamToCreate = streamToCreate;        
            
        Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
        [eventTypes:eventTypes];        
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardThree()
    {
        log.debug( "UserStreamDefinition.createWizard.stage3");
        log.trace( "params: ${params}");
        UserStreamDefinition streamToCreate = session.streamToCreate;
        // eventTypes:[219, 218]
        String[] eventTypes = request.getParameterValues( 'eventTypes' );
        
        log.debug( "eventTypes: ${eventTypes}");
        
        for( String eventTypeId : eventTypes )
        {
            EventType eventType = eventTypeService.findEventTypeById( Long.valueOf( eventTypeId ) );
            streamToCreate.addToEventTypesIncluded( eventType );
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
                
        /* load user list */
        log.debug( "getting eligible users: ");
        List<User> eligibleUsers = userService.findEligibleUsersForUser( currentUser );
        log.debug( "found ${eligibleUsers.size()} eligible users\n");
        
        log.debug("returning eligible users:");
        [users:eligibleUsers, selectedUsers:streamToCreate.userUuidsIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardFour()
    {
        log.debug( "UserStreamDefinition.createWizard.stage4" );
        log.trace( "params: ${params}");
        UserStreamDefinition streamToCreate = session.streamToCreate;
        
        String userFilter = params.userFilter;
        
        if( userFilter.equals( "all_users" ))
        {
            streamToCreate.includeAllUsers = true;
            
        }
        else if( userFilter.equals( "no_users" ))
        {
            streamToCreate.includeSelfOnly = true;
            
        }
        else if( userFilter.equals( "select_list" ))
        {
            // save users
            String[] userUuids = request.getParameterValues( 'users' );
            streamToCreate.userUuidsIncluded?.clear();
            
            for( String userUuid : userUuids )
            {
                User userToInclude = userService.findUserByUuid( userUuid );
                if( userToInclude == null ) {
                    log.debug( "Failed to locate User for uuid ${userUuid}");
                    continue;
                }
                
                streamToCreate.addToUserUuidsIncluded( userUuid );
            }

        }
        else
        {
            // this isn't supposed to happen
        }
                        
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = null;
        currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )

        
        /* load userList list */
        List<UserList> userLists = userListService.getListsForUser( currentUser );
        
        [userLists:userLists, selectedUserLists:streamToCreate.userListUuidsIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardFive()
    {        
        // save user lists
        log.debug( "UserStreamDefinition.createWizard.stage5" );
        log.trace( "params: ${params}");
        UserStreamDefinition streamToCreate = session.streamToCreate;
        
        // userLists
        String[] userListUuids = request.getParameterValues( 'userLists' );
        streamToCreate.userListUuidsIncluded?.clear();
        
        for( String userListUuid : userListUuids )
        {
            UserList userListToInclude = userListService.findUserListByUuid( userListUuid );
            if( userListToInclude == null ) {
                log.debug( "Failed to locate UserList for uuid ${userListUuid}");
                continue;
            }
            
            streamToCreate.addToUserListUuidsIncluded( userListUuid );
        }
                                        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = null;
        currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )

        
        /* load group list */
        List<UserGroup> groups = userGroupService.getAllGroupsForUser( currentUser );
        log.debug( "found ${groups.size()} groups");
        [groups:groups, selectedGroups:streamToCreate.userGroupUuidsIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardSix()
    {
        log.debug( "UserStreamDefinition.createWizard.stage6" );
        log.trace( "params: ${params}" );
        UserStreamDefinition streamToCreate = session.streamToCreate;
        // save groups
        
        String[] userGroupUuids = request.getParameterValues( 'userGroups' );
        streamToCreate.userGroupUuidsIncluded?.clear();
        
        for( String userGroupUuid : userGroupUuids )
        {
            UserGroup userGroupToInclude = userGroupService.findUserGroupByUuid( userGroupUuid );
            if( userGroupToInclude == null ) {
                log.debug( "Failed to locate UserGroup for uuid ${userGroupUuid}");
                continue;
            }
            
            streamToCreate.addToUserGroupUuidsIncluded( userGroupUuid );
        }
        
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = null;
        currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )

                                
        /* load subscription list */
        List<BusinessEventSubscription> eventSubscriptions =
            businessEventSubscriptionService.getAllSubscriptionsForUser( currentUser );
        
        [eventSubscriptions:eventSubscriptions, selectedEventSubscriptions:streamToCreate.subscriptionUuidsIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createWizardFinish()
    {
        log.debug( "UserStreamDefinition.createWizard.finish" );
        log.trace( "params: ${params}" );
        UserStreamDefinition streamToCreate = session.streamToCreate;
        
        // save subscriptions
        // subscriptionUuidsIncluded
        String[] eventSubscriptionUuids = request.getParameterValues( 'eventSubscriptions' );
        streamToCreate.subscriptionUuidsIncluded?.clear();
        
        for( String eventSubscriptionUuid : eventSubscriptionUuids )
        {
            BusinessEventSubscription eventSubscriptionToInclude = businessEventSubscriptionService.findByUuid( eventSubscriptionUuid );
            if( eventSubscriptionToInclude == null ) {
                log.debug( "Failed to locate EventSubscription for uuid ${eventSubscriptionUuid}");
                continue;
            }
            
            streamToCreate.addToSubscriptionUuidsIncluded( eventSubscriptionUuid );
        }

        log.debug( "create using params: ${params}");
                
        if( !streamToCreate.save(flush:true) )
        {
            log.debug( "saving UserStream FAILED");
            streamToCreate.errors.allErrors.each { log.debug( it ) };
        }
        
        redirect( controller: "userStreamDefinition", action:"index");
        
    }	
	
    
	/* NOTE:  this has a problem as implemented, if the user moves back and forth in the wizard using the
	 * back button.  Because we "touch" our actual domain object (using calls like addToUserUuidsIncluded)
	 * in intermediate stages, we can get errors like "Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [org.fogbeam.quoddy.UserStreamDefinition#6]"
	 * To prevent this, we probably need to do all of the intermediate stage operations on a detached object which is a clone
	 * of the real object, and then merge it only in the final state.  This also ensures we don't persist a change
	 * that the user intended to abandon (by not finshing the wizard). See Bugzilla bug #125. 
	 */
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardOne()
    {
        log.debug( "UserStreamDefinition.editWizard.stage1" );
        
        def streamId = params.streamId;
        log.debug( "Editing UserStream with id: ${streamId}");
        
        // TODO: use UserStreamDefinitionService instead of using GORM directly here
        
        /*
            userUuidsIncluded:String, 
            userListUuidsIncluded:String, 
            userGroupUuidsIncluded:String,
            subscriptionUuidsIncluded:String,
            eventTypesIncluded:EventType]
        */
        UserStreamDefinition streamToEdit = UserStreamDefinition.findById( streamId, 
            [ fetch:[eventTypesIncluded:"eager", subscriptionUuidsIncluded:"eager",  userGroupUuidsIncluded:"eager", userListUuidsIncluded:"eager",
              userUuidsIncluded:"eager"]]);
        
        // detach the instance from the session and only merge it in the finish state.  This
        // addresses the issue mentionedd in the comment above.
        streamToEdit.discard();
        session.streamToEdit = streamToEdit;
        
        // select only the event types that are "user" scoped
        // as opposed to "subscription" types
        // Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
        Set<EventType> eventTypes = eventTypeService.findEventTypesByScope( EventTypeScopes.EVENT_TYPE_USER.name );
        
        session.userStreamDefinitionEditWizardEventTypes = eventTypes;
        
        log.debug(  "found eventTypes with size = ${eventTypes?.size()}");
        [streamToEdit:streamToEdit, eventTypes:eventTypes];
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardTwo()
    {       
        log.debug( "UserStreamDefinition.editWizard.stage1" );
                   
        UserStreamDefinition streamToEdit = session.streamToEdit;
        streamToEdit.name = params.streamName;
        streamToEdit.description = params.streamDescription;
        
        if( params.includeEverything != null && params.includeEverything.equals( "on") )
        {
            streamToEdit.includeEverything = true;
        }
        else
        {
            streamToEdit.includeEverything = false;
        }
       
        [eventTypes:session.userStreamDefinitionEditWizardEventTypes, selectedEventTypes:streamToEdit.eventTypesIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardThree()
    {
        log.debug( "UserStreamDefinition.editWizard.stage3" );           
        log.trace( "params: ${params}");
        
        UserStreamDefinition streamToEdit = session.streamToEdit;
        
        String[] eventTypes = request.getParameterValues( 'eventTypes' );
        
        streamToEdit.eventTypesIncluded.clear();
        
        for( String eventTypeId : eventTypes )
        {
            EventType eventType = eventTypeService.findEventTypeById( Long.valueOf( eventTypeId ) );
            
            if( eventType == null ) {
                log.debug(  "Failed to locate eventType entry for id: ${eventTypeId}");
                continue;
            }
            streamToEdit.addToEventTypesIncluded( eventType );
        }
        
        /* load user list */
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
        
        List<User> allusers = userService.findAllUsers();
        List<User> eligibleUsers = userService.findEligibleUsersForUser( currentUser );
        
        log.debug( "Found ${eligibleUsers.size()} eligible users\n");
                        
        [streamToEdit:streamToEdit, users:eligibleUsers, selectedUsers:streamToEdit.userUuidsIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardFour()
    {
        log.debug( "UserStreamDefinition.editWizard.stage4" );
        log.trace( "params: ${params}" );
        UserStreamDefinition streamToEdit = session.streamToEdit;
        
        // save users
        String[] userUuids = request.getParameterValues( 'users' );
        streamToEdit.userUuidsIncluded.clear();
        
        for( String userUuid : userUuids )
        {
            User userToInclude = userService.findUserByUuid( userUuid );
            if( userToInclude == null ) 
            {
                log.debug( "Failed to locate User for uuid ${userUuid}");
                continue;
            }
            
            streamToEdit.addToUserUuidsIncluded( userUuid );
        }
        
        String includeSelfParam = params.includeSelf;
        log.debug( "includeSelfParam: \"${includeSelfParam}\"");
        Boolean includeSelf = false;
        if( includeSelfParam != null && includeSelfParam.equalsIgnoreCase("on"))
        {
            log.debug( "updating includeSelf value to TRUE");
            includeSelf = true;
        }
        
        log.debug( "setting includeSelf value to ${includeSelf}");
        streamToEdit.includeSelf = includeSelf;
        
        /* load userList list */
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )

        
        List<UserList> userLists = userListService.getListsForUser( currentUser );
        
        [streamToEdit:streamToEdit, userLists:userLists, selectedUserLists:streamToEdit.userListUuidsIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardFive()
    {
        // save user lists
        log.debug( "UserStreamDefinition.editWizard.stage5" );
        log.trace( "params: ${params}");
        UserStreamDefinition streamToEdit = session.streamToEdit;
        
        // userLists
        String[] userListUuids = request.getParameterValues( 'userLists' );
        streamToEdit.userListUuidsIncluded.clear();
        
        for( String userListUuid : userListUuids )
        {
            UserList userListToInclude = userListService.findUserListByUuid( userListUuid );
            if( userListToInclude == null )
            {
                log.debug( "Failed to locate UserList for uuid ${userListUuid}");
                continue;
            }
            
            streamToEdit.addToUserListUuidsIncluded( userListUuid );
        }
                                        
        /* load group list */
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )

        List<UserGroup> groups = userGroupService.getAllGroupsForUser( currentUser );
        
        log.debug( "found ${groups.size()} groups");
        [streamToEdit: streamToEdit, groups:groups, selectedGroups:streamToEdit.userGroupUuidsIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardSix()
    {
        log.debug( "UserStreamDefinition.editWizard.stage6" );
        log.trace( "params: ${params}" );
        UserStreamDefinition streamToEdit = session.streamToEdit;
        // save groups
        
        String[] userGroupUuids = request.getParameterValues( 'userGroups' );
        streamToEdit.userGroupUuidsIncluded.clear();
        
        for( String userGroupUuid : userGroupUuids )
        {
            UserGroup userGroupToInclude = userGroupService.findUserGroupByUuid( userGroupUuid );
            if( userGroupToInclude == null ) {
                log.debug( "Failed to locate UserGroup for uuid ${userGroupUuid}");
                continue;
            }
            
            streamToEdit.addToUserGroupUuidsIncluded( userGroupUuid );
        }
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
                                
        /* load subscription list */
        List<BaseSubscription> eventSubscriptions =
            eventSubscriptionService.getAllSubscriptionsForUser( currentUser );
        
        log.debug( "Found eventSubscriptions with size: ${eventSubscriptions?.size()}");
            
        [streamToEdit:streamToEdit, eventSubscriptions:eventSubscriptions, selectedEventSubscriptions:streamToEdit.subscriptionUuidsIncluded];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    editWizardFinish()
    {
        log.debug( "UserStreamDefinition.editWizard.finish" );
        log.trace( "params: ${params}" );
        UserStreamDefinition streamToEdit = session.streamToEdit;
        
        // save subscriptions
        // subscriptionUuidsIncluded
        String[] eventSubscriptionUuids = request.getParameterValues( 'eventSubscriptions' );
        streamToEdit.subscriptionUuidsIncluded.clear();
        
        for( String eventSubscriptionUuid : eventSubscriptionUuids )
        {
            log.debug( "looking for subscription with uuid: ${eventSubscriptionUuid}" );
            BaseSubscription eventSubscriptionToInclude = eventSubscriptionService.findByUuid( eventSubscriptionUuid );
            if( eventSubscriptionToInclude == null ) {
                log.debug(  "Failed to locate EventSubscription for uuid ${eventSubscriptionUuid}" );
                continue;
            }
            
            streamToEdit.addToSubscriptionUuidsIncluded( eventSubscriptionUuid );
        }
             
        if( !streamToEdit.isAttached())
        {
            streamToEdit.attach();
        }
        
        log.info( "about to save streamToEdit: ${streamToEdit}");
        
        if( !streamToEdit.save(flush:true) )
        {
            log.error( "Saving UserStream FAILED");
            streamToEdit.errors.allErrors.each { log.error(it) };
        }
        
        redirect(controller:"userStreamDefinition", action:"index");
    }
    	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def edit()
	{
		def streamId = params.id;
		log.debug( "Editing UserStream with id: ${streamId}");
		UserStreamDefinition streamToEdit = null;
		
		streamToEdit = UserStreamDefinition.findById( streamId );
		
		
		[streamToEdit:streamToEdit];	
	}
}