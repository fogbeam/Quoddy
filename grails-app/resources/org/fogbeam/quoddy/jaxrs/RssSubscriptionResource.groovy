package org.fogbeam.quoddy.jaxrs

import static org.grails.jaxrs.response.Responses.*
import groovy.json.JsonSlurper

import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response


import org.fogbeam.quoddy.subscription.RssFeedSubscription

@Path( "api/subscription/rssfeed" )
class RssSubscriptionResource 
{
	
	def userService;
	def rssFeedSubscriptionService;
	
	public void insertSingleSubscription( def jsonObject ) 
	{
				
		RssFeedSubscription subscriptionToCreate = new RssFeedSubscription();
		subscriptionToCreate.name = jsonObject.subscriptionName;
		subscriptionToCreate.url = jsonObject.subscriptionUrl;
		
		def user = userService.findUserByUserId( jsonObject.userId );
		subscriptionToCreate.owner = user;
	
		rssFeedSubscriptionService.saveSubscription( subscriptionToCreate );
	}
	
	@POST
	public Response createNewRssFeedSubscription( final String inputData )
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
