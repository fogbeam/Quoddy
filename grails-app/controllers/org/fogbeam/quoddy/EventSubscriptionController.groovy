package org.fogbeam.quoddy

class EventSubscriptionController
{
	def userService;
	def userStreamService;
	def userListService;
	def userGroupService;
	def eventSubscriptionService;
	
	def index =
	{
		User user = null;
		
		def systemDefinedStreams = new ArrayList<UserStream>();
		def userDefinedStreams = new ArrayList<UserStream>(); 
		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		def eventSubscriptions = new ArrayList<EventSubscription>();
		
		if( session.user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
		
		
			def tempSysStreams = userStreamService.getSystemDefinedStreamsForUser( user );
			systemDefinedStreams.addAll( tempSysStreams );
			def tempUserStreams = userStreamService.getUserDefinedStreamsForUser( user );
			userDefinedStreams.addAll( tempUserStreams );
				
			def tempUserLists = userListService.getListsForUser( user );
			userLists.addAll( tempUserLists );
				
			def tempUserGroups = userGroupService.getAllGroupsForUser( user );
			userGroups.addAll( tempUserGroups );
			
			def tempEventSubscriptions = eventSubscriptionService.getAllSubscriptionsForUser( user );
			eventSubscriptions.addAll( tempEventSubscriptions );
			
			[user:user, 
			  sysDefinedStreams:systemDefinedStreams, 
			  userDefinedStreams:userDefinedStreams,
			  userLists:userLists,
			  userGroups:userGroups,
			  eventSubscriptions:eventSubscriptions ];
	  
		}
		else
		{
			// TODO: not logged in, deal with this...	
		}
	}

	def display =
	{
		[];
	}
		
	def createWizardFlow =
	{
		start {
			action {
				[];
			}
			on("success").to("createWizardOne")
		}
		
		createWizardOne {
			on("stage2") {
				
				println "transitioning to stage2";
				
				EventSubscription subscriptionToCreate = new EventSubscription();
				subscriptionToCreate.name = params.subscriptionName;
				subscriptionToCreate.description = params.subscriptionDescription;
			   
				def user = userService.findUserByUserId( session.user.userId );
				subscriptionToCreate.owner = user;
				
				flow.subscriptionToCreate = subscriptionToCreate;
				
				
			}.to("createWizardTwo")
				
		}
		
		createWizardTwo {
			on("finishWizard"){
				println "finishing wizard with params ${params}";
				
				EventSubscription subscriptionToCreate = flow.subscriptionToCreate;
				subscriptionToCreate.xQueryExpression = params.xQueryExpression;
				
			   [];
			}.to("finish")
		}
		
		/* an action state to do the final save/update on the object */
		finish {
			action {
				println "create using params: ${params}"

				EventSubscription subscriptionToCreate = flow.subscriptionToCreate;
				
				if( !subscriptionToCreate.save() )
				{
					println( "Saving EventSubscription FAILED");
					subscriptionToCreate.errors.allErrors.each { println it };
				}
				
			}
			on("success").to("exitWizard");
	   }
		
	   exitWizard {
			redirect(controller:"eventSubscription", action:"index");
	   }
		
	}
	
	def editWizardFlow =
	{
		start {
			action {
				def subscriptionId = params.subscriptionId;
				println "Editing EventSubscription with id: ${subscriptionId}";	
				EventSubscription subscriptionToEdit = null;
				subscriptionToEdit = EventSubscription.findById( subscriptionId );
				[subscriptionToEdit: subscriptionToEdit];
			}
			on("success").to( "editWizardOne" )
		}
		
		editWizardOne {
			on("stage2") {
				
				println "transitioning to stage2";
				
				EventSubscription subscriptionToEdit = flow.subscriptionToEdit;
				subscriptionToEdit.name = params.subscriptionName;
				subscriptionToEdit.description = params.subscriptionDescription;
					
			}.to( "editWizardTwo" );
			
		}
		
		editWizardTwo {
			on( "finishWizard" ) {
				println "finishing wizard";
				[];	
			}.to( "finish" );	
		}
		
		/* an action state to do the final save/update on the object */
		finish {
			action {
				println "update using params: ${params}"
				
				def subscriptionId = params.subscriptionId;
				EventSubscription subscriptionToEdit = flow.subscriptionToEdit;
				
				subscriptionToEdit.xQueryExpression = params.xQueryExpression;
				
				if( !subscriptionToEdit.save() )
				{
					println( "Saving EventSubscription FAILED");
					subscriptionToEdit.errors.allErrors.each { println it };
				}
				
			}
			on("success").to("exitWizard");
		}
	
		exitWizard {
			redirect(controller:"eventSubscription", action:"index");
		}
	}	
	
	
	
	
	
	
	
	
	
		
}
