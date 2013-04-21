package org.fogbeam.quoddy.social

class FriendCollection implements Serializable
{
	String ownerUuid;
	Date dateCreated;

	Set<String> friends;	

	static hasMany = [friends:String]
	
}
