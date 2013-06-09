package org.fogbeam.quoddy



import groovyx.net.http.RESTClient
import net.fortuna.ical4j.model.component.*

import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.protocol.HttpContext
import org.fogbeam.quoddy.stream.ActivitiUserTask
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription


class UpdateActivitiUserTaskSubscriptionsJob 
{
	
	def group = "MyGroup";
	def volatility = false;
	def jmsService;
	
	static triggers = {
	}
	
    def execute() 
	{
		
		ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
		
		
		println "executing UpdateActivitiUserTaskSubscriptionsJob";
		
     	// get a list of all the active CalendarFeedSubscription objects
		List<ActivitiUserTaskSubscription> allSubscriptions = ActivitiUserTaskSubscription.findAll();
		
		allSubscriptions.each {
			
			ActivitiUserTaskSubscription owningSubscription = it;
			User owner = it.owner;
			// make a REST request to the specified Activiti server for the
			// provided candidate group (or assignee)
			String activitiServer = it.activitiServer;
			println "Making REST request to server at: ${activitiServer}";
			RESTClient restClient = new RESTClient( activitiServer )
			
			restClient.client.addRequestInterceptor(new HttpRequestInterceptor() {
					void process(HttpRequest httpRequest, HttpContext httpContext) {
							httpRequest.addHeader('Authorization', 'Basic ' + 'kermit:kermit'.bytes.encodeBase64().toString())
						}
					});
					
			
			def response = restClient.get(path:'tasks', query:['candidate-group':'admin'])
						
			println "found ${response.data.total} matching tasks!";
			
			response.data.data.each {
			
				
				String taskId = it.id;
				
				// check to see if we already have this one, before trying to save
				// it
				List<ActivitiUserTask> tasks = ActivitiUserTask.executeQuery( "select task from ActivitiUserTask as task where task.taskId = :taskId", [taskId:taskId]  );
				if( tasks == null || tasks.isEmpty())
				{
				
					ActivitiUserTask userTask = new ActivitiUserTask();
					
					userTask.createTime = it.createTime;
					userTask.executionId = it.executionId;
					// attachmentList
					userTask.assignee = it.assignee;
					userTask.processInstanceId = it.processInstanceId;
					userTask.description = it.description;
					userTask.priority = it.priority;
					userTask.name = it.name;
					userTask.remoteName = it.name;
					userTask.remoteOwner = it.owner;
					userTask.dueDate = it.dueDate;
					userTask.taskId = it.id;
					userTask.owner = owner;
					userTask.owningSubscription = owningSubscription;
					userTask.targetUuid  = streamPublic.uuid;
					userTask.effectiveDate = new Date(); // now
					
					if( !userTask.save() )
					{
						// print errors...	
						println( "Saving CalendarEvent FAILED");
						userTask.errors.allErrors.each { println it };
					}
					else
					{
						println "successfully saved ActivitiUserTask.";
					}
				}
				else
				{
					println "We already have this one, SKIPPING...";	
				}
			}
			
			
			// send JMS message for UI notifications
			
			// send JMS message for indexing and additional processing
			// def msg = [ id:event.id, uuid:event.uuid, msgType:'NEW_CALENDAR_FEED_ITEM'];
			// jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		}
		
	}
}