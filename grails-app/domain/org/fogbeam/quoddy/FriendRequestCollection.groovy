package org.fogbeam.quoddy

class FriendRequestCollection 
{
	String ownerUuid;
	Date dateCreated;
	
	Set<String> friendRequests;

	static hasMany = [friendRequests:String]
	
}
