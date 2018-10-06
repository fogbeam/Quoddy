package org.fogbeam.quoddy.jaxrs

// import static org.grails.jaxrs.response.Responses.*
import static org.grails.plugins.jaxrs.response.Responses.*;

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

import org.fogbeam.protocol.activitystreams.ActivityStreamEntry
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.RemoteActivityStreamItem
import org.fogbeam.quoddy.stream.ShareTarget

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
		log.info "Creating new entry: ${entry.content}";
		
		// convert our remote object to our internal representation
		ActivityStreamItem item = activityStreamTransformerService.transform( entry );
		
		RemoteActivityStreamItem remoteItem = new RemoteActivityStreamItem();
		remoteItem.name = item.name;
		remoteItem.owner = item.owner;
		remoteItem.effectiveDate = new Date(); // now
		
		/* 
		if( !item.targetUuid )
		{
			ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
			item.targetUuid = streamPublic.uuid;
			
			/ / do we really need this?
			remoteItem.targetUuid = streamPublic.uuid;
			
		}
		else
		{
			remoteItem.targetUuid = item.targetUuid;
		}
		*/
		
		String targetUuid = item.targetUuid;
		println "Fucking setting targetUuid to fucking: ${targetUuid}";
		
		remoteItem.targetUuid = targetUuid;
		remoteItem.remoteObjectType = item.objectObjectType;
		
		if( ! remoteItem.save(flush:true) )
		{
			remoteItem.errors.allErrors.each { println it };
			throw new RuntimeException( "could not save RemoteItem!");
		}
		
		item.streamObject = remoteItem;
		
		eventStreamService.saveActivity( item );
		
	}
	
}