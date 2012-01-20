package org.fogbeam.quoddy

import java.util.List;

class UserGroupService
{
	public List<UserGroup> getGroupsForUser( final User user )
	{
		List<UserGroup> groups = new ArrayList<UserGroup>();
		
		List<UserGroup> tempGroups = UserGroup.executeQuery( "select group from UserGroup as group where group.owner = :owner",
														  ['owner':user] );
		if( tempGroups )
		{
			groups.addAll( tempGroups );
		}
		
		return groups;
	}		

	public List<UserGroup> getListsForUser( final User user, final int maxCount )
	{
		List<UserGroup> groups = new ArrayList<UserGroup>();

		List<UserGroup> tempGroups = UserGroup.executeQuery( "select group from UserGroup as group where group.owner = :owner",
														  ['owner':user], ['max':maxCount] );

		if( tempGroups )
		{
			groups.addAll( tempGroups );
		}
											  										  
		return groups;
	}
	
}
