package org.fogbeam.quoddy

import java.util.Date;

class UserGroup
{
	static constraints = 
	{	
		description(nullable:true);	
	}
	
	String 	name;
	String 	description;
	Boolean requireJoinConfirmation = false;
	User 	owner;
	Date 	dateCreated;
	
	
	static hasMany = [groupMembers:User];
}
