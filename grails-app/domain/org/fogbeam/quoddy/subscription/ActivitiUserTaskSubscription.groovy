package org.fogbeam.quoddy.subscription

import java.io.Serializable;

import org.fogbeam.quoddy.User;

class ActivitiUserTaskSubscription extends BaseSubscription implements Serializable
{
	static constraints = {
		activitiServer( nullable:false)
		candidateGroup(nullable:true)
		assignee( nullable:true)
	}

	String 	activitiServer;
	String 	candidateGroup;
	String 	assignee;
	
	
}
