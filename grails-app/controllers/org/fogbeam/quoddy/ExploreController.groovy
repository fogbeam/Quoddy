package org.fogbeam.quoddy

import static groovyx.net.http.ContentType.TEXT
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH;

class ExploreController
{

	def index = 
	{
		
		return [];
	}
	
	def enrich =
	{
		
		println "params:\n " + params;
		
		def content = params.content;
		
		if( params.enrichButton.equals( "Enrich" ))
		{
			// call Stanbol REST API to get enrichment data
			String stanbolServerUrl = CH.config.urls.stanbol.endpoint;
			println "using stanbolServerUrl: ${stanbolServerUrl}";
			RESTClient restClient = new RESTClient( stanbolServerUrl );
		
			println "content submitted: ${content}";
			def restResponse = restClient.post(	path:'enhancer', 
											body: content,
											requestContentType : TEXT );
		
			Object restResponseData = restResponse.getData();
			
			println "restResponseData.class: ${restResponseData.class}";
			
			java.util.Scanner s = new java.util.Scanner(restResponseData).useDelimiter("\\A");
	
			String restResponseText = s.next();
			
			println "using Scanner: ${restResponseText}";
															
			println "\n\n*************************************************";
			println restResponseText.toString();
			println "*************************************************\n\n";
		
		
			// def ks= restResponseText.keySet();
			// ks.each { println it };
		
		
			[content: content, value:restResponseText];
		}
		else
		{
			render(view:"normal",model:[content:content]);
		}
		
	}
		
}
