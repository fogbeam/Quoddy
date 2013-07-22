package org.fogbeam.quoddy

import org.codehaus.jackson.map.ObjectMapper
import org.fogbeam.quoddy.integration.activitystream.ActivityStreamEntry
import org.fogbeam.quoddy.stream.ActivityStreamItem

class ActivityStreamOldControllerMethods
{
	def jmsService;
	
	def index = {
		
		switch(request.method){
			case "POST":
				// def originTime = new Date().getTime();
			  println "Create\n"
			  // String json = request.reader.text;
			  String json = params.activityJson;
			  println("Got json:\n " + json );
			  
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
			  
			  println "sending message to JMS";
			  jmsService.send( queue: 'uitestActivityQueue', /* msg */ activity, 'standard', null );
			  
			  // println streamEntry.toString();
			  
			  break
			case "GET":
			  println "Retrieve\n"
			  break
			case "PUT":
			  println "Update\n"
			  break
			case "DELETE":
			  println "Delete\n"
			  break
		  }
		
		render "OK";
	}
}
