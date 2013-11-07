package org.fogbeam.quoddy.demo.jms

import groovyx.net.http.RESTClient

class DemoContentEnrichmentListenerService
{
	
	static expose = ['jms'];
	static destination = "contentEnrichmentQueue";
	
	def onMessage( msg )
	{
		// we have a new piece of content that needs enriching... 
		
		// call Stanbol REST API to get enrichment data
		RESTClient restClient = new RESTClient( "http://localhost:8080" )
		
		
		def restResponse = restClient.post(path:'enhancer', body:"Stanbol recognizes people and places like Paris and Bob Marley");
		
		println "\n\n*************************************************";
		
		println restResponse;
		
		println "*************************************************\n\n";
		
	}
	
	
	
	
}
