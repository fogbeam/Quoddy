package org.fogbeam.quoddy.social

class FriendRequestCollection 
{
	String ownerUuid;
	Date dateCreated;
	
	Set<String> friendRequests;

	static hasMany = [friendRequests:String]
	
}
