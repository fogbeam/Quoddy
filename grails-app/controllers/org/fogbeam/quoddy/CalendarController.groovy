package org.fogbeam.quoddy

import org.fogbeam.quoddy.subscription.CalendarFeedSubscription;

class CalendarController
{
	def userService;
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
	
	def index =
	{
		List<CalendarFeedSubscription> calFeeds = new ArrayList<CalendarFeedSubscription>();
		User user = null;
		if( session.user != null )
		{
			println "Found User in Session";
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
	
	def createFeed =
	{
		[];
	}
	
	def saveFeed =
	{
		
		if( session.user != null )
		{
			CalendarFeedSubscription calFeed = new CalendarFeedSubscription();
			
			User user = userService.findUserByUserId( session.user.userId );
			calFeed.owner = user;
			calFeed.url = params.calFeedUrl;
			calFeed.name = params.calFeedName;
			
			if( !calFeed.save() )
			{
				println( "Saving CalendarFeedSubscription FAILED");
				calFeed.errors.allErrors.each { println it };
			}
		}
		else
		{
			println "No user in Session";
		}
		
		redirect( controller:"calendar", action:"index");
	}
	
	def editFeed =
	{
		CalendarFeedSubscription calFeedToEdit = null;
		if( session.user != null )
		{
			def calFeedId = params.calFeedId;
			calFeedToEdit = CalendarFeedSubscription.findById( params.calFeedId);
		}
		else
		{
			println "No user in Session";
		}		
		
		[calFeedToEdit:calFeedToEdit];
	}
	
	def updateFeed =
	{
		if( session.user != null )
		{
			CalendarFeedSubscription calFeed = CalendarFeedSubscription.findById( params.calFeedId);
			
			calFeed.url = params.calFeedUrl;
			calFeed.name = params.calFeedName;
			
			if( !calFeed.save() )
			{
				println( "Saving CalendarFeedSubscription FAILED");
				calFeed.errors.allErrors.each { println it };
			}
		}
		else
		{
			println "No user in Session";
		}
		
		redirect( controller:"calendar", action:"index");
	}
	
	def display = {
		[];	
	}
}