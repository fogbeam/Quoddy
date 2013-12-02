package org.fogbeam.quoddy.bpm

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import groovyx.net.http.RESTClient

import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.protocol.HttpContext
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.dto.ResourceLink
import org.fogbeam.quoddy.stream.ActivitiUserTask
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription

import com.hp.hpl.jena.query.Dataset
import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecution
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.ReadWrite
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.rdf.model.Literal
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.RDFNode
import com.hp.hpl.jena.tdb.TDBFactory

class ActivitiBPMController
{
	def claimTask =
	{
		println "claimTask() - params: ${params}";
		
		
		render(text: """{"msg":"OK"}""", contentType: "application/json", encoding: "UTF-8")
	}
	
	def completeTask = {
		
		// make the REST call back to the Activiti server to complete this task
		// using the supplied form values
		
		println "completeTaskForm() - params: ${params}";
		
		Map activitiForm = params.activitiForm;
		
		String taskUuid = params.taskUuid;
		
		println "taskUuid: " + taskUuid;
		
		// lookup our ActivitiUserTask instance by uuid
		ActivitiUserTask userTask = ActivitiUserTask.findByUuid( taskUuid );
		
		if( userTask )
		{
			println "located ActivitiUserTask instance!";
		}
		else
		{
			println "Failed to lookup ActivitiUserTask instance!";
			throw new RuntimeException( "Failed to locate ActivitiUserTask instance");
		}
		
		
		String taskId = userTask.taskId;
		
		ActivitiUserTaskSubscription owningSubscription = userTask.owningSubscription;
		
		String activitiServer = owningSubscription.activitiServer;
		
		// get Form from Activiti Server
		// /form/{taskId}/properties
		RESTClient restClient = new RESTClient( activitiServer )
		
		// adding server authentication, if required
		restClient.client.addRequestInterceptor(new HttpRequestInterceptor() {
				void process(HttpRequest httpRequest, HttpContext httpContext) {
						httpRequest.addHeader('Authorization', 'Basic ' + 'kermit:kermit'.bytes.encodeBase64().toString())
					}
				});
				
		// PUT /task/{taskId}/[claim|unclaim|complete|assign]	
		
		
		def restResponse = restClient.put( path: "task/${taskId}/complete", 
			contentType: TEXT,
			requestContentType:  JSON,
			body: activitiForm );
		
		println "restResponse: ${restResponse.data}";
		
		redirect( controller:'dummy' );
	}
	
