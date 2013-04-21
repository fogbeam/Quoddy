package org.fogbeam.quoddy.transformer

import java.net.URL
import java.text.SimpleDateFormat

import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.integration.activitystream.ActivityStreamEntry
import org.fogbeam.quoddy.stream.ActivityStreamItem;

class ActivityStreamTransformerService
{
	// see note here: http://stackoverflow.com/questions/2580925/simpledateformat-parsing-date-with-z-literal
	// about parsing the RFC3339 format with the 'Z' literal
	// "yyyy-MM-dd'T'HH:mm:ss.SSSZ" -- with fractional seconds part (do we need this?)
	private static final String RFC3339_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	def userService;
	
	public ActivityStreamItem getActivity( ActivityStreamEntry entry )
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
		activity.effectiveDate = activity.published;
		activity.verb = entry.verb;
		
		// NOTE: we added "name" to EventBase, but how is it really going
		// to be used?  Do we *really* need this??
		activity.name = activity.title;
		
		// do user translation
		println "Looking for user ${entry.actor.id}";
		User userActor = userService.findUserByUserId(entry.actor.id); 
		if( userActor )
		{
			println "Found user: ${userActor}";
			activity.owner = userActor; 
		}
		else
		{
			println "failed to lookup Actor as a User in our system.";	
		}
		
		return activity;		
	}	
}
