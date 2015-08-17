package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem;
import org.fogbeam.quoddy.stream.StreamItemComment;
import org.fogbeam.quoddy.stream.StreamItemBase;


class CommentController {

	def scaffold = false;
	
	def eventStreamService;
	// def entryService;
	
	def addComment = {
			
		log.debug("addComment params: ${params}");
		
		// lookup the Event by id
		log.debug( "eventId: ${params.eventId}" );
		ActivityStreamItem item = eventStreamService.getActivityStreamItemById( Integer.parseInt( params.eventId) );
			
		// add the comment to the Event
		if( session.user )
		{
			log.debug( "item: ${item}" );
			def user = User.findByUserId( session.user.userId );
			log.debug( "user: ${user}" );
		
			StreamItemComment newComment = new StreamItemComment();
			newComment.text = params.addCommentTextInput;
			newComment.creator = user;
			newComment.event = item.streamObject;
			newComment.save();
			
			item.streamObject.addToComments( newComment );
			
			eventStreamService.saveActivity( item );
			
	    	// send JMS message saying "new comment added"
	    	def newCommentMessage = [msgType:"NEW_STREAM_ENTRY_COMMENT",  
									  activityId:item.id, activityUuid:item.uuid,  
	    	                       	  entry_id:item.streamObject.id, entry_uuid:item.streamObject.uuid,
									  comment_id:newComment.id, comment_uuid:newComment.uuid, 
									  comment_text:newComment.text ];
			
	    	// send a JMS message to our testQueue
			sendJMSMessage("quoddySearchQueue", newCommentMessage );			
			
			log.debug( "saved StreamItemComment for user ${user.userId}, item ${item.id}" );
		}
		else
		{
			// do nothing, can't comment if you're not logged in.
			log.info( "doing nothing, not logged in!" );
		}
	
		// render using template, so we can ajaxify the loading of the comments...
		render( template:"/renderComments", model:[comments:item.streamObject.comments]);
		
	}
}
