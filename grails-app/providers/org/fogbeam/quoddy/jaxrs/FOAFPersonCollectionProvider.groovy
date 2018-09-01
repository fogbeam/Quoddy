package org.fogbeam.quoddy.jaxrs

import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyWriter

import org.fogbeam.foaf.api.FoafModel;
import org.fogbeam.foaf.api.FoafPerson
import org.fogbeam.foaf.factory.FoafModelFactory;
import org.fogbeam.quoddy.jaxrs.collection.FOAFPersonCollection
import org.grails.plugins.jaxrs.provider.MessageBodyWriterSupport
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Produces( ['application/rdf+xml', 'application/xml', 'text/xml'] )
public class FOAFPersonCollectionProvider extends MessageBodyWriterSupport<FOAFPersonCollection> implements MessageBodyWriter<FOAFPersonCollection>
{	
	@Override
	protected void writeTo( FOAFPersonCollection foafUsers, MultivaluedMap<String, Object> httpHeaders, 
							OutputStream entityStream ) 
		throws IOException, WebApplicationException
	{

		log.info( "writeTo() called with FOAFPersonCollection: ${foafUsers}" );
		
		FoafModel model = FoafModelFactory.createModel( "JENA_25" );
		
		// write Users out to a FOAF file and write to output stream
		List<FoafPerson> foafPeople = foafUsers.getFoafPersons();
		
		foafPeople.each { model.addPerson(it); }
		
		model.write( entityStream );
				
	}
}