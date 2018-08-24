package org.fogbeam.quoddy



import groovyx.net.http.RESTClient
import net.fortuna.ical4j.model.component.*

import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.protocol.HttpContext
import org.fogbeam.quoddy.stream.ActivitiUserTask
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.constants.EventTypes
import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription


class UpdateActivitiUserTaskSubscriptionsJob 
{
	
	def group = "MyGroup";
	def volatility = false;
	def jmsService;
	def eventStreamService;
	
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
			
			// adding server authentication, if required
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
										
					
					// and, later, comments and attachments for the User Task
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
					
					
					// make a call to retrieve "variables" for the process instance
					// String path = "task/${taskId}/variables";
					String path = "process-instance/${it.processInstanceId}";
					println "getting variables for path: " + path;
					def varsResponse = restClient.get(path:path);
					
					// println "****************\n\nvarsResponse\n${varsResponse.data}\n\n**************************";
					
					Map<String, String> vars = new HashMap<String, String>();
					
					varsResponse.data.variables.each {
						vars.put( it.variableName, it.variableValue );
					}
					
					userTask.variables = vars;
					
					
					if( !userTask.save() )
					{
						// print errors...	
						println( "Saving CalendarEvent FAILED");
						userTask.errors.allErrors.each { println it };
					}
					else
					{ 
						// if userTask save is successful...
						
						println "successfully saved ActivitiUserTask.";
						
						ActivityStreamItem activity = new ActivityStreamItem(content:"fuckme");
						
						activity.title = "ActivitiUserTask Received";
						activity.url = new URL( "http://example.com/" );
						activity.verb = "activiti_user_task_received";
						activity.actorObjectType = "ActivitiUserTaskSubscription";
						activity.actorUuid = owningSubscription.uuid;
						activity.targetObjectType = "STREAM_PUBLIC";
						activity.published = new Date(); // set published to "now"
						activity.targetUuid = streamPublic.uuid;
						activity.owner = owner;
						activity.streamObject = userTask;
						activity.objectClass = EventTypes.ACTIVITI_USER_TASK.name;
						
								
						// NOTE: we added "name" to StreamItemBase, but how is it really going
						// to be used?  Do we *really* need this??
						activity.name = activity.title;
						
						// activity.effectiveDate = activity.published;
						
						eventStreamService.saveActivity( activity );
						
						
						def newContentMsg = [msgType:'NEW_ACTIVITI_USER_TASK', activityId:activity.id, activityUuid:activity.uuid ];
						
						println "sending messages to JMS";
						
						// send message to request search indexing
						jmsService.send( queue: 'quoddySearchQueue', newContentMsg, 'standard', null );
						
		
										
						// TODO: send JMS message for UI notifications

												
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