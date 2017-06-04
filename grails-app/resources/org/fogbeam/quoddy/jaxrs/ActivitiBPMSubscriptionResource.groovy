package org.fogbeam.quoddy.jaxrs


import static org.grails.jaxrs.response.Responses.*
import groovy.json.JsonSlurper

import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response

import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription

@Path( "api/subscription/activiti" )
class ActivitiBPMSubscriptionResource {


	def activitiUserTaskSubscriptionService;
	
	
	@POST
	public Response createNewActivitiBPMSubscription( String inputData )
	{
		
		println "inputData: \n ${inputData}";
		log.info( "inputData:\n ${inputData}");

		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);

		
		ActivitiUserTaskSubscription subscriptionToCreate = new ActivitiUserTaskSubscription();
		subscriptionToCreate.name = jsonObject.subscriptionName;
		subscriptionToCreate.description = jsonObject.subscriptionDescription;
		subscriptionToCreate.activitiServer = jsonObject.activitiServer;
		subscriptionToCreate.candidateGroup = jsonObject.candidateGroup;
		subscriptionToCreate.assignee = jsonObject.assignee;
   
		def user = userService.findUserByUserId( jsonObject.userId );
		subscriptionToCreate.owner = user;
	
		activitiUserTaskSubscriptionService.saveSubscription( subscriptionToCreate );
			
		
		ok( "OK" );
	}

}
