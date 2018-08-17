package org.fogbeam.quoddy

import org.fogbeam.quoddy.subscription.CalendarFeedSubscription;

import grails.plugin.springsecurity.annotation.Secured

class CalendarController
{
	def userService;
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{
		List<CalendarFeedSubscription> calFeeds = new ArrayList<CalendarFeedSubscription>();
		User user = null;
		if( session.user != null )
		{
			log.debug( "Found User in Session");
			user = userService.findUserByUserId( session.user.userId );
			def queryResults = CalendarFeedSubscription.executeQuery( "select calfeed from CalendarFeedSubscription as calfeed where calfeed.owner = :owner", [owner:user] );
			
			calFeeds.addAll( queryResults ); 
		}
		else
		{
			log.info( "No user in Session");
		}
		
		[calFeeds:calFeeds];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def createFeed()
	{
		[];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveFeed()
	{
		
		if( session.user != null )
		{
			CalendarFeedSubscription calFeed = new CalendarFeedSubscription();
			
			User user = userService.findUserByUserId( session.user.userId );
			calFeed.owner = user;
			calFeed.url = params.calFeedUrl;
			calFeed.name = params.calFeedName;
			
			if( !calFeed.save(flush:true) )
			{
				log.debug( "Saving CalendarFeedSubscription FAILED");
				calFeed.errors.allErrors.each { log.debug( it ) };
			}
		}
		else
		{
			log.debug( "No user in Session");
		}
		
		redirect( controller:"calendar", action:"index");
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def editFeed()
	{
		CalendarFeedSubscription calFeedToEdit = null;
		if( session.user != null )
		{
			def calFeedId = params.calFeedId;
			calFeedToEdit = CalendarFeedSubscription.findById( params.calFeedId);
		}
		else
		{
			log.debug( "No user in Session");
		}		
		
		[calFeedToEdit:calFeedToEdit];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def updateFeed()
	{
		if( session.user != null )
		{
			CalendarFeedSubscription calFeed = CalendarFeedSubscription.findById( params.calFeedId);
			
			calFeed.url = params.calFeedUrl;
			calFeed.name = params.calFeedName;
			
			if( !calFeed.save(flush:true) )
			{
				log.debug( "Saving CalendarFeedSubscription FAILED");
				calFeed.errors.allErrors.each { log.debug( it ) };
			}
		}
		else
		{
			log.debug( "No user in Session" );
		}
		
		redirect( controller:"calendar", action:"index");
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def display()
	{
		[];	
	}
}