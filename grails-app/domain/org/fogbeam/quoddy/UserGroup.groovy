package org.fogbeam.quoddy

import java.util.Date;

class UserGroup implements Serializable
{
	
	public UserGroup()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	static constraints = 
	{	
		description(nullable:true);	
	}
	
	String 	name;
	String 	uuid;
	String 	description;
	Boolean requireJoinConfirmation = false;
	User 	owner;
	Date 	dateCreated;
	
	
	static hasMany = [groupMembers:User];
}
