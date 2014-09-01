package org.fogbeam.quoddy.transformer

import java.text.SimpleDateFormat

import org.fogbeam.protocol.activitystreams.ActivityStreamEntry
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.stream.ActivityStreamItem

class ActivityStreamTransformerService
{
	// see note here: http://stackoverflow.com/questions/2580925/simpledateformat-parsing-date-with-z-literal
	// about parsing the RFC3339 format with the 'Z' literal
	// "yyyy-MM-dd'T'HH:mm:ss.SSSZ" -- with fractional seconds part (do we need this?)
	private static final String RFC3339_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	def userService;
	
	public ActivityStreamItem transform( ActivityStreamEntry entry )
	{
		ActivityStreamItem activity = new ActivityStreamItem();
		
		activity.content = entry.content;
		activity.title = entry.title;
		if( entry.url )
		{
			activity.url = new URL(entry.url);
		}
		
		// do date translation
		SimpleDateFormat sdf = new SimpleDateFormat( RFC3339_DATE_FORMAT );
		String eventPublishedDate = entry.published;
		if( eventPublishedDate.endsWith( "Z" ))
		{
			eventPublishedDate = eventPublishedDate.replaceAll( "Z\$", "GMT+00:00" ); 
		}
		
		activity.published = sdf.parse( eventPublishedDate );
		// activity.effectiveDate = activity.published;
		activity.verb = entry.verb;
		
		// NOTE: we added "name" to EventBase, but how is it really going
		// to be used?  Do we *really* need this??
		activity.name = activity.title;
		
		// do user translation
		
		/* should evaluate the objectClass or whatever of the incoming Actor instance */
		if( entry.actor )
		{
			
			String actorType = entry.actor.objectType;
			
			switch( actorType )
			{
				case "UserByUserId":
					println "Looking for user ${entry.actor.id}";
				
					User userActor = userService.findUserByUserId(entry.actor.id); 
					if( userActor )
					{
						println "Found user: ${userActor}";
						activity.owner = userActor; 
						activity.actorUuid = userActor.uuid;
						activity.actorObjectType = entry.actor.objectType;
						activity.actorUrl = entry.actor.url;
						activity.actorDisplayName = entry.actor.displayName;
					}
					else
					{
						println "failed to lookup Actor as a User in our system.";	
					}
					break;
				default:
					// unknown ActorType, ignore for now
					println "Remote sent us an unknown Actor type";
				
			}
		}
		else
		{
			throw new RuntimeException( "cannot complete transform: No Actor in incoming Activity" );
		}
		
		if( entry.target )
		{
			// look up the target based on the target objectType and target id
			
			String targetObjectType = entry.target.objectType;
			switch( targetObjectType )
			{
				case "UserByUserId":
					
					println "Looking up user by id: ${entry.target.id}";
					
					User targetUser = userService.findUserByUserId(entry.target.id);
					if( targetUser )
					{
						println "setting targetUuid to user uuid: ${targetUser.uuid}";
						activity.targetUuid = targetUser.uuid;
						activity.targetObjectType = "User";
					}
					else
					{
						println "failed to lookup Target as a User in our system.";
					}
					
					break;
				default:
					println "Remote sent us an unknown Target type!";
					break;
				
			}
			
		}
		else
		{
			throw new RuntimeException( "cannot complete transform: No Target in incoming Activity" );
		}

				
		if( entry.object )
		{
			activity.objectClass = entry.object.objectType;
			activity.objectObjectType = entry.object.objectType;
			activity.objectUuid = entry.object.id;
			activity.objectSummary = entry.object.summary;
			activity.objectContent = entry.object.content;
			activity.objectUrl = entry.object.url;
		}		
		else
		{
			throw new RuntimeException( "cannot complete transform: No Object in incoming Activity" );
		}

		
		return activity;		
	}	
}
