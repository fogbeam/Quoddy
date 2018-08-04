package org.fogbeam.quoddy.jaxrs

// import static org.grails.jaxrs.response.Responses.*
import static org.grails.plugins.jaxrs.response.Responses.*;
import groovy.json.JsonSlurper

import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response


import org.fogbeam.quoddy.subscription.CalendarFeedSubscription


@Path( "/api/subscription/calendar" )
class ICalSubscriptionResource 
{
	
	def calendarFeedSubscriptionService;
	def userService;
	
	
	public void insertSingleSubscription( def jsonObject )
	{
		
		CalendarFeedSubscription calendarFeedSubscriptionToCreate = new CalendarFeedSubscription();
		
		calendarFeedSubscriptionToCreate.url = jsonObject.calFeedUrl;
		calendarFeedSubscriptionToCreate.name = jsonObject.calFeedName;
   
		def user = userService.findUserByUserId( jsonObject.userId );
		calendarFeedSubscriptionToCreate.owner = user;
	
		
		calendarFeedSubscriptionService.saveSubscription( calendarFeedSubscriptionToCreate )

	}
	
	@POST
	public Response addNewCalendarFeedSubscription( final String inputData )
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
