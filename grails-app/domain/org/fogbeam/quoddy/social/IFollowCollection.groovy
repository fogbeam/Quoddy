package org.fogbeam.quoddy.social;

public class IFollowCollection implements Serializable
{
	String ownerUuid;
	Date dateCreated;
	
	Set<String> IFollow;
	
	static hasMany = [IFollow:String];
	
}
