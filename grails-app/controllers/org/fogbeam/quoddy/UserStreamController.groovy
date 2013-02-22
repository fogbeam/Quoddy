package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin

@Mixin(SidebarPopulatorMixin)
class UserStreamController
{
	def userService;
	def eventStreamService;
	def userStreamService;
	def userListService;
	def userGroupService;
	def eventSubscriptionService;
	def eventTypeService;
	
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
	

	def createWizardFlow =
	{
		start {
			action {	
			}
			on("success").to("createWizardOne")
		}
		
		/* a view state to bring up our GSP */
		createWizardOne {
			on("stage2") {
				
				println "stage2";
			   
				UserStream streamToCreate = new UserStream();
				streamToCreate.name = params.streamName;
				streamToCreate.description = params.streamDescription;
				
				def user = userService.findUserByUserId( session.user.userId );
				streamToCreate.owner = user;
				streamToCreate.definedBy = UserStream.DEFINED_USER;
				flow.streamToCreate = streamToCreate;
			
				
				Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
				[eventTypes:eventTypes];
					
			}.to("createWizardTwo")
		}
		
		createWizardTwo {
			on("stage3"){
				println "stage3";
				println "params: ${params}";
				UserStream streamToCreate = flow.streamToCreate;
				// eventTypes:[219, 218]
				String[] eventTypes = request.getParameterValues( 'eventTypes' );
				
				println "eventTypes: ${eventTypes}";
				
				for( String eventTypeId : eventTypes ) 
				{
					EventType eventType = eventTypeService.findEventTypeById( Long.valueOf( eventTypeId ) );
					streamToCreate.addToEventTypesIncluded( eventType );
				}
				
				/* load user list */
				// List<User> allusers = userService.findAllUsers();
				println "getting eligible users: ";
				List<User> eligibleUsers = userService.findEligibleUsersForUser( session.user );
				println "Found ${eligibleUsers.size()} eligible users\n";
				
				println ("returning eligible users:");
				[users:eligibleUsers, selectedUsers:streamToCreate.userUuidsIncluded];

			}.to("createWizardThree")
		}
		
		createWizardThree {
			on( "stage4") {
				println "stage4";
				println "params: ${params}";
				UserStream streamToCreate = flow.streamToCreate;
				
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
							println "Failed to locate User for uuid ${userUuid}";
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
				println( "stage5" );
				println "params: ${params}";
				UserStream streamToCreate = flow.streamToCreate;
				
				// userLists
				String[] userListUuids = request.getParameterValues( 'userLists' );
				streamToCreate.userListUuidsIncluded?.clear();
				
				for( String userListUuid : userListUuids )
				{
					UserList userListToInclude = userListService.findUserListByUuid( userListUuid );
					if( userListToInclude == null ) {
						println "Failed to locate UserList for uuid ${userListUuid}";
						continue;
					}
					
					streamToCreate.addToUserListUuidsIncluded( userListUuid );
				}
												
				/* load group list */
				List<UserGroup> groups = userGroupService.getAllGroupsForUser( session.user );
				println "found ${groups.size()} groups";
				[groups:groups, selectedGroups:streamToCreate.userGroupUuidsIncluded];
				
			}.to( "createWizardFive")
		}
		
		createWizardFive {
			on( "stage6") {
				println "stage6";
				println "params: ${params}";
				UserStream streamToCreate = flow.streamToCreate;
				// save groups
		
				String[] userGroupUuids = request.getParameterValues( 'userGroups' );
				streamToCreate.userGroupUuidsIncluded?.clear();
				
				for( String userGroupUuid : userGroupUuids )
				{
					UserGroup userGroupToInclude = userGroupService.findUserGroupByUuid( userGroupUuid );
					if( userGroupToInclude == null ) {
						println "Failed to locate UserGroup for uuid ${userGroupUuid}";
						continue;
					}
					
					streamToCreate.addToUserGroupUuidsIncluded( userGroupUuid );
				}
										
				/* load subscription list */
				List<EventSubscription> eventSubscriptions =
					eventSubscriptionService.getAllSubscriptionsForUser( session.user );
				
				[eventSubscriptions:eventSubscriptions, selectedEventSubscriptions:streamToCreate.subscriptionUuidsIncluded];
			
			}.to( "createWizardSix")

		}
		
		createWizardSix {
			on( "finishWizard") {
				println "finishing Wizard";
				println "params: ${params}";
				UserStream streamToCreate = flow.streamToCreate;
				
				// save subscriptions
				// subscriptionUuidsIncluded
				String[] eventSubscriptionUuids = request.getParameterValues( 'eventSubscriptions' );
				streamToCreate.subscriptionUuidsIncluded?.clear();
				
				for( String eventSubscriptionUuid : eventSubscriptionUuids )
				{
					EventSubscription eventSubscriptionToInclude = eventSubscriptionService.findByUuid( eventSubscriptionUuid );
					if( eventSubscriptionToInclude == null ) {
						println "Failed to locate EventSubscription for uuid ${eventSubscriptionUuid}";
						continue;
					}
					
					streamToCreate.addToSubscriptionUuidsIncluded( eventSubscriptionUuid );
				}
				
			}.to( "finish")

		}

		/* an action state to do the final save/update on the object */
		finish {
			action {
				println "create using params: ${params}"
				UserStream streamToCreate = flow.streamToCreate;
				
				if( !streamToCreate.save() )
				{
					println( "Saving UserStream FAILED");
					streamToCreate.errors.allErrors.each { println it };
				}
			}
			on("success").to("exitWizard");
	   }
		
	   exitWizard {
			redirect(controller:"userStream", action:"index");
	   }
		
		
		
	}
	
	
	
