package org.fogbeam.quoddy.jaxrs


import static org.grails.jaxrs.response.Responses.*
import groovy.json.JsonSlurper

import javax.ws.rs.Consumes
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

import org.fogbeam.quoddy.User

@Path('/api/user/annotation')
class UserAnnotationResource 
{
	
	def jenaService;
	def userService;
	
	@PUT
	@Consumes(['application/json'])
	@Produces(['text/plain'])
	public Response addUserAnnotation( String inputData )
	{
		println "inputData: \n ${inputData}";
		log.info( "inputData:\n ${inputData}");

		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);
		
		if( jsonObject instanceof Map )
		{
			println "single object found";
			log.info( "single object found" );
			insertSingleAnnotation( jsonObject );
		}
		else if( jsonObject instanceof List )
		{
			println "list found";
			log.info( "list found");

			for( Object singleAnnotation : jsonObject ) 
			{
				insertSingleAnnotation( singleAnnotation );
			}
		}
		
		ok( "OK" );
	}
	
	private void insertSingleAnnotation( def jsonObject )
	{
		println "inserting annotation: ${jsonObject}";
		log.info( "inserting annotation: ${jsonObject}");

		String userId = jsonObject.userId;
		String annotationPredicate = jsonObject.annotationPredicate;
		String annotationObjectQN = jsonObject.annotationObjectQN;
	
		User user = userService.findUserByUserId( userId );
	
		// [quoddy:test_user_1, http://schema.fogbeam.com/people#hasExpertise, http://customers.fogbeam.com#Acme_Widgets]
		jenaService.addUserAnnotation( user, annotationPredicate, annotationObjectQN );
		
	}
		
}
