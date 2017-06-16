package org.fogbeam.quoddy.service.semantics

import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.semantics.Entity
import org.fogbeam.quoddy.semantics.Property as SemanticProperty

import com.hp.hpl.jena.ontology.OntClass
import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.ontology.OntModelSpec
import com.hp.hpl.jena.ontology.OntProperty
import com.hp.hpl.jena.ontology.OntResource
import com.hp.hpl.jena.query.Dataset
import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecution
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.ReadWrite
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.rdf.model.InfModel
import com.hp.hpl.jena.rdf.model.Literal
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.rdf.model.Statement
import com.hp.hpl.jena.rdf.model.StmtIterator
import com.hp.hpl.jena.reasoner.Reasoner
import com.hp.hpl.jena.reasoner.ReasonerRegistry
/* import org.fogbeam.quoddy.semantics.Property as SemanticProperty */

class JenaService 
{

	def jenaTemplate;

	
	def addUserAnnotation( final User user, final String annotationPredicate, final String annotationObjectQN ) 
	{
		// add a semantic annotation about a User here... 
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.WRITE);
		
		try
		{
			// Get model inside the transaction
			Model model = dataset.getDefaultModel() ;
		
			Resource newResource = model.createResource( "quoddy:${user.uuid}" );
		
			Resource object = model.createResource( annotationObjectQN );
			
			Property property = model.createProperty( annotationPredicate );

			Statement s = model.createStatement(newResource, property, object);
			model.add( s );
			
			dataset.commit();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			dataset.abort();
		}
		finally
		{
			dataset.end();
	
		}
			
