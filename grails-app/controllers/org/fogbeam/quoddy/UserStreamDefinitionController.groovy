package org.fogbeam.quoddy

// import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.stream.EventType
import org.fogbeam.quoddy.stream.constants.EventTypeScopes
import org.fogbeam.quoddy.subscription.BaseSubscription
import org.fogbeam.quoddy.subscription.BusinessEventSubscription

import grails.plugin.springsecurity.annotation.Secured

// @Mixin(SidebarPopulatorMixin)
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
		User user = null;
		
		def systemDefinedStreams = new ArrayList<UserStreamDefinition>();
		def userDefinedStreams = new ArrayList<UserStreamDefinition>();

		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		
		
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
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def createWizardFlow()
	{
		start {
			action {	
			}
			on("success").to("createWizardOne")
		}
		
		/* a view state to bring up our GSP */
		createWizardOne {
			on("stage2") {
				
				log.debug( "stage2");
			   
				UserStreamDefinition streamToCreate = new UserStreamDefinition();
				streamToCreate.name = params.streamName;
				streamToCreate.description = params.streamDescription;
				
				def user = userService.findUserByUserId( session.user.userId );
				streamToCreate.owner = user;
				streamToCreate.definedBy = UserStreamDefinition.DEFINED_USER;
				flow.streamToCreate = streamToCreate;
			
				
				Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
				[eventTypes:eventTypes];
					
			}.to("createWizardTwo")
		}
		
		createWizardTwo {
			on("stage3"){
				log.debug( "stage3");
				log.trace( "params: ${params}");
				UserStreamDefinition streamToCreate = flow.streamToCreate;
				// eventTypes:[219, 218]
				String[] eventTypes = request.getParameterValues( 'eventTypes' );
				
				log.debug( "eventTypes: ${eventTypes}");
				
				for( String eventTypeId : eventTypes ) 
				{
					EventType eventType = eventTypeService.findEventTypeById( Long.valueOf( eventTypeId ) );
					streamToCreate.addToEventTypesIncluded( eventType );
				}
				
				/* load user list */
				// List<User> allusers = userService.findAllUsers();
				log.debug( "getting eligible users: ");
				List<User> eligibleUsers = userService.findEligibleUsersForUser( session.user );
				log.debug( "found ${eligibleUsers.size()} eligible users\n");
				
				log.debug("returning eligible users:");
				[users:eligibleUsers, selectedUsers:streamToCreate.userUuidsIncluded];

			}.to("createWizardThree")
		}
		
		createWizardThree {
			on( "stage4") {
				log.debug( "stage4" );
				log.trace( "params: ${params}");
				UserStreamDefinition streamToCreate = flow.streamToCreate;
				
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
								
				
				/* load userList list */
				List<UserList> userLists = userListService.getListsForUser( session.user );
				
				[userLists:userLists, selectedUserLists:streamToCreate.userListUuidsIncluded];
			}.to( "createWizardFour")

		}
		
		createWizardFour {
			on( "stage5" ) {
				

				// save user lists
				log.debug( "stage5" );
				log.trace( "params: ${params}");
				UserStreamDefinition streamToCreate = flow.streamToCreate;
				
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
												
				/* load group list */
				List<UserGroup> groups = userGroupService.getAllGroupsForUser( session.user );
				log.debug( "found ${groups.size()} groups");
				[groups:groups, selectedGroups:streamToCreate.userGroupUuidsIncluded];
				
			}.to( "createWizardFive")
		}
		
		createWizardFive {
			on( "stage6") {
				log.debug( "stage6" );
				log.trace( "params: ${params}" );
				UserStreamDefinition streamToCreate = flow.streamToCreate;
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
										
				/* load subscription list */
				List<BusinessEventSubscription> eventSubscriptions =
					businessEventSubscriptionService.getAllSubscriptionsForUser( session.user );
				
				[eventSubscriptions:eventSubscriptions, selectedEventSubscriptions:streamToCreate.subscriptionUuidsIncluded];
			
			}.to( "createWizardSix")

		}
		
		createWizardSix {
			on( "finishWizard") {
				log.debug( "finishing Wizard" );
				log.trace( "params: ${params}" );
				UserStreamDefinition streamToCreate = flow.streamToCreate;
				
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
				
			}.to( "finish")

		}

		/* an action state to do the final save/update on the object */
		finish {
			action {
				log.debug( "create using params: ${params}");
				UserStreamDefinition streamToCreate = flow.streamToCreate;
				
				if( !streamToCreate.save() )
				{
					log.debug( "saving UserStream FAILED");
					streamToCreate.errors.allErrors.each { log.debug( it ) };
				}
			}
			on("success").to("exitWizard");
	   }
		
	   exitWizard {
			redirect(controller:"userStreamDefinition", action:"index");
	   }
		
		
		
	}
	
	
	
	/* NOTE:  this has a problem as implemented, if the user moves back and forth in the wizard using the
	 * back button.  Because we "touch" our actual domain object (using calls like addToUserUuidsIncluded)
	 * in intermediate stages, we can get errors like "Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [org.fogbeam.quoddy.UserStreamDefinition#6]"
	 * To prevent this, we probably need to do all of the intermediate stage operations on a detached object which is a clone
	 * of the real object, and then merge it only in the final state.  This also ensures we don't persist a change
	 * that the user intended to abandon (by not finshing the wizard). See Bugzilla bug #125. 
	 */
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def editWizardFlow()
	{
		start {
			action {
				def streamId = params.streamId;
				log.debug( "Editing UserStream with id: ${streamId}");
				UserStreamDefinition streamToEdit = null;
				streamToEdit = UserStreamDefinition.findById( streamId );
				
				// select only the event types that are "user" scoped
				// as opposed to "subscription" types
				// Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
				Set<EventType> eventTypes = eventTypeService.findEventTypesByScope( EventTypeScopes.EVENT_TYPE_USER.name );
				
				log.debug(  "found eventTypes with size = ${eventTypes?.size()}");
				[streamToEdit:streamToEdit, eventTypes:eventTypes];
			}
			on("success").to("editWizardOne")
		}
		
		/* a view state to bring up our GSP */
		editWizardOne {
			on("stage2") {
				
				log.debug( "transitioning to stage2" );
			   
				UserStreamDefinition streamToEdit = flow.streamToEdit;
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
				
				
				// Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
				[eventTypes:flow.eventTypes, selectedEventTypes:streamToEdit.eventTypesIncluded];
				
				
			}.to("editWizardTwo")
		}
		
		editWizardTwo {
			on("stage3"){
				log.debug( "stage3" );
			   
				log.trace( "params: ${params}");
				
				UserStreamDefinition streamToEdit = flow.streamToEdit;
				
				
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
				List<User> allusers = userService.findAllUsers();
				List<User> eligibleUsers = userService.findEligibleUsersForUser( session.user );
				
				log.debug( "Found ${eligibleUsers.size()} eligible users\n");
								
				[users:eligibleUsers, selectedUsers:streamToEdit.userUuidsIncluded];
			
			}.to("editWizardThree")
		}
		
		editWizardThree {
			on( "stage4") {
				log.debug(  "stage4" );
				logger.trace( "params: ${params}" );
				UserStreamDefinition streamToEdit = flow.streamToEdit;
				
				// save users
				String[] userUuids = request.getParameterValues( 'users' );
				streamToEdit.userUuidsIncluded.clear();
				
				for( String userUuid : userUuids )
				{
					User userToInclude = userService.findUserByUuid( userUuid );
					if( userToInclude == null ) {
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
				List<UserList> userLists = userListService.getListsForUser( session.user );
				
				[userLists:userLists, selectedUserLists:streamToEdit.userListUuidsIncluded];
			}.to( "editWizardFour")

		}
		editWizardFour {
			on( "stage5" ) {
				

				// save user lists
				log.debug( "stage5" );
				log.trace( "params: ${params}");
				UserStreamDefinition streamToEdit = flow.streamToEdit;
				
				// userLists
				String[] userListUuids = request.getParameterValues( 'userLists' );
				streamToEdit.userListUuidsIncluded.clear();
				
				for( String userListUuid : userListUuids )
				{
					UserList userListToInclude = userListService.findUserListByUuid( userListUuid );
					if( userListToInclude == null ) {
						log.debug( "Failed to locate UserList for uuid ${userListUuid}");
						continue;
					}
					
					streamToEdit.addToUserListUuidsIncluded( userListUuid );
				}
												
				/* load group list */
				List<UserGroup> groups = userGroupService.getAllGroupsForUser( session.user );
				log.debug( "found ${groups.size()} groups");
				[groups:groups, selectedGroups:streamToEdit.userGroupUuidsIncluded];
				
			}.to( "editWizardFive")	
		}
		
		editWizardFive {
			on( "stage6") {
				log.debug( "stage6" );
				log.trace( "params: ${params}" );
				UserStreamDefinition streamToEdit = flow.streamToEdit;
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
										
				/* load subscription list */
				List<BaseSubscription> eventSubscriptions =
					eventSubscriptionService.getAllSubscriptionsForUser( session.user );
				
				log.debug( "Found eventSubscriptions with size: ${eventSubscriptions?.size()}");	
					
				[eventSubscriptions:eventSubscriptions, selectedEventSubscriptions:streamToEdit.subscriptionUuidsIncluded];
			
			}.to( "editWizardSix")

		}
		
		editWizardSix {
			on( "finishWizard") {
				log.debug( "finishing Wizard" );
				log.trace( "params: ${params}" );
				UserStreamDefinition streamToEdit = flow.streamToEdit;
				
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
				
			}.to( "finish")

		}
		
		finish {
			action {
				log.debug( "update using params: ${params}");
				def streamId = params.streamId;
				UserStreamDefinition streamToEdit = flow.streamToEdit;
				
				if( !streamToEdit.save() )
				{
					log.error( "Saving UserStream FAILED");
					streamToEdit.errors.allErrors.each { log.debug(it) };
				}
				
			}
			on("success").to("exitWizard");
		}
		
		exitWizard {
			redirect(controller:"userStreamDefinition", action:"index");
	   }
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
	
	/* TODO: This method is never called now that we use the wizard approach, right? This
	 * should be dead code that we can delete.  Verify and delete this if so.
	 */
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def update()
	{
		// TODO: implement this...
		log.debug( "update using params: ${params}");
		def streamId = params.streamId;
		UserStreamDefinition streamToEdit = null;
		
		streamToEdit = UserStreamDefinition.findById( streamId );
		
		streamToEdit.name = params.streamName;
		streamToEdit.save();
		
		redirect(controller:"userStreamDefinition", action:"index");
	}
}