package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.stream.ActivitiUserTask
import org.fogbeam.quoddy.stream.BusinessEventSubscriptionItem
import org.fogbeam.quoddy.stream.CalendarFeedItem
import org.fogbeam.quoddy.stream.RssFeedItem
import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription
import org.fogbeam.quoddy.subscription.BusinessEventSubscription
import org.fogbeam.quoddy.subscription.CalendarFeedSubscription
import org.fogbeam.quoddy.subscription.RssFeedSubscription

@Mixin(SidebarPopulatorMixin)
class SubscriptionController
{
	def userService;
	def userStreamDefinitionService;
	def userListService;
	def userGroupService;
	def businessEventSubscriptionService;
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
	
	def index =
	{
		User user = null;
		
		def systemDefinedStreams = new ArrayList<UserStreamDefinition>();
		def userDefinedStreams = new ArrayList<UserStreamDefinition>(); 
		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		def eventSubscriptions = new ArrayList<BusinessEventSubscription>();
		def calendarFeedSubscriptions = new ArrayList<CalendarFeedSubscription>();
		def activitiUserTaskSubscriptions = new ArrayList<ActivitiUserTaskSubscription>();
		def rssFeedSubscriptions = new ArrayList<RssFeedSubscription>();
		
		if( session.user != null )
		{
			log.debug( "got user: ${session.user}");
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

	def displayBusinessEventSubscription =
	{
		
		
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			log.debug( "Doing display with params: ${params}");
			def subEvents = new ArrayList<BusinessEventSubscriptionItem>();
								
			
			BusinessEventSubscription subscription = BusinessEventSubscription.findById( params.subscriptionId );
			
			Map model = [:];
			if( user )
			{
				subEvents = businessEventSubscriptionService.getRecentEventsForSubscription( subscription, 25 );
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

	def displayCalendarFeedSubscription =
	{
		
		
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			log.debug( "Doing display with params: ${params}");
			def subEvents = new ArrayList<CalendarFeedItem>();
								
			
			CalendarFeedSubscription subscription = CalendarFeedSubscription.findById( params.subscriptionId );
			
			Map model = [:];
			if( user )
			{
				subEvents = calendarFeedSubscriptionService.getRecentItemsForSubscription( subscription, 25 );
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

	
	def displayActivitiUserTaskSubscription =
	{
		
		
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			log.debug( "Doing display with params: ${params}");
			def subEvents = new ArrayList<ActivitiUserTask>();
								
			
			ActivitiUserTaskSubscription subscription = ActivitiUserTaskSubscription.findById( params.subscriptionId );
			
			Map model = [:];
			if( user )
			{
				subEvents = activitiUserTaskSubscriptionService.getRecentItemsForSubscription( subscription, 25 );
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
		

	def displayRssFeedSubscription =
	{
		
		
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			log.debug(  "Doing display with params: ${params}");
			def subEvents = new ArrayList<RssFeedItem>();
								
			
			RssFeedSubscription subscription = RssFeedSubscription.findById( params.subscriptionId );
			
			Map model = [:];
			if( user )
			{
				subEvents = rssFeedSubscriptionService.getRecentItemsForSubscription( subscription, 25 );
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
				
				log.debug(  "transitioning to stage2");				
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
		   on( "rssFeed" ).to("createRssFeedSubscriptionWizardOne")
		}
		
				
		createBusinessEventSubscriptionWizardOne {
				
				on( "stage2" ) {
					log.debug( "Creating BES with params: ${params}");
					BusinessEventSubscription subscriptionToCreate = new BusinessEventSubscription();
					subscriptionToCreate.name = params.subscriptionName;
					subscriptionToCreate.description = params.subscriptionDescription;
			   
					// UserService userService = grailsApplication.mainContext.getBean('userService');
					def user = userService.findUserByUserId( session.user.userId );
					subscriptionToCreate.owner = user;
				
					log.debug( "about to create: ${subscriptionToCreate}");
					flow.subscriptionToCreate = subscriptionToCreate;
			
				}.to("createBusinessEventSubscriptionWizardTwo")
 
			}
		
		
		
		createBusinessEventSubscriptionWizardTwo {
			on("finishWizard"){
				log.debug( "finishing wizard with params ${params}");
				
				BusinessEventSubscription subscriptionToCreate = flow.subscriptionToCreate;
				subscriptionToCreate.xQueryExpression = params.xQueryExpression;
				
			   [];
			}.to("finishBusinessEventSubscription")
		}
		
		/* an action state to do the final save/update on the object */
		finishBusinessEventSubscription {
			action {
				log.debug( "create using params: ${params}");

				BusinessEventSubscription subscriptionToCreate = flow.subscriptionToCreate;
				
				log.debug( "about to save: ${ subscriptionToCreate.toString()}");
				
				if( !subscriptionToCreate.save() )
				{
					log.error( "Saving BusinessEventSubscription FAILED");
					subscriptionToCreate.errors.allErrors.each { log.debug( it ) };
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
				log.debug( "create using params: ${params}");

				ActivitiUserTaskSubscription subscriptionToCreate = flow.subscriptionToCreate;
				
				// save this new subscription using the activitiSubscriptionService
				activitiUserTaskSubscriptionService.saveSubscription( subscriptionToCreate );
				
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
				log.debug( "create using params: ${params}");

				CalendarFeedSubscription calendarFeedToCreate = flow.calendarFeedToCreate;
				
				if( !calendarFeedToCreate.save() )
				{
					log.error( "Saving CalendarFeedSubscription FAILED");
					calendarFeedToCreate.errors.allErrors.each { log.debug( it ) };
				}
				
			}
			on("success").to("exitWizard");
	   }

		
	   createRssFeedSubscriptionWizardOne {
		   on( "stage2" ){
			   RssFeedSubscription subscriptionToCreate = new RssFeedSubscription();
			   subscriptionToCreate.name = params.subscriptionName;
			   subscriptionToCreate.url = params.subscriptionUrl;
			   
			   def user = userService.findUserByUserId( session.user.userId );
			   subscriptionToCreate.owner = user;
		   
			   flow.subscriptionToCreate = subscriptionToCreate;
		   }.to( "finishCreateRssFeedSubscription")
	   }
		
	   
	   finishCreateRssFeedSubscription {
		   
		   action {
			   log.debug( "create using params: ${params}");

			   RssFeedSubscription subscriptionToCreate = flow.subscriptionToCreate;
			   
			   if( !subscriptionToCreate.save() )
			   {
				   log.error( "Saving RssFeedSubscription FAILED");
				   subscriptionToCreate.errors.allErrors.each { log.debug( it ) };
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
				log.debug( "Editing Subscription with id: ${subscriptionId}");
				
				def subscriptionType = params.subscriptionType;
				log.debug( "subscriptionType: ${subscriptionType}");
				
				
				switch( subscriptionType )
				{
					case "ActivitiUserTaskSubscription":
					
						ActivitiUserTaskSubscription subscriptionToEdit = null;
						subscriptionToEdit = ActivitiUserTaskSubscription.findById( subscriptionId );
						flow.subscriptionToEdit = subscriptionToEdit;
	
						activitiUserTaskSubscription();
						break;
						
					case "BusinessEventSubscription":
	
						BusinessEventSubscription subscriptionToEdit = null;
						subscriptionToEdit = BusinessEventSubscription.findById( subscriptionId );
						flow.subscriptionToEdit = subscriptionToEdit;
	
						businessEventSubscription();
						break;
						
					case "CalendarFeedSubscription":
						
						CalendarFeedSubscription subscriptionToEdit = null;
						subscriptionToEdit = CalendarFeedSubscription.findById( subscriptionId );
						flow.subscriptionToEdit = subscriptionToEdit;
	
						calendarFeedSubscription();
						break;
						
					case "RssFeedSubscription":
					
						RssFeedSubscription subscriptionToEdit = null;
						subscriptionToEdit = RssFeedSubscription.findById( subscriptionId );
						flow.subscriptionToEdit = subscriptionToEdit;
	
						rssFeedSubscription();
						break;
					default:
						break;
					
				}
				
			}
			on( "activitiUserTaskSubscription" ).to("editActivitiUserTaskSubscriptionWizardOne")
			on( "businessEventSubscription" ).to("editBusinessEventSubscriptionWizardOne")
			on( "calendarFeedSubscription" ).to("editCalendarFeedSubscriptionWizardOne")
			on( "rssFeedSubscription" ).to("editRssFeedSubscriptionWizardOne")
		}
		
		editBusinessEventSubscriptionWizardOne {
			on("stage2") {
				
				log.debug( "transitioning to stage2");
				
				BusinessEventSubscription subscriptionToEdit = flow.subscriptionToEdit;
				subscriptionToEdit.name = params.subscriptionName;
				subscriptionToEdit.description = params.subscriptionDescription;
					
			}.to( "editBusinessEventSubscriptionWizardTwo" );
			
		}
		
		editBusinessEventSubscriptionWizardTwo {
			on( "finishWizard" ) {
				log.debug( "finishing wizard");
				[];	
			}.to( "finishBusinessEventSubscriptionWizard" );	
		}
		
		/* an action state to do the final save/update on the object */
		finishBusinessEventSubscriptionWizard {
			action {
				log.debug( "update using params: ${params}");
				
				def subscriptionId = params.subscriptionId;
				BusinessEventSubscription subscriptionToEdit = flow.subscriptionToEdit;
				
				subscriptionToEdit.xQueryExpression = params.xQueryExpression;
				
				if( !subscriptionToEdit.save() )
				{
					log.error( "Saving BusinessEventSubscription FAILED");
					subscriptionToEdit.errors.allErrors.each { log.debug( it ) };
				}
				
			}
			on("success").to("exitWizard");
		}
			
		/* activiti user task */
		editActivitiUserTaskSubscriptionWizardOne {
			on("finish") {
				
				ActivitiUserTaskSubscription subscriptionToEdit = flow.subscriptionToEdit;
				subscriptionToEdit.name = params.subscriptionName;
				subscriptionToEdit.description = params.subscriptionDescription;
				subscriptionToEdit.activitiServer = params.activitiServer;
				subscriptionToEdit.candidateGroup = params.candidateGroup;
				subscriptionToEdit.assignee = params.assignee;
				
				
			}.to( "finishActivitiUserTaskSubscriptionWizardOne" )
		}
		
		finishActivitiUserTaskSubscriptionWizardOne 
		{
			action {
				
				log.debug( "update using params: ${params}");
				
				ActivitiUserTaskSubscription subscriptionToEdit = flow.subscriptionToEdit;
				
				if( !subscriptionToEdit.save() )
				{
					log.debug( "Saving BusinessEventSubscription FAILED");
					subscriptionToEdit.errors.allErrors.each { log.debug( it ) };
				}
				
			}
			on( "success").to("exitWizard");
		}
		
		/* calendar feed */
		editCalendarFeedSubscriptionWizardOne {
			on("finish") {
				CalendarFeedSubscription subscriptionToEdit = flow.subscriptionToEdit;
				subscriptionToEdit.name = params.calFeedName;
				subscriptionToEdit.url = params.calFeedUrl;
			}.to( "finishCalendarFeedSubscriptionWizardOne" )
		}
		
		finishCalendarFeedSubscriptionWizardOne
		{
			action {
				
				log.debug( "update using params: ${params}" );
				
				CalendarFeedSubscription subscriptionToEdit = flow.subscriptionToEdit;
				
				if( !subscriptionToEdit.save() )
				{
					log.error( "Saving CalendarFeedSubscription FAILED");
					subscriptionToEdit.errors.allErrors.each { log.debug( it ) };
				}
				
			}
			on( "success").to("exitWizard");
		}

	
		/* rss feed */
		editRssFeedSubscriptionWizardOne {
			on("finish") {
				
			}.to( "finishRssFeedSubscriptionWizardOne" )
		}
		
		finishRssFeedSubscriptionWizardOne
		{
			action {
				
				log.debug( "update using params: ${params}");
			}
			on( "success").to("exitWizard");
		}


		exitWizard {
			redirect(controller:"subscription", action:"index");
		}

				
	}	
	
	
	
	
	
	
	
	
	
		
}
