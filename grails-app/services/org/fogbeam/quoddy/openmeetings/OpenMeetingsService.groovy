package org.fogbeam.quoddy.openmeetings

import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.profile.ContactAddress
import org.fogbeam.quoddy.profile.Profile

import groovyx.net.http.RESTClient

class OpenMeetingsService 
{
	def grailsApplication;
	
	def emailService;
	def userService;
	
	
	def launchOpenMeetingsSession( def currentUser, def discussItemUuid, def discussItemComment, def discussTargetUserId, def discussItem  )
	{
		// prereq... instantiate a RESTClient and generate OM session ID and login
		String openMeetingsEndpoint = grailsApplication.config.urls.openmeetings.endpoint;
		
		log.info( "got openMeetingsEndpoint as: ${openMeetingsEndpoint}");
		
		def client = new RESTClient( openMeetingsEndpoint )
		
		// call getSession
		def resp = client.get( path : 'openmeetings/services/UserService/getSession', contentType:"text/xml" );
		
		log.info( "got resp: ${resp}");
		log.info( "resp.data: ${resp.data}" );
		
		String sid = resp.data.return.session_id;
		
		
		
		log.info( "sessionId: $sid");

		// TODO: deal with this username/password properly...
		// call login using the SID from getSession
		String omUsername = grailsApplication.config.credentials.om.username;
		String omPassword = grailsApplication.config.credentials.om.password;
		resp = client.get( path : 'openmeetings/services/UserService/loginUser', contentType:"text/xml", query: [ SID:sid, username:omUsername, userpass:omPassword ] );
		
		def openMeetingsSession = [:];
		
		/**
		 
			NOTE: We need to decide how to deal with the user creating the discussion.  Do we create the room using that
			user's credentials? Or do we create the room using a single "system" OM account?  If the latter, how
			do we make sure we set the creating user as the room moderator? Or does the room in question even
			need moderation?  And how does the room get deleted after we're done with it?   And should we give
			the user the option to either pick an existing room OR create a temporary room?
		 */
																											
		/****	first step:  Create a new demo room */
																											
		resp = client.get( path : 'openmeetings/services/RoomService/addRoomWithModeration', contentType: "text/xml",
			query:[ SID:sid, name: "Test Room", roomtypes_id: 1, comment: "", numberOfPartizipants:25, ispublic:true, appointment:false, isDemoRoom:false, demoTime:0, isModeratedRoom:false ] );
		
		log.info( "resp: ${resp}" );
		log.info( "resp.data: ${resp.data}");
		
		
		int newRoomID = -1;
		String roomURL = "${openMeetingsEndpoint}openmeetings/#room/";
		
		if( !( resp.status == 200 )) // HTTP response code; 404 means not found, etc.
		{
			throw new Exception( "Not 200 HTTP Response!   ${resp.status}" );
		}
		else
		{
			String strNewRoomID = resp.data.return;
			log.info( "newRoomID: $strNewRoomID");
			newRoomID = Integer.parseInt( strNewRoomID );
			roomURL = roomURL + newRoomID;
			
			openMeetingsSession.newRoomID = newRoomID;
			openMeetingsSession.roomURL = roomURL;
			 
			// then generate invitation hashes for all of the invited users
			
			resp = client.get( path : 'openmeetings/services/RoomService/getInvitationHash', contentType: "text/xml",
				query:[ SID:sid, username:discussTargetUserId, room_id:newRoomID, isPasswordProtected:false, invitationpass:"", valid:1, validFromDate:"", validFromTime:"",
												validToDate:"", validToTime:"" ] );
			
			log.trace( "response from getInvitationHash: ${resp.data}");
			
			if( !( resp.status == 200 )) // HTTP response code; 404 means not found, etc.
			{
				throw new Exception( "Not 200 HTTP Response!   ${resp.status}" );
			}
			else
			{
				
				def hash = resp.data.return;
				log.info( "Invitation Hash: $hash" );
				
				def inviteUrl = "${openMeetingsEndpoint}openmeetings/?invitationHash=$hash";
				
				log.info( "URL: $inviteUrl");
				
				// After creating has, and then what... ???  email the hashes to the usesr?  IM them?  Post to their Quoddy
				// stream? What??  Should the incoming request tell us which contact mechanism to use?
				
				// for now we're just going to hardcode this to use email, based on the user's primary email address.
				User targetUser = userService.findUserByUserId( discussTargetUserId );
				Profile targetProfile = targetUser.profile;
				Set<ContactAddress> targetContactAddresses = targetProfile.contactAddresses;
				ContactAddress toEmail = targetContactAddresses.find { it.serviceType == ContactAddress.EMAIL && it.primaryInType == true };
				
				User creatingUser = userService.findUserByUserId( currentUser.userId );
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
				
				try
				{
					log.info( "about to send invitation email!")

					
					log.info( "toEmail: ${toEmail}");
					log.info( "senderEmail: ${senderEmail}");
					
					
					emailService.deliverEmail( toEmail, senderEmail, "Video conference invitation from ${creatingUser.displayName}", emailBody );
				}
				catch( Exception e )
				{
				   // TODO: turn this into a notification to the user
				   e.printStackTrace();
				   log.error( "Error sending invitation email", e );
				}
				
			}
		}
		
		return openMeetingsSession;		
	}
}
