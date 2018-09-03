package org.fogbeam.quoddy

import java.util.List;

import org.fogbeam.quoddy.stream.ActivityStreamItem;

class UserListService
{
	public UserList findUserListByUuid( final String uuid ) 
	{
		UserList userList = UserList.findByUuid( uuid );
		
		return userList;	
	}
	
	public UserList findById( final Long id, boolean membersEager )
	{
		UserList list = null;
		
		if( membersEager )
		{
			list = UserList.findById( id, [fetch:[members:"eager"]] );
		}
		else
		{
			list = UserList.findById( id );
		}
		
		return list;
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

	public List<ActivityStreamItem> getRecentActivitiesForList( final UserList list, final int maxCount )
	{
		log.debug( "getRecentActivitiesForList: ${list.id} - ${maxCount}");
		
		List<ActivityStreamItem> recentActivities = new ArrayList<ActivityStreamItem>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
	
		log.debug( "Using ${cutoffDate} as cutoffDate" );

	
		List<ActivityStreamItem> queryResults =
			ActivityStreamItem.executeQuery( 
					"select activity from ActivityStreamItem as activity, UserList as ulist where activity.dateCreated >= :cutoffDate " + 
					" and activity.owner in elements(ulist.members) and ulist = :thelist order by activity.dateCreated desc",
              ['cutoffDate':cutoffDate, 'thelist':list], ['max': maxCount ]);
			
		if( queryResults )
		{
			log.debug( "adding ${queryResults.size()} activities read from DB" );
			recentActivities.addAll( queryResults );
		}
	
		return recentActivities;
	}
	
	public UserList save( final UserList listToSave )
	{
		
		if( !listToSave.save(flush:true) )
		{
			log.error( "Saving UserList FAILED" );
			listToSave.errors.allErrors.each { log.error( it.toString() ) };
		}
	
		return listToSave;
	}
	
	public UserList attachAndSave( final UserList listToSave )
	{
		if( !listToSave.isAttached())
		{
			listToSave.attach();
		}
	
		return save( listToSave );
	}
}
