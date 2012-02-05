package org.fogbeam.quoddy


import java.text.SimpleDateFormat 


class UpdateCalendarFeedsJob 
{
	
	def group = "MyGroup";
	def volatility = false;
	
	static triggers = {
	}
	
    def execute() 
	{
     	// get a list of all the active CalendarFeed objects
		
		// for each feed:
		
			// download the contents of the feed, and parse out all of the VEVENTs
		
			// for each VEVENT create a CalendarEvent instance with the CalendarFeed owner
			// as the ownere of the VEVENT
		
	}
}