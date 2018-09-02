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
		User currentUser = userService.getLoggedInUser();
		
		List<CalendarFeedSubscription> calFeeds = calendarFeedSubscriptionService.getAllSubscriptionsForUser( currentUser );
		
		[calFeeds:calFeeds];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def createFeed()
	{
		[:];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveFeed()
	{
		CalendarFeedSubscription calFeed = new CalendarFeedSubscription();
		
		User currentUser = userService.getLoggedInUser();
		
		calFeed.owner = currentUser;
		calFeed.url = params.calFeedUrl;
		calFeed.name = params.calFeedName;
		
		calendarFeedSubscriptionService.saveSubscription(calFeed);
		
		redirect( controller:"calendar", action:"index");
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def editFeed()
	{
		CalendarFeedSubscription calFeedToEdit = null;
		
		Long calFeedId = Long.parseLong( params.calFeedId );
		
		calFeedToEdit = calendarFeedSubscriptionService.findById( calFeedId);

		[calFeedToEdit:calFeedToEdit];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def updateFeed()
	{
		Long calFeedId = Long.parseLong( params.calFeedId );		
		
		CalendarFeedSubscription calFeed = calendarFeedSubscriptionService.findById( calFeedId);

		calFeed.url = params.calFeedUrl;
		calFeed.name = params.calFeedName;
		
		calendarFeedSubscriptionService.saveSubscription( calFeed );
		
		redirect( controller:"calendar", action:"index");
	}
	
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def display()
	{
		[:];	
	}
}