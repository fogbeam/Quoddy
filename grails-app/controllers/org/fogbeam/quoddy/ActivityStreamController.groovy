package org.fogbeam.quoddy

import org.codehaus.jackson.map.ObjectMapper
import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.protocol.activitystreams.ActivityStreamEntry
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ResharedActivityStreamItem
import org.fogbeam.quoddy.stream.StreamItemBase


@Mixin(SidebarPopulatorMixin)
class ActivityStreamController 
{
	def eventStreamService;
	def activityStreamTransformerService;
	def userService;
	def jmsService;
	def eventQueueService;
	def userStreamDefinitionService;
	def userListService;
	def userGroupService;
	def businessEventSubscriptionService;
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
	
	
	def getQueueSize =
	{	
		// check and see how many queued up messages are waiting for this user...	
		// we'll call this on a timer basis and build up a message that says
		// XXX more recent updates waiting
		// or something along those lines...
		long queueSize = 0;
		if( session.user != null )
		{
			UserStreamDefinition userStream = null;
			// TODO: Include UserStream.  Use default if no userStreamId is provided.
			if( params.streamId )
			{
				userStream = userStreamDefinitionService.findStreamById( Long.parseLong( params.streamId  ) );
			}
			else 
			{
				userStream = userStreamDefinitionService.getStreamForUser( session.user, UserStreamDefinition.DEFAULT_STREAM  );	
			}
			
			// println "checking queueSize for user: ${session.user.userId}";
			queueSize = eventQueueService.getQueueSizeForUser( session.user.userId, userStream );
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
		
		// NOTE: this should be receiving a streamId parameter.  If there isn't one
		// we can assume the default stream for the user in question.  And since this is the
		// only place we call this variation of eventStreamService.getRecentActivitiesForUser,
		// we should be able to delete it (or force it to default to the default user stream
		// and then call the other version)
		
		
		// also, if this stuff is really supposed to be paginated, we need to fix this to include
		// an offset parameter for the call to eventStreamService.getRecentActivitiesForUser
		
		def user = session.user;
		def page = params.page;
		if( !page ) 
		{
			page = "1";
		}
		println "getContentHtml requested page: ${page}";
		def items = new ArrayList<StreamItemBase>();
		if( user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
			
			UserStreamDefinition selectedStream = null;
			if( params.streamId )
			{
				Long streamId = Long.valueOf( params.streamId );
				selectedStream = userStreamDefinitionService.findStreamById( streamId );
			}
			else
			{
				selectedStream = userStreamDefinitionService.getStreamForUser( user, UserStreamDefinition.DEFAULT_STREAM );
			}
			
			
			items = eventStreamService.getRecentActivitiesForUser( user, 25 * Integer.parseInt( page ), selectedStream );
		}
		else
		{
			// don't do anything if we don't have a user
		}
		
		render(template:"/activityStream",model:[activities:items]);
		
		
	}
	

	
	def viewUserStream = {
		
		println "viewUserStream: ";
		User user = session.user;
		
		
		String userId = params.userId;
		println "userId: ${userId}";
		def page = params.page;
		if( !page )
		{
			page = "1";
		}
		
		
		if( userId == null || userId.isEmpty() )
		{
			flash.message = "No UserId sent!";	
			return [];
		}
		
		User requestedUser = userService.findUserByUserId( userId );
		
		List<StreamItemBase> statusUpdatesForUser = null;
		if( requestedUser != null )
		{
			println "getting status updates for user ${requestedUser.userId}";
			statusUpdatesForUser = eventStreamService.getStatusUpdatesForUser( requestedUser );
				
		}
		else 
		{
			println "NO user";
		}
		
		Map model = [:];
		
		model.putAll( [user:user, statusUpdatesForUser:statusUpdatesForUser] );
		Map sidebarCollections = populateSidebarCollections( this, user );
		model.putAll( sidebarCollections );
		
		return model;
				
	}
	
	
	def discussItem = 
	{
		println "ActivityStreamController.discussItem invoked:";
		println "params: ${params}";
		
		/*  So, what data should we be receiving?  At a minimum, the id (or uuid) of the thing being
		 *  discussed, the id (or uuid) of the person starting the discussion, and one or more discussTarget id's.  
		 *  Optionally there could be a comment associated with the discussion.
		 *
		 *  For an initial version, let's make some simplifying assumptions.  You can only open discussion with
		 *  USERS (no groups or other 'things' )
		 */
		
		String discussItemUuid = params.discussItemUuid;
		String discussItemComment = params.discussItemComment;
		String discussTargetUserId = params.discussTargetUserId;
		
		// look up the existing ActivityStreamItem representing this item
		ActivityStreamItem discussItem = eventStreamService.getActivityStreamItemByUuid( discussItemUuid );
		
		/* Use our OpenMeetings integration to start a temporary conference room, generate
		 * invitation hashes for all requested users, send the invite hashes, and return the
		 * room ID to the caller so we can put our current user into the conference 
		 */
		
		println "Invoking OpenMeetings integration here...";
	}
	
	def shareItem =
	{
		
		println "ActivityStreamController.shareItem invoked:";
		println "params: ${params}";
		
		/*  So, what data should we be receiving?  At a minimum, the id (or uuid) of the thing being
		 *  shared, the id (or uuid) of the person sharing it, and one or more shareTarget id's.  Optionally
		 *  there could be a comment associated with the sharing activity.
		 *
		 *  For an initial version, let's make some simplifying assumptions.  You can only share to a
		 *  USER (no groups or other 'things' and you can only share to ONE target at a time.
		 *  
		 *  NOTE: we also have to be able to handle weird situations like "somebody shared this to me (an individual user)
		 *  and now I want to reshare it to somebody else", or even "I now want to share this to my public stream".
		 *   
		 */
		
		String shareItemUuid = params.shareItemUuid;
		String shareItemComment = params.shareItemComment;
		String shareTargetUserId = params.shareTargetUserId;
		
		// look up the existing ActivityStreamItem representing this item
		ActivityStreamItem originalItem = eventStreamService.getActivityStreamItemByUuid( shareItemUuid );
		
		// look up the "share target user" by userId
		User shareTargetUser = userService.findUserByUserId( shareTargetUserId );
		
		// this should mean a new ActivityStreamItem instance, which "points" to the same underlying
		// streamObject as the one being shared, no?  Or should an ActivityStreamItem be able to be the "target"
		// of another ASI in turn?  If there's a comment associated with the reshare activity, what do we
		// hang the comment off of?  Should we create a new StreamObject type just for reshares?  
		
		  
		ResharedActivityStreamItem newStreamItem = new ResharedActivityStreamItem();
		newStreamItem.verb = "quoddy_item_reshare";
		newStreamItem.actorObjectType = "User";
		newStreamItem.targetObjectType = "User";
		newStreamItem.actorUuid = session.user.uuid;
		newStreamItem.targetUuid = shareTargetUser.uuid;
		newStreamItem.owner = session.user;
		newStreamItem.objectClass = originalItem.objectClass;
		newStreamItem.streamObject = originalItem.streamObject;
		newStreamItem.published = new Date(); // set published to "now"
		
		newStreamItem.originalItem = originalItem;
		newStreamItem.title = "Reshared ActivityStreamItem";
		newStreamItem.url = new URL( "http://www.example.com" );	
		
		// NOTE: we added "name" to StreamItemBase, but how is it really going
		// to be used?  Do we *really* need this??
		newStreamItem.name = newStreamItem.title;
		
		// NOTE: are we eliminating the idea of "effective date" or do we
		// just need to rethink it?
		// newStreamItem.effectiveDate = newStreamItem.published;
		
		eventStreamService.saveActivity( newStreamItem );		
		
	}
	
}