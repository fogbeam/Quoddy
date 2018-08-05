package org.fogbeam.quoddy.jaxrs

// import static org.grails.jaxrs.response.Responses.*
import static org.grails.plugins.jaxrs.response.Responses.*;
import groovy.json.JsonSlurper

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

import grails.core.GrailsClass
import org.quartz.Trigger
import org.quartz.impl.triggers.SimpleTriggerImpl

@Path( "/api/scheduledjob/trigger" )
class TriggerResource 
{
	def grailsApplication;

	public void insertSingleTrigger( def jsonObject )
	{
		String jobGroup = jsonObject.jobGroup;
		String jobName = jsonObject.jobName;
		
		String triggerName = jsonObject.triggerName;
		String triggerGroup = jsonObject.triggerGroup; 
		int repeatCount = Integer.parseInt(jsonObject.repeatCount);
		long recurrenceInterval = Long.parseLong(jsonObject.recurrenceInterval);
		
		
		log.info( "adding Trigger for jobName: ${jobName}" );

		
		GrailsClass jobClass = grailsApplication.getArtefact( "Job", jobName );
		
		if( jobClass == null )
		{
			log.error( "Could not load GrailsClass for ${jobName}" );
			throw new RuntimeException( "Could not load GrailsClass for ${jobName}" );
			// return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		else
		{
			// SimpleTrigger(String name, String group, int repeatCount, long repeatInterval)
			
			Trigger trigger = new SimpleTriggerImpl( triggerName, triggerGroup, repeatCount, recurrenceInterval);
			jobClass.newInstance().schedule( trigger );
		}
		
	}
		
	
	@POST
	@Consumes( "application/json")
	@Produces( "text/plain")
	public Response addTrigger( String inputData ) 
	{
		println "inputData: \n ${inputData}";
		log.info( "inputData:\n ${inputData}");

		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);

		if( jsonObject instanceof Map )
		{
			println "single object found";
			log.info( "single object found" );
			insertSingleTrigger( jsonObject );
		}
		else if( jsonObject instanceof List )
		{
			println "list found";
			log.info( "list found");

			for( Object singleSubscription : jsonObject )
			{
				insertSingleTrigger( singleSubscription );
			}
		}
		
		ok( "OK" );
	}
	
}
