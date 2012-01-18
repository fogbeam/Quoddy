package org.fogbeam.quoddy.transformer

import java.net.URL
import java.text.SimpleDateFormat

import org.fogbeam.quoddy.Activity
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.integration.activitystream.ActivityStreamEntry

class ActivityStreamTransformerService
{
	// see note here: http://stackoverflow.com/questions/2580925/simpledateformat-parsing-date-with-z-literal
	// about parsing the RFC3339 format with the 'Z' literal
	// "yyyy-MM-dd'T'HH:mm:ss.SSSZ" -- with fractional seconds part (do we need this?)
	private static final String RFC3339_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	def userService;
	
	public Activity getActivity( ActivityStreamEntry entry )
	{
		Activity activity = new Activity();
		
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
		
		activity.verb = entry.verb;
		
		// do user translation
		println "Looking for user ${entry.actor.id}";
		User userActor = userService.findUserByUserId(entry.actor.id); 
		if( userActor )
		{
			println "Found user: ${userActor}";
			activity.userActor = userActor; 
		}
		else
		{
			println "failed to lookup Actor as a User in our system.";	
		}
		
		return activity;		
	}	
}
