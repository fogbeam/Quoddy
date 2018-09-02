package org.fogbeam.quoddy

// import static groovyx.net.http.ContentType.JSON
// import static groovyx.net.http.ContentType.TEXT
// import static groovyx.net.http.ContentType.XML
// import groovyx.net.http.RESTClient


import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.profile.ContactAddress
import org.fogbeam.quoddy.profile.Profile
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ResharedActivityStreamItem
import org.fogbeam.quoddy.stream.StreamItemBase

import grails.plugin.springsecurity.annotation.Secured
import groovyx.net.http.*;


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
	def openMeetingsService;
	
	
    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
	def getQueueSize()
	{	
		// check and see how many queued up messages are waiting for this user...	
		// we'll call this on a timer basis and build up a message that says
		// XXX more recent updates waiting
		// or something along those lines...
		long queueSize = 0;

		UserStreamDefinition userStream = null;
		
		User currentUser = userService.getLoggedInUser();

		log.info( "getQueueSize for user: ${currentUser}");
				
		// use supplied streamId, or default if no user streamId is provided.
		if( params.streamId )
		{
			log.info( "StreamId: ${params.streamId}");
			userStream = userStreamDefinitionService.findStreamById( Long.parseLong( params.streamId  ) );
		}
		else
		{
			log.info( "Using default stream for user: ${currentUser}");
			userStream = userStreamDefinitionService.getStreamForUser( currentUser, UserStreamDefinition.DEFAULT_STREAM  );
		}
		
		log.trace( "checking queueSize for user: ${currentUser.userId}" );
		
		queueSize = eventQueueService.getQueueSizeForUser( currentUser.userId, userStream );
		
		log.info( "got queueSize as ${queueSize}" ); 
		
		render( queueSize );
	}
	
	
	// get all messages from the queue for this user, plus older messages from the DB
	// if necessary.  Return N total messages.  We need to make "N" a parameter
	// or something if we want a "click here to load more" button that just keeps pulling
	// in more messages on each click.
    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def getContentHtml() 
	{
		// NOTE: this should be receiving a streamId parameter.  If there isn't one
		// we can assume the default stream for the user in question.  And since this is the
		// only place we call this variation of eventStreamService.getRecentActivitiesForUser,
		// we should be able to delete it (or force it to default to the default user stream
		// and then call the other version)
		
		log.info( "getContentHtml() called" );
		
		// also, if this stuff is really supposed to be paginated, we need to fix this to include
		// an offset parameter for the call to eventStreamService.getRecentActivitiesForUser
		
		User currentUser = userService.getLoggedInUser();
		
		def page = params.page;
		if( !page ) 
		{
			page = "1";
		}
		
		log.trace( "getContentHtml requested page: ${page}" );
		
		def items = new ArrayList<StreamItemBase>();
		
		UserStreamDefinition selectedStream = null;
		if( params.streamId )
		{
			Long streamId = Long.valueOf( params.streamId );
			selectedStream = userStreamDefinitionService.findStreamById( streamId );
		}
		else
		{
			selectedStream = userStreamDefinitionService.getStreamForUser( currentUser, UserStreamDefinition.DEFAULT_STREAM );
		}
		
		log.info( "selectedStream: ${selectedStream.toString()}");
		
		items = eventStreamService.getRecentActivitiesForUser( currentUser, 25 * Integer.parseInt( page ), selectedStream );

		log.info( "about to render template /activityStream" );
		
		render(template:"/activityStream",model:[activities:items]);		
	}
	

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
	public Object viewUserStream()
    {
		log.trace( "viewUserStream: " );
		
		User currentUser = userService.getLoggedInUser();
				
		String userId = params.userId;
		
		log.trace( "userId: ${userId}" );
		
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
			log.debug( "getting status updates for user ${requestedUser.userId}" );
			statusUpdatesForUser = eventStreamService.getStatusUpdatesForUser( requestedUser );
				
		}
		else 
		{
			log.debug( "NO user" );
		}
		
		Map model = [:];
		
		model.putAll( [user:currentUser, statusUpdatesForUser:statusUpdatesForUser] );
		Map sidebarCollections = populateSidebarCollections( this, currentUser );
		model.putAll( sidebarCollections );
		
		return model;
	}
	
    
    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
	def discussItem() 
	{
		log.trace( "ActivityStreamController.discussItem invoked:" );
		log.trace( "params: ${params}" );
		
		User currentUser = userService.getLoggedInUser();
		
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
		
		log.info( "Invoking OpenMeetings integration here...");

		def openMeetingsSession = openMeetingsService.launchOpenMeetingsSession( currentUser, discussItemUuid, discussItemComment, discussTargetUserId, discussItem );
		log.info( "openMeetingsSession: ${openMeetingsSession}");
		
		// return the generated room number to the user who created the room so the client-side
		// code can put the user in the conference
		// render( );	
		log.info( "calling respond()" );
		respond( [
			"newRoomID": openMeetingsSession.newRoomID, 
			"roomURL": openMeetingsSession.roomURL
		], formats: ['json']);
	}
		
	
    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
	public void shareItem()
	{
		
		log.debug(  "ActivityStreamController.shareItem invoked:");
		log.trace( "params: ${params}" );
		
		User currentUser = userService.getLoggedInuser();
		
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
		newStreamItem.actorUuid = currentUser.uuid;
		newStreamItem.targetUuid = shareTargetUser.uuid;
		newStreamItem.owner = currentUser;
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