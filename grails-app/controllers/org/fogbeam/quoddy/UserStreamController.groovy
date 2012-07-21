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
				Set<EventType> eventTypes = eventTypeService.findAllEventTypes();
				[eventTypes:eventTypes];
			}
			on("success").to("createWizardOne")
		}
		
		/* a view state to bring up our GSP */
		createWizardOne {
			on("stage2") {
				
				println "transitioning to stage2";
			   
				UserStream streamToCreate = new UserStream();
				streamToCreate.name = params.streamName;
				streamToCreate.description = params.streamDescription;
				
				def user = userService.findUserByUserId( session.user.userId );
				streamToCreate.owner = user;
				streamToCreate.definedBy = UserStream.DEFINED_USER;
				flow.streamToCreate = streamToCreate;
				
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
			   
				UserStream streamToEdit = flow.streamToEdit
				streamToEdit.name = params.streamName;
				streamToEdit.description = params.streamDescription;

				
			}.to("editWizardTwo")
		}
		
		editWizardTwo {
			on("finishWizard"){
				println "finishing Wizard";
			   [];
			}.to("finish")
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