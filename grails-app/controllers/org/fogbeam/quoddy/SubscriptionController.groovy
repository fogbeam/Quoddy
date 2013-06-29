package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.stream.BusinessEventSubscriptionItem
import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription
import org.fogbeam.quoddy.subscription.BusinessEventSubscription
import org.fogbeam.quoddy.subscription.CalendarFeedSubscription
import org.fogbeam.quoddy.subscription.RssFeedSubscription

@Mixin(SidebarPopulatorMixin)
class SubscriptionController
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
		def eventSubscriptions = new ArrayList<BusinessEventSubscription>();
		def calendarFeedSubscriptions = new ArrayList<CalendarFeedSubscription>();
		def activitiUserTaskSubscriptions = new ArrayList<ActivitiUserTaskSubscription>();
		def rssFeedSubscriptions = new ArrayList<RssFeedSubscription>();
		
		if( session.user != null )
		{
			println "got user: ${session.user}";
			user = userService.findUserByUserId( session.user.userId );
		
			Map model = [:];
			if( user )
			{

				Map sidebarCollections = this.populateSidebarCollections( this, user );
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
			def subEvents = new ArrayList<BusinessEventSubscriptionItem>();
								
			
			BusinessEventSubscription subscription = BusinessEventSubscription.findById( params.subscriptionId );
			
			Map model = [:];
			if( user )
			{
				subEvents = eventSubscriptionService.getRecentEventsForSubscription( subscription, 25 );
				model.putAll( [ activities:subEvents ] );
				
				Map sidebarCollections = this.populateSidebarCollections( this, user );
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
		
		createWizardOne {
			on("stage2") {
				
				println "transitioning to stage2";				
			}.to("createWizardTemp")
				
		}
		
		createWizardTemp {
			
		 action {
			 String subscriptionType = params.subscriptionType;
			 
			 if( subscriptionType.equals( "activitiUserTask" ) )
			 {
				 activitiUserTask();
			 }
			 else if( subscriptionType.equals( "businessEvent" ) )
			 {
				 businessEventSubscription();
			 }
			 else if( subscriptionType.equals( "calendarFeed" ) )
			 {
				 calendarFeed();
			 }
			 else if( subscriptionType.equals( "rssFeed" ) )
			 {
				 rssFeed();
			 }
			 
		   }	
		   on( "activitiUserTask" ).to("createActivitiUserTaskSubscriptionWizardOne")
		   on( "businessEventSubscription" ).to("createBusinessEventSubscriptionWizardOne")
		   on( "calendarFeed" ).to("createCalendarFeedSubscriptionWizardOne")
		   on( "rssFeed" ).to("createBusinessEventSubscriptionWizardOne")
		}
		
				
		createBusinessEventSubscriptionWizardOne {
				
				on( "stage2" ) {
					BusinessEventSubscription subscriptionToCreate = new BusinessEventSubscription();
					subscriptionToCreate.name = params.subscriptionName;
					subscriptionToCreate.description = params.subscriptionDescription;
			   
					// UserService userService = grailsApplication.mainContext.getBean('userService');
					def user = userService.findUserByUserId( session.user.userId );
					subscriptionToCreate.owner = user;
				
					flow.subscriptionToCreate = subscriptionToCreate;
			
				}.to("createBusinessEventSubscriptionWizardTwo")
 
			}
		
		
		
		createBusinessEventSubscriptionWizardTwo {
			on("finishWizard"){
				println "finishing wizard with params ${params}";
				
				BusinessEventSubscription subscriptionToCreate = flow.subscriptionToCreate;
				subscriptionToCreate.xQueryExpression = params.xQueryExpression;
				
			   [];
			}.to("finishBusinessEventSubscription")
		}
		
		/* an action state to do the final save/update on the object */
		finishBusinessEventSubscription {
			action {
				println "create using params: ${params}"

				BusinessEventSubscription subscriptionToCreate = flow.subscriptionToCreate;
				
				if( !subscriptionToCreate.save() )
				{
					println( "Saving BusinessEventSubscription FAILED");
					subscriptionToCreate.errors.allErrors.each { println it };
				}
				
			}
			on("success").to("exitWizard");
	   }
		
		createActivitiUserTaskSubscriptionWizardOne {
			
				on( "stage2" ){
					ActivitiUserTaskSubscription subscriptionToCreate = new ActivitiUserTaskSubscription();
					subscriptionToCreate.name = params.subscriptionName;
					subscriptionToCreate.description = params.subscriptionDescription;
					subscriptionToCreate.activitiServer = params.activitiServer;
					subscriptionToCreate.candidateGroup = params.candidateGroup;
					subscriptionToCreate.assignee = params.assignee;
			   
					def user = userService.findUserByUserId( session.user.userId );
					subscriptionToCreate.owner = user;
				
					flow.subscriptionToCreate = subscriptionToCreate;
				}.to( "finishActivitiUserTaskSubscription") 
		}
		
		finishActivitiUserTaskSubscription {
			action {
				println "create using params: ${params}"

				ActivitiUserTaskSubscription subscriptionToCreate = flow.subscriptionToCreate;
				
				if( !subscriptionToCreate.save() )
				{
					println( "Saving ActivitiUserTaskSubscription FAILED");
					subscriptionToCreate.errors.allErrors.each { println it };
				}
				
			}
			on("success").to("exitWizard");
		}
		
		createCalendarFeedSubscriptionWizardOne {
			
			on( "stage2" ){

				CalendarFeedSubscription calendarFeedToCreate = new CalendarFeedSubscription();
				
				calendarFeedToCreate.url = params.calFeedUrl;
				calendarFeedToCreate.name = params.calFeedName;
		   
				def user = userService.findUserByUserId( session.user.userId );
				calendarFeedToCreate.owner = user;
			
				flow.calendarFeedToCreate = calendarFeedToCreate;
			
			}.to( "finishCalendarFeedSubscription")
		}

		finishCalendarFeedSubscription {
			action {
				println "create using params: ${params}"

				CalendarFeedSubscription calendarFeedToCreate = flow.calendarFeedToCreate;
				
				if( !calendarFeedToCreate.save() )
				{
					println( "Saving CalendarFeedSubscription FAILED");
					calendarFeedToCreate.errors.allErrors.each { println it };
				}
				
			}
			on("success").to("exitWizard");
	   }

	   exitWizard {
			redirect(controller:"subscription", action:"index");
	   }
		
	}
	
	def editWizardFlow =
	{
		start {
			action {
				def subscriptionId = params.subscriptionId;
				println "Editing BusinessEventSubscription with id: ${subscriptionId}";	
				BusinessEventSubscription subscriptionToEdit = null;
				subscriptionToEdit = BusinessEventSubscription.findById( subscriptionId );
				[subscriptionToEdit: subscriptionToEdit];
			}
			on("success").to( "editWizardOne" )
		}
		
		editWizardOne {
			on("stage2") {
				
				println "transitioning to stage2";
				
				BusinessEventSubscription subscriptionToEdit = flow.subscriptionToEdit;
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
				BusinessEventSubscription subscriptionToEdit = flow.subscriptionToEdit;
				
				subscriptionToEdit.xQueryExpression = params.xQueryExpression;
				
				if( !subscriptionToEdit.save() )
				{
					println( "Saving BusinessEventSubscription FAILED");
					subscriptionToEdit.errors.allErrors.each { println it };
				}
				
			}
			on("success").to("exitWizard");
		}
	
		exitWizard {
			redirect(controller:"subscription", action:"index");
		}
	}	
	
	
	
	
	
	
	
	
	
		
}
