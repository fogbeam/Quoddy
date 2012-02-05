package org.fogbeam.quoddy


import java.text.SimpleDateFormat 


class ReapOldCalendarEventsJob 
{
	
	def group = "MyGroup";
	def volatility = false;
	
	static triggers = {
	}
	
    def execute() 
	{
     	
		Date now = new Date();
		SimpleDateFormat sdf = SimpleDateFormat.getDateTimeInstance();
		
		println( "TRIGGER: Reaping old CalendarEvent instances: ${sdf.format( now )}" );

	}
}