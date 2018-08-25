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
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

import grails.plugin.springsecurity.annotation.Secured

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
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{	
		def systemDefinedStreams = new ArrayList<UserStreamDefinition>();
		def userDefinedStreams = new ArrayList<UserStreamDefinition>(); 
		def userLists = new ArrayList<UserList>();
		def userGroups = new ArrayList<UserGroup>();
		def eventSubscriptions = new ArrayList<BusinessEventSubscription>();
		def calendarFeedSubscriptions = new ArrayList<CalendarFeedSubscription>();
		def activitiUserTaskSubscriptions = new ArrayList<ActivitiUserTaskSubscription>();
		def rssFeedSubscriptions = new ArrayList<RssFeedSubscription>();
		
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId ); 
    
        Map model = [:];
        Map sidebarCollections = this.populateSidebarCollections( this, currentUser );
        model.putAll( sidebarCollections );        
        
        return model;
	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def displayBusinessEventSubscription()
	{	
        def subEvents = new ArrayList<BusinessEventSubscriptionItem>();
        
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
        

        BusinessEventSubscription subscription = BusinessEventSubscription.findById( params.subscriptionId );
        
        Map model = [:];
        subEvents = businessEventSubscriptionService.getRecentEventsForSubscription( subscription, 25 );
        model.putAll( [ activities:subEvents ] );
        
        Map sidebarCollections = this.populateSidebarCollections( this, currentUser );
        model.putAll( sidebarCollections );
        
        return model;
	}

    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def displayCalendarFeedSubscription()
	{
        def subEvents = new ArrayList<CalendarFeedItem>();
     
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
        
           
        CalendarFeedSubscription subscription = CalendarFeedSubscription.findById( params.subscriptionId );
        
        Map model = [:];
        subEvents = calendarFeedSubscriptionService.getRecentItemsForSubscription( subscription, 25 );
        model.putAll( [ activities:subEvents ] );
        
        Map sidebarCollections = this.populateSidebarCollections( this, currentUser );
        model.putAll( sidebarCollections );
                
        return model;

	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def displayActivitiUserTaskSubscription()
	{
        def subEvents = new ArrayList<ActivitiUserTask>();

        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
        
        ActivitiUserTaskSubscription subscription = ActivitiUserTaskSubscription.findById( params.subscriptionId );
        
        Map model = [:];
        subEvents = activitiUserTaskSubscriptionService.getRecentItemsForSubscription( subscription, 25 );
        model.putAll( [ activities:subEvents ] );
        
        Map sidebarCollections = this.populateSidebarCollections( this, currentUser );
        model.putAll( sidebarCollections );
                
        return model;

	}
		
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def displayRssFeedSubscription()
	{	
        def subEvents = new ArrayList<RssFeedItem>();
        
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )

        RssFeedSubscription subscription = RssFeedSubscription.findById( params.subscriptionId );
        
        Map model = [:];
        subEvents = rssFeedSubscriptionService.getRecentItemsForSubscription( subscription, 25 );
        model.putAll( [ activities:subEvents ] );
    
        Map sidebarCollections = this.populateSidebarCollections( this, currentUser );
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

        String subscriptionType = params.subscriptionType;
        
        String nextAction = "";
        if( subscriptionType.equals( "activitiUserTask" ) )
        {   
            nextAction = "createActivitiUserTaskSubscriptionWizardOne";
        }
        else if( subscriptionType.equals( "businessEvent" ) )
        {
            nextAction = "createBusinessEventSubscriptionWizardOne";
        }
        else if( subscriptionType.equals( "calendarFeed" ) )
        {
            nextAction = "createCalendarFeedSubscriptionWizardOne";
        }
        else if( subscriptionType.equals( "rssFeed" ) )
        {
            nextAction = "createRssFeedSubscriptionWizardOne";
        } 

        redirect( action:nextAction );
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createBusinessEventSubscriptionWizardOne()
    {
        log.debug( "Subscription.createBusinessEventSubscription.stage1" );
        log.trace( "Creating BES with params: ${params}");
        BusinessEventSubscription subscriptionToCreate = new BusinessEventSubscription();
        subscriptionToCreate.name = params.subscriptionName;
        subscriptionToCreate.description = params.subscriptionDescription;
   
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId ) 
        subscriptionToCreate.owner = currentUser;
    
        log.debug( "about to create: ${subscriptionToCreate}");
        session.subscriptionToCreate = subscriptionToCreate;

        [subscriptionToCreate:subscriptionToCreate];
    }


    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createBusinessEventSubscriptionWizardFinish()
    {
        log.debug( "Subscription.createBusinessEventSubscription.finish" );
        log.debug( "finishing wizard with params ${params}");
        
        BusinessEventSubscription subscriptionToCreate = session.subscriptionToCreate;
        subscriptionToCreate.name = params.subscriptionName;
        subscriptionToCreate.description = params.subscriptionDescription;
        subscriptionToCreate.xQueryExpression = params.xQueryExpression;
        
        log.debug( "about to save: ${ subscriptionToCreate.toString()}");
        
        businessEventSubscriptionService.saveSubscription( subscriptionToCreate );
    
        redirect(controller:"subscription", action:"index");
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createActivitiUserTaskSubscriptionWizardOne()
    {
        [:];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createActivitiUserTaskSubscriptionWizardFinish()
    {
        log.debug( "Subscription.createActivitiUserTaskSubscription.stage1" );
        
        ActivitiUserTaskSubscription subscriptionToCreate = new ActivitiUserTaskSubscription();
        subscriptionToCreate.name = params.subscriptionName;
        subscriptionToCreate.description = params.subscriptionDescription;
        subscriptionToCreate.activitiServer = params.activitiServer;
        subscriptionToCreate.candidateGroup = params.candidateGroup;
        subscriptionToCreate.assignee = params.assignee;
    
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId ) 

        subscriptionToCreate.owner = currentUser;
    
        log.debug( "Subscription.createActivitiUserTaskSubscription.finish" );
        log.debug( "create using params: ${params}");
        
                
        // save this new subscription using the activitiSubscriptionService
        activitiUserTaskSubscriptionService.saveSubscription( subscriptionToCreate );
    
        redirect(controller:"subscription", action:"index");
        
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createCalendarFeedSubscriptionWizardOne()
    {
        log.debug( "Subscription.createCalendarFeedSubscription.stage1" );
        [:];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createCalendarFeedSubscriptionWizardFinish()
    {
        log.debug( "Subscription.createCalendarFeedSubscription.finish" );
        
        CalendarFeedSubscription calendarFeedToCreate = new CalendarFeedSubscription();
        
        calendarFeedToCreate.url = params.calFeedUrl;
        calendarFeedToCreate.name = params.calFeedName;
    
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId ) 
        calendarFeedToCreate.owner = currentUser;
    
        session.calendarFeedToCreate = calendarFeedToCreate;
        
        calendarFeedSubscriptionService.saveSubscription( calendarFeedToCreate);

        redirect(controller:"subscription", action:"index");
    }


    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createRssFeedSubscriptionWizardOne()
    {
        log.debug( "Subscription.createRssFeedSubscription.stage1" );
        
        [:];
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def createRssFeedSubscriptionWizardFinish()
    {
        log.debug( "Subscription.createRssFeedSubscription.finish" );
        log.debug( "create using params: ${params}");

        RssFeedSubscription subscriptionToCreate = new RssFeedSubscription();
        subscriptionToCreate.name = params.subscriptionName;
        subscriptionToCreate.url = params.subscriptionUrl;
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info( "current Authentication: ${authentication}");
        
        User currentUser = userService.findUserByUserId( ((User)authentication.principal).userId )
        subscriptionToCreate.owner = currentUser;
                   
        rssFeedSubscriptionService.saveSubscription(subscriptionToCreate);
  
        redirect(controller:"subscription", action:"index");
    }


    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editWizardOne()
    {
        def subscriptionId = params.subscriptionId;
        log.debug( "Editing Subscription with id: ${subscriptionId}");
        
        def subscriptionType = params.subscriptionType;
        log.debug( "subscriptionType: ${subscriptionType}");
                
        String nextAction = "";
        switch( subscriptionType )
        {
            case "ActivitiUserTaskSubscription":
                
                ActivitiUserTaskSubscription subscriptionToEdit = null;
                subscriptionToEdit = ActivitiUserTaskSubscription.findById( subscriptionId );
                session.subscriptionToEdit = subscriptionToEdit;
                nextAction = "editActivitiUserTaskSubscriptionWizardOne";
                
                break;
                
            case "BusinessEventSubscription":

                BusinessEventSubscription subscriptionToEdit = null;
                subscriptionToEdit = BusinessEventSubscription.findById( subscriptionId );
                session.subscriptionToEdit = subscriptionToEdit;
                nextAction = "editBusinessEventSubscriptionWizardOne";
                
                break;
                
            case "CalendarFeedSubscription":
                
                CalendarFeedSubscription subscriptionToEdit = null;
                subscriptionToEdit = CalendarFeedSubscription.findById( subscriptionId );
                session.subscriptionToEdit = subscriptionToEdit;
                nextAction = "editCalendarFeedSubscriptionWizardOne";
                
                break;
                
            case "RssFeedSubscription":
            
                RssFeedSubscription subscriptionToEdit = null;
                subscriptionToEdit = RssFeedSubscription.findById( subscriptionId );
                session.subscriptionToEdit = subscriptionToEdit;
                nextAction = "editRssFeedSubscriptionWizardOne";
                
                break;
            default:
                break;

        }
        
        redirect(controller:"subscription", action:nextAction);
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editBusinessEventSubscriptionWizardOne()
    {
        BusinessEventSubscription subscriptionToEdit = session.subscriptionToEdit;
        subscriptionToEdit.discard();
        
        [subscriptionToEdit:subscriptionToEdit];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editBusinessEventSubscriptionWizardFinish()
    {
        log.debug( "update using params: ${params}");

        BusinessEventSubscription subscriptionToEdit = session.subscriptionToEdit;

        subscriptionToEdit.name = params.subscriptionName;
        subscriptionToEdit.description = params.subscriptionDescription;                
        subscriptionToEdit.xQueryExpression = params.xQueryExpression;

        // re-attach to Hibernate session        
        if( !subscriptionToEdit.isAttached())
        {
            subscriptionToEdit.attach();
        }

        if( !subscriptionToEdit.save(flush:true) )
        {
            log.error( "Saving BusinessEventSubscription FAILED");
            subscriptionToEdit.errors.allErrors.each { log.debug( it.toString() ) };
        }

        // redirect
        redirect( controller: "subscription", action:"index" );
    }
        
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editActivitiUserTaskSubscriptionWizardOne()
    {
        ActivitiUserTaskSubscription subscriptionToEdit = session.subscriptionToEdit;
        subscriptionToEdit.discard();
        
        [subscriptionToEdit:subscriptionToEdit];
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editActivitiUserTaskSubscriptionWizardFinish()
    {
        log.debug( "update using params: ${params}");
        
        ActivitiUserTaskSubscription subscriptionToEdit = session.subscriptionToEdit;
        subscriptionToEdit.name = params.subscriptionName;
        subscriptionToEdit.description = params.subscriptionDescription;
        subscriptionToEdit.activitiServer = params.activitiServer;
        subscriptionToEdit.candidateGroup = params.candidateGroup;
        subscriptionToEdit.assignee = params.assignee;
        
        // re-attach to Hibernate session
        if( !subscriptionToEdit.isAttached())
        {
            subscriptionToEdit.attach();
        }

        if( !subscriptionToEdit.save(flush:true) )
        {
            log.debug( "Saving BusinessEventSubscription FAILED");
            subscriptionToEdit.errors.allErrors.each { log.debug( it.toString() ) };
        }

        redirect( controller:"subscription", action:"index");
    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editCalendarFeedSubscriptionWizardOne()
    {
        CalendarFeedSubscription subscriptionToEdit = session.subscriptionToEdit;
        subscriptionToEdit.discard();
        
        [subscriptionToEdit:subscriptionToEdit];

    }
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editCalendarFeedSubscriptionWizardFinish()
    {
        log.debug( "update using params: ${params}" );
        
        CalendarFeedSubscription subscriptionToEdit = session.subscriptionToEdit;
        subscriptionToEdit.name = params.calFeedName;
        subscriptionToEdit.url = params.calFeedUrl;

        // re-attach to Hibernate session
        if( !subscriptionToEdit.isAttached())
        {
            subscriptionToEdit.attach();
        }
    
        if( !subscriptionToEdit.save(flush:true) )
        {
            log.error( "Saving CalendarFeedSubscription FAILED");
            subscriptionToEdit.errors.allErrors.each { log.debug( it.toString() ) };
        }

        redirect(controller:"subscription", action:"index");
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editRssFeedSubscriptionWizardOne()
    {
        RssFeedSubscription subscriptionToEdit = session.subscriptionToEdit;
        subscriptionToEdit.discard();
        
        [subscriptionToEdit:subscriptionToEdit];

    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def editRssFeedSubscriptionWizardFinish()
    {        
        log.debug( "update using params: ${params}");

        RssFeedSubscription subscriptionToEdit = session.subscriptionToEdit;

        // update params
        subscriptionToEdit.name = params.subscriptionName;
        subscriptionToEdit.url = params.subscriptionUrl;
        
        // re-attach to Hibernate session
        if( !subscriptionToEdit.isAttached())
        {
            subscriptionToEdit.attach();
        }
                
        if( !subscriptionToEdit.save(flush:true) )
        {
            log.error( "Saving RssFeedSubscription FAILED");
            subscriptionToEdit.errors.allErrors.each { log.debug( it.toString() ) };
        }

        redirect(controller:"subscription", action:"index");
    }        
}
