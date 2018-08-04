package org.fogbeam.quoddy.jaxrs

/* this will eventually replace the existing EventSubscriptionCollectionResource and EventSubscriptionResource
 * but for now we just want a single method to create an EventSubscription without risking breaking the existing
 * API since the Hatteras engine depends on it.
 */

// import static org.grails.jaxrs.response.Responses.*
import groovy.json.JsonSlurper
import static org.grails.plugins.jaxrs.response.Responses.*;
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

import org.fogbeam.quoddy.subscription.BusinessEventSubscription

@Path( "api/subscription/businessevent" )
class BusinessEventSubscriptionResource 
{
	def businessEventSubscriptionService;
	def userService;
	
	@POST
	@Consumes("application/json")
	@Produces("text/plain")
	public Response addBusinessEventSubscription( final String inputData )
	{
		
		println "inputData: \n ${inputData}";
		log.info( "inputData:\n ${inputData}");

		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);
		
		
		BusinessEventSubscription subscriptionToCreate = new BusinessEventSubscription();
		subscriptionToCreate.name = jsonObject.subscriptionName;
		subscriptionToCreate.description = jsonObject.subscriptionDescription;
		subscriptionToCreate.xQueryExpression = jsonObject.xQueryExpression;
		
		
		// UserService userService = grailsApplication.mainContext.getBean('userService');
		def user = userService.findUserByUserId( jsonObject.userId );
		subscriptionToCreate.owner = user;

		businessEventSubscriptionService.saveSubscription( subscriptionToCreate );
		
		
		ok( "OK" );
	}
}