	def editWizardFlow =
	{
		start {
			action {
				def streamId = params.streamId;
				println "Editing UserStream with id: ${streamId}";
				UserStream streamToEdit = null;
				streamToEdit = UserStream.findById( streamId );
		
				Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
				
				[streamToEdit:streamToEdit, eventTypes:eventTypes];
			}
			on("success").to("editWizardOne")
		}
		
		/* a view state to bring up our GSP */
		editWizardOne {
			on("stage2") {
				
				println "transitioning to stage2";
			   
				UserStream streamToEdit = flow.streamToEdit;
				streamToEdit.name = params.streamName;
				streamToEdit.description = params.streamDescription;

				
				Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
				[eventTypes:eventTypes, selectedEventTypes:streamToEdit.eventTypesIncluded];
				
				
			}.to("editWizardTwo")
		}
		
		editWizardTwo {
			on("stage3"){
				println "stage3";
			   
				println "params: ${params}";
				
				UserStream streamToEdit = flow.streamToEdit;
				
				
				String[] eventTypes = request.getParameterValues( 'eventTypes' );
				
				streamToEdit.eventTypesIncluded.clear();
				
				for( String eventTypeId : eventTypes ) 
				{
					EventType eventType = eventTypeService.findEventTypeById( Long.valueOf( eventTypeId ) );
					
					if( eventType == null ) {
						println "Failed to locate eventType entry for id: ${eventTypeId}";
						continue;	
					}
					streamToEdit.addToEventTypesIncluded( eventType );
				}				
				
				/* load user list */
				List<User> allusers = userService.findAllUsers();
				List<User> eligibleUsers = userService.findEligibleUsersForUser( session.user );
				
				println "Found ${eligibleUsers.size()} eligible users\n";
				
				[users:eligibleUsers, eligibleUsers:streamToEdit.userUuidsIncluded];
			
			}.to("editWizardThree")
		}
		
		editWizardThree {
			on( "stage4") {
				println "stage4";
				println "params: ${params}";
				UserStream streamToEdit = flow.streamToEdit;
				
				// save users
				String[] userUuids = request.getParameterValues( 'users' );
				streamToEdit.userUuidsIncluded.clear();
				
				for( String userUuid : userUuids )
				{
					User userToInclude = userService.findUserByUuid( userUuid );
					if( userToInclude == null ) {
						println "Failed to locate User for uuid ${userUuid}";
						continue;
					}
					
					streamToEdit.addToUserUuidsIncluded( userUuid );
				}
				
				
				/* load userList list */
				List<UserList> userLists = userListService.getListsForUser( session.user );
				
				[userLists:userLists, selectedUserLists:streamToEdit.userListUuidsIncluded];
			}.to( "editWizardFour")

		}
		editWizardFour {
			on( "stage5" ) {
				

				// save user lists
				println( "stage5" );
				println "params: ${params}";
				UserStream streamToEdit = flow.streamToEdit;
				
				// userLists
				String[] userListUuids = request.getParameterValues( 'userLists' );
				streamToEdit.userListUuidsIncluded.clear();
				
				for( String userListUuid : userListUuids )
				{
					UserList userListToInclude = userListService.findUserListByUuid( userListUuid );
					if( userListToInclude == null ) {
						println "Failed to locate UserList for uuid ${userListUuid}";
						continue;
					}
					
					streamToEdit.addToUserListUuidsIncluded( userListUuid );
				}
												
				/* load group list */
				List<UserGroup> groups = userGroupService.getAllGroupsForUser( session.user );
				println "found ${groups.size()} groups";
				[groups:groups, selectedGroups:streamToEdit.userGroupUuidsIncluded];
				
			}.to( "editWizardFive")	
		}
		
		editWizardFive {
			on( "stage6") {
				println "stage6";
				println "params: ${params}";
				UserStream streamToEdit = flow.streamToEdit;
				// save groups
		
				String[] userGroupUuids = request.getParameterValues( 'userGroups' );
				streamToEdit.userGroupUuidsIncluded.clear();
				
				for( String userGroupUuid : userGroupUuids )
				{
					UserGroup userGroupToInclude = userGroupService.findUserGroupByUuid( userGroupUuid );
					if( userGroupToInclude == null ) {
						println "Failed to locate UserGroup for uuid ${userGroupUuid}";
						continue;
					}
					
					streamToEdit.addToUserGroupUuidsIncluded( userGroupUuid );
				}
										
				/* load subscription list */
				List<EventSubscription> eventSubscriptions =
					eventSubscriptionService.getAllSubscriptionsForUser( session.user );
				
				[eventSubscriptions:eventSubscriptions, selectedEventSubscriptions:streamToEdit.subscriptionUuidsIncluded];
			
			}.to( "editWizardSix")

		}
		
		editWizardSix {
			on( "finishWizard") {
				println "finishing Wizard";
				println "params: ${params}";
				UserStream streamToEdit = flow.streamToEdit;
				
				// save subscriptions
				// subscriptionUuidsIncluded
				String[] eventSubscriptionUuids = request.getParameterValues( 'eventSubscriptions' );
				streamToEdit.subscriptionUuidsIncluded.clear();
				
				for( String eventSubscriptionUuid : eventSubscriptionUuids )
				{
					EventSubscription eventSubscriptionToInclude = eventSubscriptionService.findByUuid( eventSubscriptionUuid );
					if( eventSubscriptionToInclude == null ) {
						println "Failed to locate EventSubscription for uuid ${eventSubscriptionUuid}";
						continue;
					}
					
					streamToEdit.addToSubscriptionUuidsIncluded( eventSubscriptionUuid );
				}
				
			}.to( "finish")

		}
		
		finish {
			action {
				println "update using params: ${params}"
				def streamId = params.streamId;
				UserStream streamToEdit = flow.streamToEdit;
				
				if( !streamToEdit.save() )
				{
					println( "Saving UserStream FAILED");
					streamToEdit.errors.allErrors.each { println it };
				}
				
			}
			on("success").to("exitWizard");
		}
		
		exitWizard {
			redirect(controller:"userStream", action:"index");
	   }
	}
	
		
	def edit =
	{
		def streamId = params.id;
		println "Editing UserStream with id: ${streamId}";
		UserStream streamToEdit = null;
		
		streamToEdit = UserStream.findById( streamId );
		
		
		[streamToEdit:streamToEdit];	
	}
	
	def update = 
	{
		
		// TODO: implement this...
		println "update using params: ${params}"
		def streamId = params.streamId;
		UserStream streamToEdit = null;
		
		streamToEdit = UserStream.findById( streamId );
		
		streamToEdit.name = params.streamName;
		streamToEdit.save();
		
		redirect(controller:"userStream", action:"index");
	}
}