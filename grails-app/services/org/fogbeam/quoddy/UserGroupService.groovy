package org.fogbeam.quoddy

import java.util.List

import org.fogbeam.quoddy.stream.ActivityStreamItem;
import org.fogbeam.quoddy.stream.StreamItemBase;

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

	public UserGroup findUserGroupByName( final String name )
	{
		UserGroup group = UserGroup.findByName( name );
		
		return group;
	}
		
	public UserGroup save( UserGroup userGroup )
	{
		if( !userGroup.save(flush:true) )
		{
			log.error( "Failed to save UserGroup" );
			
			userGroup.errors.allErrors.each { log.error( it.toString() ) }
			
		}		
		
		
		return userGroup;
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
	
	public List<ActivityStreamItem> getRecentActivitiesForGroup( final UserGroup group, final int maxCount )
	{
		log.debug( "getRecentActivitiesForGroup: ${group.id} - ${maxCount}");
			
		List<ActivityStreamItem> recentActivities = new ArrayList<ActivityStreamItem>();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
		
		log.debug( "Using ${cutoffDate} as cutoffDate");

		
		List<ActivityStreamItem> queryResults =
			ActivityStreamItem.executeQuery( "select activity from ActivityStreamItem as activity where activity.dateCreated >= :cutoffDate and activity.targetUuid = :targetUuid order by activity.dateCreated desc",
			['cutoffDate':cutoffDate, 'targetUuid':group.uuid], ['max': maxCount ]);

		if( queryResults )
		{
			log.debug( "adding ${queryResults.size()} activities read from DB");
			recentActivities.addAll( queryResults );	
		}
		else
		{
            log.debug( "no Activities read from DB!");
        }
		
		return recentActivities;
				
	}
	
	
	/*
	public List<StreamItemBase> getRecentEventsForGroup( final UserGroup group, final int maxCount )
	{
		log.debug( "getRecentEventsForGroup: ${group.id} - ${maxCount}");
			
		List<StreamItemBase> recentEvents = new ArrayList<StreamItemBase>();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
		
		log.debug( "Using ${cutoffDate} as cutoffDate");

		
		List<StreamItemBase> queryResults =
			StreamItemBase.executeQuery( "select event from StreamItemBase as event where event.dateCreated >= :cutoffDate and event.targetUuid = :targetUuid order by event.dateCreated desc",
			['cutoffDate':cutoffDate, 'targetUuid':group.uuid], ['max': maxCount ]);

		if( queryResults )
		{
			log.debug( "adding ${queryResults.size()} activities read from DB");
			recentEvents.addAll( queryResults );
		}
		
		
		return recentEvents;
	}
	*/
	
	
}
