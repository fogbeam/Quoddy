package org.fogbeam.quoddy.jaxrs

import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

import org.fogbeam.protocol.activitystreams.ActivityStreamEntry
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.RemoteActivityStreamItem

@Path('/api/activitystreamentry')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class ActivityStreamEntryResource
{
	def eventStreamService;
	def activityStreamTransformerService;
	
	
	@POST
	ActivityStreamEntry insert( ActivityStreamEntry entry )
	{
		println "Creating new entry: ${entry.content}";
		
		// convert out remote object to our internal representation
		ActivityStreamItem item = activityStreamTransformerService.transform( entry );
		
		RemoteActivityStreamItem remoteItem = new RemoteActivityStreamItem();
		remoteItem.name = item.name;
		remoteItem.owner = item.owner;
		remoteItem.effectiveDate = new Date(); // now
		remoteItem.targetUuid = item.targetUuid;
		remoteItem.remoteObjectType = item.objectObjectType;
		
		if( ! remoteItem.save() )
		{
			remoteItem.errors.allErrors.each { println it };
			throw new RuntimeException( "could not saave RemoteItem!");
		}
		
		item.streamObject = remoteItem;
		
		eventStreamService.saveActivity( item );
		
	}
	
}