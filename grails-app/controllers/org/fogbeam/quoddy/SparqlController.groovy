package org.fogbeam.quoddy

import org.fogbeam.quoddy.search.SearchResult
import org.fogbeam.quoddy.stream.ActivityStreamItem

import com.hp.hpl.jena.query.Dataset
import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecution
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.ReadWrite
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.RDFNode
import com.hp.hpl.jena.tdb.TDBFactory


class SparqlController
{
	
	def userService;

	def index = 
	{
		User user = null;
		if( session.user )
		{
			user = userService.findUserByUserId( session.user.userId);

		}
		else
		{
			user = userService.findUserByUserId( "anonymous" );
		}
		
		
		def model = [:];

		return model;
	}	
	
	
	def doSearch =
	{
		
		User user = null;
		if( session.user )
		{
			user = userService.findUserByUserId( session.user.userId);

		}
		else
		{
			user = userService.findUserByUserId( "anonymous" );
		}
		

		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		String baseQueryString = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
								 "PREFIX dc: <http://purl.org/dc/elements/1.1/> PREFIX dcterm: <http://purl.org/dc/terms/> " +
								 "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
								 "PREFIX dbpo: <http://dbpedia.org/ontology/> PREFIX dbp: <http://dbpedia.org/resource/> PREFIX scorg: <http://schema.org/> ";
		
		String userQueryString = params.queryString;
		
		String queryString = baseQueryString + userQueryString;
		
		// a sample query
		// final queryString = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT distinct ?entity ?y WHERE { ?entity <http://purl.org/dc/terms/references> ?y . ?y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/MusicalArtist> . ?y <http://dbpedia.org/ontology/birthDate> ?birthDate . FILTER ( ?birthDate  < "1950-01-01T00:00:00Z"^^xsd:dateTime) }";

		
		
		// Make a TDB-backed dataset
		String quoddyHome = System.getProperty( "quoddy.home" );
		String directory = "${quoddyHome}/jenastore/triples" ;
		println "Opening TDB triplestore at: ${directory}";
		Dataset dataset = TDBFactory.createDataset(directory) ;
		
		dataset.begin(ReadWrite.READ) ;

		// Get model inside the transaction
		Model tdbModel = dataset.getDefaultModel() ;

		/* Now create and execute the query using a Query object */
		println "Our Query: ${queryString}";
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, tdbModel);
				
		try
		{
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				RDFNode x = soln.get("entity" );
				RDFNode y = soln.get( "y" );
				
				System.out.println( x.toString() + " y: " + y.toString() );
				
				// extract our entry UUID from the Subject and locate the matching Entry and
				// add it to searchResults
				String subject = x.toString();
				subject = subject.replace( "quoddy:", "" );
				
				println "subject: " + subject;
				
				List<ActivityStreamItem> asiResults = ActivityStreamItem.executeQuery( "select asi from ActivityStreamItem as asi where asi.uuid = ?", [subject] );
				
				
				if( asiResults != null && !asiResults.isEmpty() )
				{
					println "found valid ActivityStreamItem for SPARQL query";
					
					SearchResult result = new SearchResult();
					result.object = asiResults[0];
					searchResults.add( result );
					
				}
				else
				{
					println "could not locate ActivityStreamItem for uuid: " + subject;
				}				
			}
		}
		finally
		{
			qexec.close();
		}
		
		dataset.commit();
		dataset.end();

		
		
		
		def model = [searchResults:searchResults];
			
		render(view:"displaySearchResults", model:model);

	}
	
}
