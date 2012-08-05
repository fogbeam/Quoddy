package org.fogbeam.quoddy

import java.util.List;

class UserListService
{
	public UserList findUserListByUuid( final String uuid ) 
	{
		UserList userList = UserList.findByUuid( uuid );
		
		return userList;	
	}
	
	public List<UserList> getListsForUser( final User user )
	{
		List<UserList> lists = new ArrayList<UserList>();
		
		List<UserList> tempLists = UserList.executeQuery( "select list from UserList as list where list.owner = :owner",
														  ['owner':user] );
		if( tempLists )
		{
			lists.addAll( tempLists );
		}
		
		return lists;	
	}
	
	public List<UserList> getListsForUser( final User user, final int maxCount )
	{
		List<UserList> lists = new ArrayList<UserList>();

		List<UserList> tempLists = UserList.executeQuery( "select list from UserList as list where list.owner = :owner",
														  ['owner':user], ['max':maxCount] );

		if( tempLists )
		{
			lists.addAll( tempLists );
		}
											  
													  		
		return lists;
	}

	public List<User> getEligibleUsersForList( final UserList list )
	{
		List<User> eligibleUsers = new ArrayList<User>();
		
		def queryResults = User.executeQuery( "select user from User as user, UserList as list where user not in elements(list.members) and user <> list.owner" );
		
		eligibleUsers.addAll( queryResults ); 
		
		return eligibleUsers;
			
	}	

	public List<Activity> getRecentActivitiesForList( final UserList list, final int maxCount )
	{
		println "getRecentActivitiesForList: ${list.id} - ${maxCount}";
		
		List<Activity> recentActivities = new ArrayList<Activity>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -600 );
		Date cutoffDate = cal.getTime();
	
		println "Using ${cutoffDate} as cutoffDate";

	
		List<Activity> queryResults =
			Activity.executeQuery( 
					"select activity from Activity as activity, UserList as ulist where activity.dateCreated >= :cutoffDate " + 
					" and activity.owner in elements(ulist.members) and ulist = :thelist order by activity.dateCreated desc",
              ['cutoffDate':cutoffDate, 'thelist':list], ['max': maxCount ]);
			
		if( queryResults )
		{
			println "adding ${queryResults.size()} activities read from DB";
			recentActivities.addAll( queryResults );
		}
	
		return recentActivities;
	}
}