		log.debug( "done adding annotation");
	}
	
	
	List<Statement> listAllStatements()
	{
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.READ);

		
		// list all the Statements in our Jena store
		Model m = jenaTemplate.getDefaultModel();
		
		List allStatements = new ArrayList();
		
		StmtIterator sIter = m.listStatements();
		
		
		while( sIter.hasNext() )
		{
			
			Statement s = sIter.nextStatement();
			
			allStatements.add(s);
		}
		
		dataset.end();
		
		return allStatements;
	}
		
	
	public void saveProperty( final String propertyUri, final String propertyLabel )
	{
		
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.WRITE);

		
		Model m = jenaTemplate.getDefaultModel();
		
		OntModel onto = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, m);
		
		OntProperty newProperty = onto.createOntProperty(propertyUri)
		OntResource objectPropertyType = onto.createOntResource("http://www.w3.org/2002/07/owl#ObjectProperty");
		newProperty.setRDFType( objectPropertyType );
		
		
		Property rdfsLabel = m.createProperty("http://www.w3.org/2000/01/rdf-schema#", "label");
		newProperty.addProperty(rdfsLabel, propertyLabel )
		
		
		dataset.commit();
		
		dataset.end();

		
	}
	
	
	public void saveClass( final String classUri, final String classLabel )
	{
		
		
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.WRITE);

		
		Model m = jenaTemplate.getDefaultModel();
		
		OntModel onto = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, m);
		
		
		OntClass newClass = onto.createClass( classUri );

		Property rdfsLabel = m.createProperty("http://www.w3.org/2000/01/rdf-schema#", "label");
		newClass.addProperty(rdfsLabel, classLabel );
		
		dataset.commit();
		
		dataset.end();

	}
	
	public void saveStatementWithResourceObject( final String subject, final String predicate, final String object )
	{
		
		log.debug( "saveStatementWithResourceObject" );
		log.debug( "subject: ${subject} \npredicate: ${predicate}\nobject: ${object}");
		
		
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.WRITE);
		
		Model m = jenaTemplate.getDefaultModel();
		
		Resource rSubject = m.createResource( subject );
		Property rProperty = m.createProperty( predicate );
		Resource rObject = m.createResource( object );
		
		log.debug( "rObject: ${rObject}");
		
		try
		{
		
			Statement s = m.createStatement(rSubject, rProperty, rObject);
		
			m.add( s );
		
			dataset.commit();
		}
		finally
		{
			if( dataset != null )
			{
				dataset.end();
			}
		}
	}

	public void saveStatementWithLiteralObject( final String subject, final String predicate, final String object )
	{
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.WRITE);
		
		Model m = jenaTemplate.getDefaultModel();
		
		Resource rSubject = m.createResource( subject );
		Property rProperty = m.createProperty( predicate );
		Literal lObject = m.createTypedLiteral( object );
		try
		{
			m.addLiteral( rSubject, rProperty, lObject );
		
			dataset.commit();
		}
		finally
		{
			if( dataset != null )
			{
				dataset.end();
			}
		}
	}

	
	
	public List<SemanticProperty> listProperties()
	{

		List<SemanticProperty> allProperties = new ArrayList<SemanticProperty>();
				
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.READ);

		
		// list all the Statements in our Jena store
		Model m = jenaTemplate.getDefaultModel();
		
		
		String baseQueryString =
		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
		"PREFIX dcterm: <http://purl.org/dc/terms/> " +
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX dbpo: <http://dbpedia.org/ontology/> " +
		"PREFIX dbp: <http://dbpedia.org/resource/> " +
		"PREFIX scorg: <http://schema.org/> " +
		"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " +
		"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
		"PREFIX fogent: <http://competitive.fogbeam.org/ontology/>";
		
		
		String queryString =
		baseQueryString +
		"select ?entity ?label where { ?entity rdf:type owl:ObjectProperty . ?entity rdfs:label ?label . }";
		
		/* Query for Statements that represent our Properties */
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		

		try
		{
			ResultSet solutions = qexec.execSelect() ;
			for ( ; solutions.hasNext() ; )
			{
				QuerySolution soln = solutions.nextSolution();
				log.debug( "solution: ${soln}");
				Resource entity = soln.getResource("entity" );
				Literal label = soln.getLiteral("label");
				
				SemanticProperty property = new SemanticProperty();
				property.uri = entity.getURI();
				property.label = label.getValue();
				
				allProperties.add( property );

			}
		}
		finally
		{
			qexec.close();
		}
		
		dataset.end();

		return allProperties;
		
	}

	public List listClasses()
	{

		
		List allClasses = new ArrayList();
		
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.READ);

		
		// list all the Statements in our Jena store
		Model m = jenaTemplate.getDefaultModel();
		
		
		String baseQueryString =
		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
		"PREFIX dcterm: <http://purl.org/dc/terms/> " +
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX dbpo: <http://dbpedia.org/ontology/> " +
		"PREFIX dbp: <http://dbpedia.org/resource/> " +
		"PREFIX scorg: <http://schema.org/> " +
		"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " +
		"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
		"PREFIX fogent: <http://competitive.fogbeam.org/ontology/>";
		
		
		String queryString =
		baseQueryString +
		"select ?entity where { ?entity rdf:type owl:Class . }";
		
		/* Query for Statements that represent our Properties */
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		

		try
		{
			ResultSet solutions = qexec.execSelect() ;
			for ( ; solutions.hasNext() ; )
			{
				QuerySolution soln = solutions.nextSolution();
				log.debug( "solution: ${soln}" );
				
				
				allClasses.add( soln );

			}
		}
		finally
		{
			qexec.close();
		}
				
		
		dataset.end();

		
		return allClasses;
	}

	
	public List<Entity> listEntities()
	{

		
		List<Entity> allEntities = new ArrayList<Entity>();
		
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.READ);

		
		// list all the Statements in our Jena store
		Model m = jenaTemplate.getDefaultModel();
		
		
		String baseQueryString =
		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
		"PREFIX dcterm: <http://purl.org/dc/terms/> " +
		"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		"PREFIX dbpo: <http://dbpedia.org/ontology/> " +
		"PREFIX dbp: <http://dbpedia.org/resource/> " +
		"PREFIX scorg: <http://schema.org/> " +
		"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " +
		"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
		"PREFIX fogent: <http://competitive.fogbeam.org/ontology/>";		
		
		String queryString =
		baseQueryString +
		"select ?entity ?label where { ?entity rdf:type fogent:FogbeamEntity . ?entity rdfs:label ?label .}";
		
		/* Query for Statements that represent our Properties */
		Query query = QueryFactory.create(queryString) ;
		
		Reasoner reasoner = ReasonerRegistry.getOWLMiniReasoner();
		InfModel inf = ModelFactory.createInfModel(reasoner, m);
		
		QueryExecution qexec = QueryExecutionFactory.create(query, inf);
		
		
		try
		{
			ResultSet solutions = qexec.execSelect() ;
			for ( ; solutions.hasNext() ; )
			{
				QuerySolution soln = solutions.nextSolution();
				log.debug( "solution: ${soln}" );
				Resource entityRes = soln.getResource("entity");
				Literal labelLit = soln.getLiteral("label");
				Entity entity = new Entity();
				entity.uri = entityRes.getURI();
				entity.label = labelLit.getValue();
				allEntities.add( entity );
			}
		}
		finally
		{
			qexec.close();
		}
				
		
		dataset.end();

		
		return allEntities;
	}

	
	public List<Statement> findStatementsWithSubject( String subject )
	{

		
		List<Statement> allStatements = new ArrayList<Statement>();
		
		Dataset dataset = jenaTemplate.getDataset();
		dataset.begin(ReadWrite.READ);

		
		// list all the Statements in our Jena store
		Model m = jenaTemplate.getDefaultModel();

		Resource subjectResource = m.createResource( subject );
		StmtIterator stmtIter = m.listStatements(subjectResource, null, null );
		while( stmtIter.hasNext() )
		{
			allStatements.add( stmtIter.nextStatement() );
		}
		
		dataset.end();

		
		return allStatements;
	}
	
					
}
