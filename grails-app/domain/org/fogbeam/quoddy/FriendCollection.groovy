package org.fogbeam.quoddy

class FriendCollection implements Serializable
{
	String ownerUuid;
	Date dateCreated;

	Set<String> friends;	

	static hasMany = [friends:String]
	
}
