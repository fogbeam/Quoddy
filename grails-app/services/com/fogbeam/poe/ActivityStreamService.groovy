package com.fogbeam.poe

class ActivityStreamService {

	public List<Activity> getRecentFriendActivitiesForUser( User user )
	{
		List<Activity> recentActivities = new ArrayList<Activity>();
		
		Activity aOne = new Activity( text: "Joe did something" );
		Activity aTwo = new Activity( text: "Mary did something" );
		
		recentActivities.add( aOne );
		recentActivities.add( aTwo );
		
		return recentActivities;
	}

	public void saveActivity( Activity activity )
	{
		println "about to save activity...";
		if( !activity.save() )
		{
			println( "Saving activity FAILED");
			activity.errors.allErrors.each { println it };
		}
	}
}
