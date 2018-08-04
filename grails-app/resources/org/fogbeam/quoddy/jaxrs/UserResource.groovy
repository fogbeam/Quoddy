package org.fogbeam.quoddy.jaxrs

import groovy.json.JsonSlurper

import javax.ws.rs.POST
import javax.ws.rs.Path
import static org.grails.plugins.jaxrs.response.Responses.*;
import org.fogbeam.quoddy.User

@Path( "api/user" )
class UserResource 
{
	def userService;
	
	@POST
	public void createUser( final String inputData )
	{
		println "inputData: \n ${inputData}";
		log.info( "inputData:\n ${inputData}");

		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);
		
		
		if( jsonObject instanceof Map )
		{
			println "single object found";
			log.info( "single object found" );
			insertSingleUser( jsonObject );
		}
		else if( jsonObject instanceof List )
		{
			println "list found";
			log.info( "list found");

			for( Object singleUser : jsonObject )
			{
				insertSingleUser( singleUser );
			}
		}
	}
	
	
	def insertSingleUser( def jsonObject )
	{
		User user = new User();
		
		user.userId = jsonObject.userId;
		user.homepage = jsonObject.homepage;
		user.firstName = jsonObject.firstName;
		user.lastName = jsonObject.lastName;
		user.email = jsonObject.email;
		user.bio = jsonObject.bio;
		user.password = jsonObject.password; // TODO; don't allow this to be passed.
											 // always assume SSO, OR autogenerate a 
											 // one-time-use password and send it to the user
		
		userService.createUser( user );
	}
}
