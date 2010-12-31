package org.fogbeam.quoddy

class FriendCollection 
{
	String ownerUuid;
	Date dateCreated;

	Set<String> friends;	

	static hasMany = [friends:String]
	
}
