package org.fogbeam.quoddy

import grails.plugin.springsecurity.annotation.Secured

class StatementController 
{

	def jenaService;
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def list()
	{
		List allStatements = jenaService.listAllStatements();
					
		Map model = [:];
		
		model.put( "allStatements", allStatements);
		
		return model;
	}	
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def addProperty()
	{
		
		Map model = [:];
		
		return model;
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProperty()
	{
		String propertyUri = params.propertyUri;
		String propertyLabel = params.propertyLabel;
		
		jenaService.saveProperty( propertyUri, propertyLabel );
		
		redirect(controller:"statement", action:"list" );
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def addClass()
	{
		
		Map model = [:];
		
		return model;
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveClass()
	{
		String classUri = params.classUri;
		String classLabel = params.classLabel;
		
		jenaService.saveClass( classUri, classLabel );
						
		redirect(controller:"statement", action:"list" );
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def create()
	{
		Map model = [:];
		
		return model;
	}
	
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def save()
	{
		String subject = params.subject;
		String predicate = params.predicate;
		String object = params.object;
		
		jenaService.saveStatementWithResourceObject( subject, predicate, object );		
		
		redirect(controller:"statement", action:"list" );
		
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def listProperties()
	{
		List allStatements = jenaService.listProperties();
		
		Map model = [:];
		
		model.put( "allStatements", allStatements );
		
		return model;
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def listClasses()
	{
		List allStatements = jenaService.listClasses();
				
		Map model = [:];
		
		model.put( "allStatements", allStatements );
		
		return model;
	}
}