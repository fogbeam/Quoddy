package org.fogbeam.quoddy


import java.text.SimpleDateFormat 


class DummyJob 
{
	
	def group = "MyGroup";
	def volatility = false;
	
	static triggers = {
	}
	
    def execute() 
	{
     	
		Date now = new Date();
		SimpleDateFormat sdf = SimpleDateFormat.getDateTimeInstance();
		
		// println( "TRIGGER: sending rebuild cache message: ${sdf.format( now )}" );

	}
}