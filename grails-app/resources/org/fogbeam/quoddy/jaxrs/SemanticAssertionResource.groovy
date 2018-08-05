package org.fogbeam.quoddy.jaxrs

// import static org.grails.jaxrs.response.Responses.*
import static org.grails.plugins.jaxrs.response.Responses.*;
import groovy.json.JsonSlurper

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status

@Path( "/api/semantic/assertion" )
class SemanticAssertionResource {

	def jenaService;
	
	public void insertSingleAssertion( def jsonObject )
	{
		String subject = jsonObject.subject;
		String predicate = jsonObject.predicate;
		String object = jsonObject.object;
		String objectType = jsonObject.objectType;
		
		log.info( "object: \"${object}\"");
		
		if( objectType.equals("literal"))
		{
			log.info( "saving with Literal object");
			jenaService.saveStatementWithLiteralObject( subject, predicate, object );
		}
		else if( objectType.equals( "resource"))
		{
			log.info( "saving with Resource object");
			jenaService.saveStatementWithResourceObject( subject, predicate, object );
		}
		else
		{
			throw new RuntimeException( "invalid objectType: ${objectType}");
		}
	}	
	
	/* add an arbitrary Statement to the Jena store.  This mainly exists for setting up demos
	 * and what-not.
	 */
	
	@POST
	@Produces( "text/plain" )
	@Consumes( "application/json" )
	public Response addSemanticAssertion( String inputData )
	{
		
		println "inputData: \n ${inputData}";
		log.info( "inputData:\n ${inputData}");

		JsonSlurper jsonSlurper = new JsonSlurper();
		def jsonObject = jsonSlurper.parseText(inputData);
		
		
		if( jsonObject instanceof Map )
		{
			println "single object found";
			log.info( "single object found" );
			insertSingleAssertion( jsonObject );
		}
		else if( jsonObject instanceof List )
		{
			println "list found";
			log.info( "list found");

			for( Object singleSubscription : jsonObject )
			{
				insertSingleAssertion( singleSubscription );
			}
		}
		
		
		ok( "OK " );
	}	
}
