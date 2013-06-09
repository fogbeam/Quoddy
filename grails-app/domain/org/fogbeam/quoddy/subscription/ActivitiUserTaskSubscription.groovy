package org.fogbeam.quoddy.subscription

import java.io.Serializable;

import org.fogbeam.quoddy.User;

class ActivitiUserTaskSubscription implements Serializable
{
	public ActivitiUserTaskSubscription()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	String 	uuid;
	String 	name;
	String 	description;
	String 	activitiServer;
	String 	candidateGroup;
	String 	assignee;
	User 	owner;
	
	static mapping = {
		owner lazy:false; // eagerly fetch the owner
	}
	
}
