package org.fogbeam.quoddy

class StatementController 
{

	def jenaService;
	
	def list =
	{
		
		List allStatements = jenaService.listAllStatements();
				
		
		Map model = [:];
		
		model.put( "allStatements", allStatements);
		
		return model;
		
	}	
	
	
	def addProperty =
	{
		
		Map model = [:];
		
		return model;
	}
	
	def saveProperty =
	{
		String propertyUri = params.propertyUri;
		String propertyLabel = params.propertyLabel;
		
		jenaService.saveProperty( propertyUri, propertyLabel );
		
		redirect(controller:"statement", action:"list" );
	}
	
	def addClass =
	{
		
		Map model = [:];
		
		return model;
	}
	
	def saveClass =
	{
		String classUri = params.classUri;
		String classLabel = params.classLabel;
		
		jenaService.saveClass( classUri, classLabel );
						
		redirect(controller:"statement", action:"list" );
	}
	
	
	def create = 
	{
		Map model = [:];
		
		return model;
	}
	
	
	def save = 
	{
		String subject = params.subject;
		String predicate = params.predicate;
		String object = params.object;
		
		jenaService.saveStatementWithResourceObject( subject, predicate, object );		
		
		redirect(controller:"statement", action:"list" );
		
	}
	
	def listProperties =
	{
		List allStatements = jenaService.listProperties();
		
		Map model = [:];
		
		model.put( "allStatements", allStatements );
		
		return model;

	}
	
	
	def listClasses =
	{
		List allStatements = jenaService.listClasses();
				
		Map model = [:];
		
		model.put( "allStatements", allStatements );
		
		return model;

	}
}