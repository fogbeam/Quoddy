package org.fogbeam.quoddy.jaxrs


// import static org.grails.jaxrs.response.Responses.*
import static org.grails.plugins.jaxrs.response.Responses.*;

import groovy.json.JsonSlurper

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription

@Path( "/api/subscription/activiti" )
class ActivitiBPMSubscriptionResource {


	def activitiUserTaskSubscriptionService;
	def userService;
	
	public void insertSingleSubscription( def jsonObject ) 
	{
		ActivitiUserTaskSubscription subscriptionToCreate = new ActivitiUserTaskSubscription();
		subscriptionToCreate.name = jsonObject.subscriptionName;
		subscriptionToCreate.description = jsonObject.subscriptionDescription;
		subscriptionToCreate.activitiServer = jsonObject.activitiServer;
		subscriptionToCreate.candidateGroup = jsonObject.candidateGroup;
		subscriptionToCreate.assignee = jsonObject.assignee;
   
		def user = userService.findUserByUserId( jsonObject.userId );
		subscriptionToCreate.owner = user;
	
		activitiUserTaskSubscriptionService.saveSubscription( subscriptionToCreate );

	}

	@POST
	@Consumes( "application/json")
	@Produces( "text/plain")
	public Response createNewActivitiBPMSubscription( String inputData )
	{
		
		println "inputData: \n ${inputData}";
		log.info( "inputData:\n ${inputData}");

		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);

		if( jsonObject instanceof Map )
		{
			println "single object found";
			log.info( "single object found" );
			insertSingleSubscription( jsonObject );
		}
		else if( jsonObject instanceof List )
		{
			println "list found";
			log.info( "list found");

			for( Object singleSubscription : jsonObject ) 
			{
				insertSingleSubscription( singleSubscription );
			}
		}
		
		ok( "OK" );
	}

}
