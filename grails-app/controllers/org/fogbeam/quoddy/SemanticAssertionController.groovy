package org.fogbeam.quoddy

import com.hp.hpl.jena.rdf.model.Statement

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