	def completeTaskForm = {
		
		println "completeTaskForm() - params: ${params}";
		
		String taskUuid = params.id;
		
		// lookup our ActivitiUserTask instance by uuid
		ActivitiUserTask userTask = ActivitiUserTask.findByUuid( taskUuid );
		
		String taskId = userTask.taskId;
		
		ActivitiUserTaskSubscription owningSubscription = userTask.owningSubscription;
		
		String customerNumber = userTask.variables.get( "customerNumber" );
		
		// construct a TDB backed store on our triples
		// Make a TDB-backed dataset
		String quoddyHome = System.getProperty( "quoddy.home" );
		String directory = "${quoddyHome}/jenastore/triples" ;
		println "Opening TDB triplestore at: ${directory}";
		Dataset dataset = TDBFactory.createDataset(directory) ;
		
		dataset.begin(ReadWrite.READ) ;

		// Get model inside the transaction
		Model tdbModel = dataset.getDefaultModel() ;

		String baseQueryString = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +  
								 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
								 "PREFIX dc: <http://purl.org/dc/elements/1.1/> " + 
								 "PREFIX dcterm: <http://purl.org/dc/terms/> " +
								 "PREFIX owl: <http://www.w3.org/2002/07/owl#> " + 
								 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
								 "PREFIX dbpo: <http://dbpedia.org/ontology/> " + 
								 "PREFIX dbp: <http://dbpedia.org/resource/> " +
								 "PREFIX scorg: <http://schema.org/> " +
								 "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " + 
								 "PREFIX fogbeam: <http://schema.fogbeam.com#> " +
								 "PREFIX fogcust: <http://customers.fogbeam.com#> " +
								 "PREFIX fogpeople: <http://schema.fogbeam.com/people#>";

		
		List<User> people = new ArrayList<User>();						 
		List<ActivityStreamItem> statusUpdates = new ArrayList<ActivityStreamItem>();
		List<ResourceLink> links = new ArrayList<ResourceLink>();
										 
		// query for people that "haveExpertise" regarding our customer number
		// [quoddy:a7274c07-c904-4a70-bc98-6d8c21a962ce, http://schema.fogbeam.com/people#hasExpertise, http://customers.fogbeam.com/Boxer_Steel]
		String queryString = """select distinct ?entity ?y where {?entity fogpeople:hasExpertise ?y . ?y rdfs:label "${customerNumber}"^^<http://www.w3.org/2001/XMLSchema#string> .}""";
		
		/* Now create and execute the query using a Query object */
		queryString = baseQueryString + queryString;
		// println "Our Query: ${queryString}";
		try
		{
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, tdbModel);
				
			ResultSet results = null;
			try
			{
				results = qexec.execSelect() ;
				for ( ; results.hasNext() ; )
				{
					QuerySolution soln = results.nextSolution() ;
					RDFNode x = soln.get("entity" );
					RDFNode y = soln.get( "y" );
				
					System.out.println( x.toString() + " y: " + y.toString() );
				
					// extract our entry UUID from the Subject and locate the matching Entry and
					// add it to searchResults
					String subject = x.toString();
					String uuid = subject.replace( "quoddy:", "" );
					
					// lookup User by UUID
					User user = User.findByUuid( uuid );
					people.add( user );
								
				}
			}
			finally
			{
				qexec.close();
			}
			
			// query for content items that dc:reference our customer number
			// [quoddy:ab384185-7e70-4804-b44e-593b7cc1a72f, http://purl.org/dc/terms/references, http://customers.fogbeam.com/Boxer_Steel]
			queryString = """select distinct ?entity ?y where { ?entity dcterm:references ?y . ?y rdfs:label "${customerNumber}"^^<http://www.w3.org/2001/XMLSchema#string> . }""";
			
			queryString = baseQueryString + queryString;
			// println "Our Query: ${queryString}";
			
			println "\n\n\n*******************************************************************\n\n\n";
			
			query = QueryFactory.create(baseQueryString + queryString);
			qexec = QueryExecutionFactory.create(query, tdbModel);
			
			
			try
			{
				results = qexec.execSelect();
				for ( ; results.hasNext() ; )
				{
					println "Found a Potential Status Update!";
					
					QuerySolution soln = results.nextSolution() ;
					RDFNode x = soln.get("entity" );
					RDFNode y = soln.get( "y" );
					
					System.out.println( x.toString() + " y: " + y.toString() );
				
					// extract our entry UUID from the Subject and locate the matching Entry and
					// add it to searchResults
					String subject = x.toString();
					String uuid = subject.replace( "quoddy:", "" );
								
					// look up Status Update by uuid
					ActivityStreamItem update = ActivityStreamItem.findByUuid( uuid );
					if( update != null && update.streamObject instanceof StatusUpdate )
					{
						println "Got a real Status Update!";
						statusUpdates.add( update );
					}
					else
					{
						println "Nope";
					}
					
				}

			}
			finally
			{
				qexec.close();
			}
		
			println "\n\n\n*******************************************************************************\n\n\n";
			
			// query for entities with an internalLink that have our customer number
			// [fogcust:Boxer_Steel, http://schema.fogbeam.com#internalLink, "http://crm.int.fogbeam.com/index.php?module=Accounts&action=DetailView&record=2e383b79-3892-799a-ad7d-529ae40d866e"^^http://www.w3.org/2001/XMLSchema#string]
			// [fogcust:Boxer_Steel, http://www.w3.org/2000/01/rdf-schema#label, "CUS729897"^^http://www.w3.org/2001/XMLSchema#string]
			queryString = """select distinct ?entity ?z ?prefLabel where {?entity fogbeam:internalLink ?z . ?entity rdfs:label "${customerNumber}"^^<http://www.w3.org/2001/XMLSchema#string> . ?entity dc:title ?prefLabel }""";
			
			queryString = baseQueryString + queryString;
			// println "Our Query: ${queryString}";
			
			query = QueryFactory.create(baseQueryString + queryString);
			qexec = QueryExecutionFactory.create(query, tdbModel);
			
			try
			{
				results = qexec.execSelect();
				for ( ; results.hasNext() ; )
				{
					QuerySolution soln = results.nextSolution() ;
					RDFNode x = soln.get("entity" );
					Literal intLink = soln.get( "z" );
					Literal prefLabel = soln.getLiteral( "prefLabel" );
				
					System.out.println( x.toString() + " y: " + intLink.toString() );
				
					// extract our entry UUID from the Subject and locate the matching Entry and
					// add it to searchResults
					String internalLink = intLink.getString();
					println "internalLink: " + internalLink;
					String linkName = prefLabel.getString();
					ResourceLink aLink = new ResourceLink( href: internalLink, name: linkName );
					links.add( aLink );			
					
				}
			}
			finally
			{
				qexec.close();
			}

		}
		finally
		{
			dataset.commit();
			dataset.end();
		}
		
		
		
		String activitiServer = owningSubscription.activitiServer;
		
		// get Form from Activiti Server
		// /form/{taskId}/properties
		RESTClient restClient = new RESTClient( activitiServer )
		
		// adding server authentication, if required
		restClient.client.addRequestInterceptor(new HttpRequestInterceptor() {
				void process(HttpRequest httpRequest, HttpContext httpContext) {
						httpRequest.addHeader('Authorization', 'Basic ' + 'kermit:kermit'.bytes.encodeBase64().toString())
					}
				});
				
		String restPath = "form/${taskId}/properties";
		println "activitiServer: ${activitiServer}";
		println "restPath: ${restPath}";
		def response = restClient.get(path:restPath);

		// def slurper = new JsonSlurper();
		
		println "response.data = ${response.data}";
		
		def taskForm = response.data.data;
				
		
		[taskUuid: taskUuid, taskForm:taskForm, people:people, statusUpdates:statusUpdates, links:links];
	}	
	
}
