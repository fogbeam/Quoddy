package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.integration.activitystream.ActivityStreamEntry


class ActivityStreamController 
{
	def objectMapper
	def eventStreamService;
	def activityStreamTransformerService;
	def userService;
	def jmsService;
	def eventQueueService;
	
	def getQueueSize =
	{	
		// check and see how many queued up messages are waiting for this user...	
		// we'll call this on a timer basis and build up a message that says
		// XXX more recent updates waiting
		// or something along those lines...
		long queueSize = 0;
		if( session.user != null )
		{
			// println "checking queueSize for user: ${session.user.userId}";
			queueSize = eventQueueService.getQueueSizeForUser( session.user.userId );
		}
		
		// println "got queueSize as ${queueSize}"; 
		
		// render( "<h1>${messages.size()} messages pending on the queue!</h1>");
		render( queueSize );
	}
	
	
	// get all messages from the queue for this user, plus older messages from the DB
	// if necessary.  Return N total messages.  We need to make "N" a parameter
	// or something if we want a "click here to load more" button that just keeps pulling
	// in more messages on each click.
	def getContentHtml = 
	{
		
		def user = session.user;
		def page = params.page;
		if( !page ) 
		{
			page = "1";
		}
		println "getContentHtml requested page: ${page}";
		def events = new ArrayList<EventBase>();
		if( user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
			// activities = eventStreamService.getRecentFriendActivitiesForUser( user );
			events = eventStreamService.getRecentActivitiesForUser( user, 25 * Integer.parseInt( page ) );
		}
		else
		{
			// don't do anything if we don't have a user
		}
		
		render(template:"/activityStream",model:[activities:events]);
		
		
	}
	
	def index = {
		
		switch(request.method){
			case "POST":
				// def originTime = new Date().getTime();
			  println "Create\n"
			  // String json = request.reader.text;
			  String json = params.activityJson;
			  println("Got json:\n " + json );
			  
			  def mapper = objectMapper
			  
			  // convert from JSON to Groovy classes
			  ActivityStreamEntry streamEntry = mapper.readValue(json, ActivityStreamEntry.class);
			  
			  // map to our internal representation and save / msg
			  Activity activity = activityStreamTransformerService.getActivity( streamEntry );
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
