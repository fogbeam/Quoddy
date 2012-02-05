package org.fogbeam.quoddy

class CalendarController
{
	def userService;
	
	def index =
	{
		List<CalendarFeed> calFeeds = new ArrayList<CalendarFeed>();
		User user = null;
		if( session.user != null )
		{
			println "Found User in Session";
			user = userService.findUserByUserId( session.user.userId );
			def queryResults = CalendarFeed.executeQuery( "select calfeed from CalendarFeed as calfeed where calfeed.owner = :owner", [owner:user] );
			
			calFeeds.addAll( queryResults ); 
		}
		else
		{
			println "No user in Session";
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
			CalendarFeed calFeed = new CalendarFeed();
			
			User user = userService.findUserByUserId( session.user.userId );
			calFeed.owner = user;
			calFeed.url = params.calFeedUrl;
			calFeed.name = params.calFeedName;
			
			if( !calFeed.save() )
			{
				println( "Saving CalendarFeed FAILED");
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
		CalendarFeed calFeedToEdit = null;
		if( session.user != null )
		{
			def calFeedId = params.calFeedId;
			calFeedToEdit = CalendarFeed.findById( params.calFeedId);
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
			CalendarFeed calFeed = CalendarFeed.findById( params.calFeedId);
			
			calFeed.url = params.calFeedUrl;
			calFeed.name = params.calFeedName;
			
			if( !calFeed.save() )
			{
				println( "Saving CalendarFeed FAILED");
				calFeed.errors.allErrors.each { println it };
			}
		}
		else
		{
			println "No user in Session";
		}
		
		redirect( controller:"calendar", action:"index");
	}
}