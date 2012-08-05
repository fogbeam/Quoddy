package org.fogbeam.quoddy

import java.util.List

class UserGroupService
{
	
	public UserGroup findByGroupId( final Integer groupId )
	{
		UserGroup group = UserGroup.findById( groupId );
		
		return group;	
	}

	public UserGroup findUserGroupByUuid( final String uuid )
	{
		UserGroup group = UserGroup.findByUuid( uuid );
		
		return group;
	}
		
	public List<UserGroup> getGroupsOwnedByUser( final User user )
	{
		List<UserGroup> groups = new ArrayList<UserGroup>();
		
		List<UserGroup> tempGroups = UserGroup.executeQuery( "select thegroup from UserGroup as thegroup where thegroup.owner = :owner",
														  ['owner':user] );
		if( tempGroups )
		{
			groups.addAll( tempGroups );
		}
		
		return groups;
	}		

	public List<UserGroup> getGroupsOwnedByUser( final User user, final int maxCount )
	{
		List<UserGroup> groups = new ArrayList<UserGroup>();

		List<UserGroup> tempGroups = UserGroup.executeQuery( "select thegroup from UserGroup as thegroup where thegroup.owner = :owner",
														  ['owner':user], ['max':maxCount] );

		if( tempGroups )
		{
			groups.addAll( tempGroups );
		}
											  										  
		return groups;
	}

	public List<UserGroup> getGroupsWhereUserIsMember( final User user )
	{
		List<UserGroup> groups = new ArrayList<UserGroup>();
		
		List<UserGroup> tempGroups = UserGroup.executeQuery( "select thegroup from UserGroup as thegroup, User as user where user in elements(thegroup.groupMembers) and user = :theUser", ['theUser':user] );

		if( tempGroups )
		{
			groups.addAll( tempGroups );
		}
		
		
		return groups;
	}
	
	public List<UserGroup> getAllGroups()
	{
		List<UserGroup> groups = new ArrayList<UserGroup>();
		
		List<UserGroup> tempGroups = UserGroup.executeQuery( "from UserGroup" );

		if( tempGroups )
		{
			groups.addAll( tempGroups );
		}
		
		
		return groups;
	}

	// get all groups where the User is the Owner OR is a member
	public List<UserGroup> getAllGroupsForUser( final User user )
	{
		List<UserGroup> groups = new ArrayList<UserGroup>();
		
		List<UserGroup> tempGroups = UserGroup.executeQuery( "select ugroup from UserGroup as ugroup, User as user where user = ? and ( ugroup.owner = user OR user in elements (ugroup.groupMembers))", [user] );

		if( tempGroups )
		{
			groups.addAll( tempGroups );
		}
		
		
		return groups;
	}
	
	public List<Activity> getRecentActivitiesForGroup( final UserGroup group, final int maxCount )
	{
		println "getRecentActivitiesForGroup: ${group.id} - ${maxCount}";
			
		List<Activity> recentActivities = new ArrayList<Activity>();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -600 );
		Date cutoffDate = cal.getTime();
		
		println "Using ${cutoffDate} as cutoffDate";

		
		List<Activity> queryResults =
			Activity.executeQuery( "select activity from Activity as activity where activity.dateCreated >= :cutoffDate and activity.targetUuid = :targetUuid order by activity.dateCreated desc",
			['cutoffDate':cutoffDate, 'targetUuid':group.uuid], ['max': maxCount ]);

		if( queryResults )
		{
			println "adding ${queryResults.size()} activities read from DB";
			recentActivities.addAll( queryResults );	
		}
		
		
		return recentActivities;
				
	}
}
