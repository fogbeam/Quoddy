package org.fogbeam.quoddy;

public class IFollowCollection implements Serializable
{
	String ownerUuid;
	Date dateCreated;
	
	Set<String> iFollow;
	
	static hasMany = [iFollow:String];
	
}
