package org.fogbeam.quoddy


class CommentController {

	def scaffold = true;
	
	def eventStreamService;
	// def entryService;
	
	def addComment = {
			
		println "addComment params: ${params}";
		
		// lookup the Event by id
		log.debug( "eventId: ${params.eventId}" );
		EventBase event = eventStreamService.getEventById( Integer.parseInt( params.eventId) );
			
		// add the comment to the Event
		if( session.user )
		{
			log.debug( "event: ${event}" );
			def user = User.findByUserId( session.user.userId );
			log.debug( "user: ${user}" );
		
			Comment newComment = new Comment();
			newComment.text = params.addCommentTextInput;
			newComment.creator = user;
			newComment.event = event;
			newComment.save();
			
			event.addToComments( newComment );
			
			eventStreamService.saveActivity( (Activity)event );
			
	    	// send JMS message saying "new entry submitted"
	    	/* def newCommentMessage = [msgType:"NEW_COMMENT", entry_id:entry.id, entry_uuid:entry.uuid, 
	    	                       	comment_id:newComment.id, comment_uuid:newComment.uuid, comment_text:newComment.text ];
	          */
			
	    	// send a JMS message to our testQueue
			// sendJMSMessage("searchQueue", newCommentMessage );			
			
			log.debug( "saved Comment for user ${user.userId}, event ${event.id}" );
		}
		else
		{
			// do nothing, can't comment if you're not logged in.
			log.info( "doing nothing, not logged in!" );
		}
	
		render( "OK" );
		
	}
}
