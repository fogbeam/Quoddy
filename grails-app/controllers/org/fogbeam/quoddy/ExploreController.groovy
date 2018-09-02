package org.fogbeam.quoddy

import static groovyx.net.http.ContentType.TEXT

import grails.plugin.springsecurity.annotation.Secured
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
// import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH;

class ExploreController
{

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
	def index()
	{
		return [];
	}
	
    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
	def enrich()
	{
		log.debug( "params:\n " + params);
		
		def content = params.content;
		
		if( params.enrichButton.equals( "Enrich" ))
		{
			// call Stanbol REST API to get enrichment data
			String stanbolServerUrl = grailsApplication.config.urls.stanbol.endpoint;
			log.debug( "using stanbolServerUrl: ${stanbolServerUrl}");
			RESTClient restClient = new RESTClient( stanbolServerUrl );
		
			log.debug(  "content submitted: ${content}");
			def restResponse = restClient.post(	path:'enhancer', 
											body: content,
											requestContentType : TEXT );
		
			Object restResponseData = restResponse.getData();
			
			log.debug(  "restResponseData.class: ${restResponseData.class}");
			
			java.util.Scanner s = new java.util.Scanner(restResponseData).useDelimiter("\\A");
	
			String restResponseText = s.next();
			
			log.debug( "using Scanner: ${restResponseText}");
															
			log.debug( "\n\n*************************************************");
			log.debug( restResponseText.toString());
			log.debug("*************************************************\n\n");
		
		
			[content: content, value:restResponseText];
		}
		else
		{
			render(view:"normal",model:[content:content]);
		}	
	}	
}
