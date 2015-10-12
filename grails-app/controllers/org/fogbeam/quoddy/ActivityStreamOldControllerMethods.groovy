package org.fogbeam.quoddy

import org.codehaus.jackson.map.ObjectMapper
import org.fogbeam.protocol.activitystreams.ActivityStreamEntry
import org.fogbeam.quoddy.stream.ActivityStreamItem

class ActivityStreamOldControllerMethods
{
	def jmsService;
	
	def index = {
		
		switch(request.method){
			case "POST":
				// def originTime = new Date().getTime();
			  
			  // String json = request.reader.text;
			  String json = params.activityJson;
			  
			  
			  ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			  
			  // convert from JSON to Groovy classes
			  ActivityStreamEntry streamEntry = mapper.readValue(json, ActivityStreamEntry.class);
			  
			  // map to our internal representation and save / msg
			  ActivityStreamItem activity = activityStreamTransformerService.getActivity( streamEntry );
			  eventStreamService.saveActivity( activity );
			  
			  // send notification message
			  // Map msg = new HashMap();
			  // msg.creator = activity.owner.userId;
			  // msg.text = activity.content;
			  // msg.targetUuid = activity.targetUuid;
			  // msg.published = activity.published;
			  // msg.originTime = activity.dateCreated.time;
			  // TODO: figure out what to do with "effectiveDate" here
			  // msg.effectiveDate = msg.originTime;
			  
			  // msg.actualEvent = activity;
			  
			  jmsService.send( queue: 'uitestActivityQueue', /* msg */ activity, 'standard', null );
			  
			  
			  break
			case "GET":
			  
			  break
			case "PUT":
			  
			  break
			case "DELETE":
			  
			  break
		  }
		
		render "OK";
	}
}
