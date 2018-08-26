package org.fogbeam.quoddy.jaxrs

// import static org.grails.jaxrs.response.Responses.*
import static org.grails.plugins.jaxrs.response.Responses.*;

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

import org.fogbeam.quoddy.jaxrs.collection.EventSubscriptionCollection
import org.fogbeam.quoddy.subscription.BusinessEventSubscription;

@Path('/api/eventsubscriptions')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class EventSubscriptionCollectionResource
{
	@GET
	Response readAll() {
		
		println "EventSubscriptionCollectionResource readAll()";
        
		List all = BusinessEventSubscription.findAll();
	
		EventSubscriptionCollection collection = new EventSubscriptionCollection();
		
		collection.addAll( all );
		
        println( "Returning OK");
		ok( collection );
	}
	
	@Path('/{id}')
	EventSubscriptionResource getResource(@PathParam('id') String id) {
		new EventSubscriptionResource(id:id)
	}
	
}
