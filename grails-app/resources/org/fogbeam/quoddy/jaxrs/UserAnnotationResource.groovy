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
		
		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);
		
		if( jsonObject instanceof Map )
		{
			insertSingleAnnotation( jsonObject );
		}
		else if( jsonObject instanceof List )
		{
			for( Object singleAnnotation : jsonObject ) 
			{
				insertSingleAnnotation( singleAnnotation );
			}
		}
		
		ok( "OK" );
	}
	
	private void insertSingleAnnotation( def jsonObject )
	{
		
		String userId = jsonObject.userId;
		String annotationPredicate = jsonObject.annotationPredicate;
		String annotationObjectQN = jsonObject.annotationObjectQN;
	
		User user = userService.findUserByUserId( userId );
	
		// [quoddy:test_user_1, http://schema.fogbeam.com/people#hasExpertise, http://customers.fogbeam.com#Acme_Widgets]
		jenaService.addUserAnnotation( user, annotationPredicate, annotationObjectQN );
		
	}
		
}
