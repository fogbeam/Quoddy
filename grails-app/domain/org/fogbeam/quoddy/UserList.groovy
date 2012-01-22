package org.fogbeam.quoddy

import java.util.Date;

class UserList
{
	static constraints = 
	{
		description(nullable:true);
	}
	
	String 	name;
	String 	description;
	User 	owner;
	Date 	dateCreated;
	
	static hasMany = [members:User];
	
}
