package org.fogbeam.quoddy

import static groovyx.net.http.ContentType.TEXT
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient

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
			RESTClient restClient = new RESTClient( "http://localhost:8080" )
		
			println "content submitted: ${content}";
			def restResponse = restClient.post(	path:'enhancer', 
											body: content,
											requestContentType : TEXT );
		
			def restResponseText = restResponse.getData();
												
			println "\n\n*************************************************";
			println restResponseText.toString(5);
			println "*************************************************\n\n";
		
		
			def ks= restResponseText.keySet();
			ks.each { println it };
		
		
			[content: content, value:restResponseText];
		}
		else
		{
			render(view:"normal",model:[content:content]);
		}
		
	}
		
}
