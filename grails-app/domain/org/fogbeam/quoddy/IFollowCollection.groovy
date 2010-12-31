package org.fogbeam.quoddy;

public class IFollowCollection 
{
	String ownerUuid;
	Date dateCreated;
	
	Set<String> iFollow;
	
	static hasMany = [iFollow:String];
	
}
