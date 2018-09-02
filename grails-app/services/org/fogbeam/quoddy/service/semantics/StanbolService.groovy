package org.fogbeam.quoddy.service.semantics

import static groovyx.net.http.ContentType.TEXT

import org.springframework.beans.factory.annotation.Autowired

import grails.core.GrailsApplication
import groovy.json.JsonBuilder
import groovyx.net.http.RESTClient

public class StanbolService 
{
	@Autowired
	GrailsApplication grailsApplication;
	
	
	public String getEnhancementData( final String content )
	{
		String enhancementJson = "";
		
		// call Stanbol REST API to get enrichment data
		String stanbolServerUrl = grailsApplication.config.urls.stanbol.endpoint;
		log.debug( "using stanbolServerUrl: ${stanbolServerUrl}");
		
		RESTClient restClient = new RESTClient( stanbolServerUrl );
		
		try
		{
			log.debug( "content submitted to Stanbol: ${content}");
			def restResponse = restClient.post(	path:'enhancer',
										body: content,
										requestContentType : TEXT );
									
			Object restResponseData = restResponse.getData();
		
			if( restResponseData instanceof InputStream )
			{
		
				log.debug( "restResponseData.class: ${restResponseData.class}");
		
				java.util.Scanner s = new java.util.Scanner((InputStream)restResponseData).useDelimiter("\\A");

				String restResponseText = s.next();
		
				log.debug( "using Scanner: ${restResponseText}");
				log.info( "got InputStream, using Scanner to extract" );
				log.info( "restResponseText:\n\n ${restResponseText}\n\n");
		
				enhancementJson = restResponseText;
		
			}
			else if( restResponseData instanceof net.sf.json.JSONObject )
			{
				String restResponseText = restResponseData.toString();
				log.info( "got JSONObject, using toString() to extract" );
				log.info( "restResponseText:\n\n ${restResponseText}\n\n");
				enhancementJson = restResponseText;
			}
			else if( restResponseData instanceof java.lang.String )
			{
				log.info( "got String, no extraction required" );
				log.info( "restResponseData:\n\n ${restResponseData}\n\n");
				enhancementJson = new String( restResponseData );
			}
			else
			{
				log.info( "got Other (${restResponseData.getClass().getName()}), using toString() to extract" );
				String restResponseText = new JsonBuilder(restResponseData).toPrettyString();
				log.info( "restResponseText:\n\n ${restResponseText}\n\n");
				enhancementJson = restResponseText;
			}
		}
		catch( ConnectException ce )
		{
			log.error( "Could not connect to Stanbol server: ", ce );
		}
		catch( Exception e )
		{
			log.error( "Error doing semantic enhancement: ", e );
		}
		
		return enhancementJson;
	}
}
