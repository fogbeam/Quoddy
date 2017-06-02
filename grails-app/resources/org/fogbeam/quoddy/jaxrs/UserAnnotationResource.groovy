package org.fogbeam.quoddy.jaxrs

import javax.ws.rs.Consumes
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path('/api/user/annotation')
@Consumes(['application/xml','application/json'])
@Produces(['application/rdf+xml','application/xml', 'text/xml'])
class UserAnnotationResource 
{
	def jenaService;
	
	// TODO: implement the PUT (create new) method for a UserAnnotation 
	
	
	
}
