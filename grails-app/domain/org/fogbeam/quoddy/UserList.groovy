package org.fogbeam.quoddy

import java.util.Date;

class UserList implements Serializable
{
	
	public UserList()
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
	User 	owner;
	Date 	dateCreated;
	
	static hasMany = [members:User];
	
}
