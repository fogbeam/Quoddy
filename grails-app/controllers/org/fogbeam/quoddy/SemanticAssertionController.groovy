package org.fogbeam.quoddy

import grails.plugin.springsecurity.annotation.Secured

class SemanticAssertionController 
{
	
	// def jenaService;
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def index()
	{
		
		Map model = [:];
		
		
		return model;
	}
}
