package org.fogbeam.quoddy.jaxrs




import static org.grails.jaxrs.response.Responses.*
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
	
	
	@POST
	public Response addNewCalendarFeedSubscription( final String inputData )
	{
		
		println "inputData: \n ${inputData}";
		log.info( "inputData:\n ${inputData}");

		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);
		
		
		CalendarFeedSubscription calendarFeedSubscriptionToCreate = new CalendarFeedSubscription();
		
		calendarFeedSubscriptionToCreate.url = jsonObject.calFeedUrl;
		calendarFeedSubscriptionToCreate.name = jsonObject.calFeedName;
   
		def user = userService.findUserByUserId( jsonObject.userId );
		calendarFeedSubscriptionToCreate.owner = user;
	
		
		calendarFeedSubscriptionService.saveSubscription( calendarFeedSubscriptionToCreate )
		
		ok( "OK" );
	}
	
}
