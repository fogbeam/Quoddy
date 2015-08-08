package org.fogbeam.quoddy

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.XML
import groovyx.net.http.RESTClient

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.profile.ContactAddress
import org.fogbeam.quoddy.profile.Profile
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
	def emailService;
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
		
		// prereq... instantiate a RESTClient and generate OM session ID and login
		// TODO: make this URL a configurable item
		def client = new RESTClient( 'http://demo2.fogbeam.org:5080/' )
		
		// call getSession
		def resp = client.get( path : 'openmeetings/services/UserService/getSession', contentType:XML );
		String sid = resp.data.return.session_id;
		println "sessionId: $sid";

		// TODO: deal with this username/password properly...
		// call login using the SID from getSession
		resp = client.get( path : 'openmeetings/services/UserService/loginUser', contentType:XML, query: [ SID:sid, username:'prhodes',
																												userpass:'3nothing' ] );
		
		/**
		 
		    NOTE: We need to decide how to deal with the user creating the discussion.  Do we create the room using that
		    user's credentials? Or do we create the room using a single "system" OM account?  If the latter, how
		    do we make sure we set the creating user as the room moderator? Or does the room in question even
		    need moderation?  And how does the room get deleted after we're done with it?   And should we give
		    the user the option to either pick an existing room OR create a temporary room?  
		 */
																											
		/****	first step:  Create a new demo room */
																											
		resp = client.get( path : 'openmeetings/services/RoomService/addRoomWithModeration', contentType: XML,
			query:[ SID:sid, name: "Test Room", roomtypes_id: 1, comment: "", numberOfPartizipants:25, ispublic:true, appointment:false, isDemoRoom:false, demoTime:0, isModeratedRoom:false ] );
		
		int newRoomID = -1;
		String roomURL = "http://demo2.fogbeam.org:5080/openmeetings/#room/";
		
		if( !( resp.status == 200 )) // HTTP response code; 404 means not found, etc.
		{
			throw new Exception( "Not 200 HTTP Response!   ${resp.status}" );
		}
		else
		{
			String strNewRoomID = resp.data.return;
			println "newRoomID: $strNewRoomID";
			newRoomID = Integer.parseInt( strNewRoomID );
			roomURL = roomURL + newRoomID;
			 
			// then generate invitation hashes for all of the invited users
			
			resp = client.get( path : 'openmeetings/services/RoomService/getInvitationHash', contentType: XML,
				query:[ SID:sid, username:discussTargetUserId, room_id:newRoomID, isPasswordProtected:false, invitationpass:"", valid:1, validFromDate:"", validFromTime:"",
												validToDate:"", validToTime:"" ] );
			
			// println "response from getInvitationHash: ${resp.data}";
			
			if( !( resp.status == 200 )) // HTTP response code; 404 means not found, etc.
			{
				throw new Exception( "Not 200 HTTP Response!   ${resp.status}" );
			}
			else
			{
				
				def hash = resp.data.return;
				println "Invitation Hash: $hash";
				
				def inviteUrl = "http://demo2.fogbeam.org:5080/openmeetings/?invitationHash=$hash";
				
				// println "URL: $inviteUrl";
				
				// After creating has, and then what... ???  email the hashes to the usesr?  IM them?  Post to their Quoddy
				// stream? What??  Should the incoming request tell us which contact mechanism to use?  
				
				// for now we're just going to hardcode this to use email, based on the user's primary email address.
				User targetUser = userService.findUserByUserId( discussTargetUserId );
				Profile targetProfile = targetUser.profile;
				Set<ContactAddress> targetContactAddresses = targetProfile.contactAddresses;
				ContactAddress toEmail = targetContactAddresses.find { it.serviceType == ContactAddress.EMAIL && it.primaryInType == true };
				
				User creatingUser = userService.findUserByUserId( session.user.userId );
				Profile creatingUserProfile = creatingUser.profile;
				Set<ContactAddress> creatingUserContactAddresses = creatingUserProfile.contactAddresses;
				ContactAddress senderEmail = creatingUserContactAddresses.find { it.serviceType == ContactAddress.EMAIL && it.primaryInType == true };

				
				// email this invitation to the user.
				StringBuffer emailBodyBuffer = new StringBuffer();
				
				
				emailBodyBuffer.append( "${creatingUser.displayName} is inviting you to join a video conference.\n\n" );
				emailBodyBuffer.append( "Invitation comment: ${discussItemComment}\n\n");
				emailBodyBuffer.append( "This conference pertains to the following item: ${discussItemUuid}\n\n");
				emailBodyBuffer.append( "To join the conference open this link:   ${inviteUrl}\n\n\n" );
				emailBodyBuffer.append( "---------------------------------------------------------------------------" );
				String emailBody = emailBodyBuffer.toString();
				
				emailService.deliverEmail( toEmail, senderEmail, "Video conference invitation from ${creatingUser.displayName}", emailBody );
				
			}			
		}
				
		// return the generated room number to the user who created the room so the client-side
		// code can put the user in the conference
		// render( );	
		render(contentType: 'text/json') {[
			newRoomID: newRoomID, 
			roomURL:roomURL
		]}
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