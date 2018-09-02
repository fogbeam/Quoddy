package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem;
import org.fogbeam.quoddy.stream.StreamItemComment;

import grails.plugin.springsecurity.annotation.Secured

import org.fogbeam.quoddy.stream.StreamItemBase;


class CommentController 
{	
    def jmsService;
	def eventStreamService;
	def userService;
	def commentService;
	
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def addComment()
    {	
		User currentUser = userService.getLoggedInUser();
		
		log.debug("addComment params: ${params}");
		
		// lookup the Event by id
		log.debug( "eventId: ${params.eventId}" );
		ActivityStreamItem item = eventStreamService.getActivityStreamItemById( Integer.parseInt( params.eventId) );
			
		log.debug( "item: ${item}" );
		
		StreamItemComment newComment = new StreamItemComment();
		newComment.text = params.addCommentTextInput;
		newComment.creator = currentUser;
		newComment.event = item.streamObject;
	
		commentService.addComment( item, newComment, currentUser );
				
		// render using template, so we can ajaxify the loading of the comments...
		render( template:"/renderComments", model:[comments:item.streamObject.comments]);
		
	}
}
