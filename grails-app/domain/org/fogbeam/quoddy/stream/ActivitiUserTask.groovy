package org.fogbeam.quoddy.stream

import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription

class ActivitiUserTask extends StreamItemBase
{
	// createTime:2013-05-26T04:13:49CDT, 
	// subTaskList:[], 
	// executionId:8301, 
	// attachmentList:[], 
	// assignee:null, id:8305, 
	// identityLinkList:[], 
	// formResourceKey:null, 
	// processInstanceId:8301, 
	// description:null, 
	// priority:50, 
	// taskDefinitionKey:usertask1, 
	// name:Do Frozgibbit, 
	// owner:null, 
	// dueDate:null, 
	// parentTaskId:null, 
	// processDefinitionId:process1:4:5304, 
	// delegationState:null
	
	static constraints = {
		createTime(nullable:true);
		executionId(nullable:true);
		// attachmentList
		assignee(nullable:true);
		processInstanceId(nullable:true);
		description(nullable:true);
		priority(nullable:true);
		remoteName(nullable:true);
		remoteOwner(nullable:true);
		dueDate(nullable:true);
		
	}
	
	static transients = ['templateName'];
		
	public String getTemplateName()
	{
		return "/renderActivitiUserTask";
	}
	
	
	String createTime;
	String executionId;
	// attachmentList
	String assignee;
	String processInstanceId;
	String description;
	String priority;
	String taskId;
	String remoteName;
	String remoteOwner;
	String dueDate;
	Map<String, String> variables;
	
	

}
