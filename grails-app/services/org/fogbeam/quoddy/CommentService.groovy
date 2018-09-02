package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.StreamItemComment

class CommentService 
{
	def eventStreamService;
	def jmsService;
	
	def addComment( final ActivityStreamItem item, final StreamItemComment newComment, final User currentUser )
	{
		newComment.save(flush:true);
		
		item.streamObject.addToComments( newComment );
		
		eventStreamService.saveActivity( item );
		
		// send JMS message saying "new comment added"
		def newCommentMessage = [msgType:"NEW_STREAM_ENTRY_COMMENT",
								  activityId:item.id, activityUuid:item.uuid,
									 entry_id:item.streamObject.id, entry_uuid:item.streamObject.uuid,
								  comment_id:newComment.id, comment_uuid:newComment.uuid,
								  comment_text:newComment.text ];
		
		// send a JMS message to our testQueue
		jmsService.send("quoddySearchQueue", newCommentMessage );
		
		log.debug( "saved StreamItemComment for user ${currentUser.userId}, item ${item.id}" );

	}
}
