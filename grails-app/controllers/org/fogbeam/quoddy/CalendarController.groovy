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
		User currentUser = userService.getLoggedInUser();
		def queryResults = CalendarFeedSubscription.executeQuery( "select calfeed from CalendarFeedSubscription as calfeed where calfeed.owner = :owner", [owner:currentUser] );
		
		calFeeds.addAll( queryResults );
		
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
		CalendarFeedSubscription calFeed = new CalendarFeedSubscription();
		
		User currentUser = userService.getLoggedInUser();
		
		calFeed.owner = currentUser;
		calFeed.url = params.calFeedUrl;
		calFeed.name = params.calFeedName;
		
		if( !calFeed.save(flush:true) )
		{
			log.debug( "Saving CalendarFeedSubscription FAILED");
			calFeed.errors.allErrors.each { log.debug( it ) };
		}

		
		redirect( controller:"calendar", action:"index");
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def editFeed()
	{
		CalendarFeedSubscription calFeedToEdit = null;
		
		def calFeedId = params.calFeedId;
		
		calFeedToEdit = CalendarFeedSubscription.findById( params.calFeedId);

		[calFeedToEdit:calFeedToEdit];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def updateFeed()
	{
		CalendarFeedSubscription calFeed = CalendarFeedSubscription.findById( params.calFeedId);
		
		calFeed.url = params.calFeedUrl;
		calFeed.name = params.calFeedName;
		
		if( !calFeed.save(flush:true) )
		{
			log.debug( "Saving CalendarFeedSubscription FAILED");
			calFeed.errors.allErrors.each { log.debug( it ) };
		}

		redirect( controller:"calendar", action:"index");
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def display()
	{
		[];	
	}
}