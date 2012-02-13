package org.fogbeam.quoddy


import java.util.Date;

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.ComponentList
import net.fortuna.ical4j.model.component.*

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient


class UpdateCalendarFeedsJob 
{
	
	def group = "MyGroup";
	def volatility = false;
	
	static triggers = {
	}
	
    def execute() 
	{
		println "executing UpdateCalendarFeedsJob";
		
     	// get a list of all the active CalendarFeed objects
		List<CalendarFeed> allFeeds = CalendarFeed.findAll();
		
		if( allFeeds != null && allFeeds.size > 0 )
		{
			// for each feed:
			allFeeds.each { feed ->
				 
				// download the contents of the feed, and parse out all of the VEVENTs
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(feed.url);
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null) 
				{
					InputStream instream = entity.getContent();
					CalendarBuilder builder = new CalendarBuilder();
					
					net.fortuna.ical4j.model.Calendar calendar = builder.build(instream);
					
					ComponentList aList = calendar.getComponents("VEVENT");
					
					for( VEvent comp : aList )
					{
						// for each VEVENT create a CalendarEvent instance with the CalendarFeed owner
						// as the owner of the VEVENT
						
						// check that we don't already have this event (using the provided uid)
						String eventUid = comp.uid.value; 
						
						// TODO: query for events with this uid, if we find one, don't try to
						// persist this event.  Phase 2, add a check for the "last modified"
						// date to see if the event has been modified since we originally
						// saw it.  
						List<CalendarEvent> temp = CalendarEvent.executeQuery( "select calEvent from CalendarEvent as calEvent where calEvent.uid = :eventUid",
							['eventUid':eventUid] );
						
						println "Temp: ${temp}";
						
						if( !(temp.isEmpty()))
						{
							// for now, just skip the rest of the loop
							println "skipping eventUid: ${eventUid}, as we already have it";
							continue;
						}
						else
						{
							println "proceeding to create CalendarEvent with uid: ${eventUid}";	
						}
						
						CalendarEvent event = new CalendarEvent();
						event.uid = eventUid;
						event.owner = feed.owner;
						event.startDate = comp.startDate?.date;
						event.endDate = comp.endDate?.date;
						String description = comp.description?.value;
						description = (description.length() > 1000 ? description[0..999] : description )
						event.description = description;
						event.status = comp.status.value;
						event.summary = comp.summary.value;
						event.dateEventCreated = comp.dateStamp.date;
						event.lastModified = comp.lastModified?.date;
						event.url = comp.url?.value;
						event.location = comp.location?.value;
						event.uid = comp.uid.value;
							
						event.geoLat = comp.geographicPos?.getLatitude();
						event.geoLong = comp.geographicPos?.getLongitude();
						
						// note: effectiveDate is a new attribute on EventBase.  It
						// represents the point in time that the event "becomes real"
						// for purpose of sorting events into the user's stream(s).  For
						// a calendar event that we might receive on an iCal feed days
						// or weeks in advance, the effectiveDate is clearly not "now"
						// since the user probably wants to see the event in his/her stream
						// some brief period of time before the event is set to take place.
						// actually, on second thought, the confirmation status might affect
						// this, but for now let's go with the idea that the effectiveDate
						// is some delta applied to the startDate
						Calendar cal = Calendar.getInstance();
						cal.setTime( comp.startDate?.date ); 
						cal.add(Calendar.HOUR_OF_DAY, -8 );
						Date effectiveDate = cal.getTime();
						event.effectiveDate = effectiveDate;
						
						
						// NOTE: we added "name" to EventBase, but how is it really going
						// to be used?  Do we *really* need this??
						event.name = "Calendar Event";
						
						ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
						event.targetUuid = streamPublic.uuid;
						
						if( !event.save(flush:true))
						{
							println( "Saving CalendarEvent FAILED");
							event.errors.allErrors.each { println it };
						}
						else
						{
							println "successfully saved CalendarEvent.  Uid: ${event.uid}, Id: ${event.id}, Uuid: ${event.uuid}";	
						}
						
					}
				}
			}
		}
		else
		{
			println "no feeds";	
		}
		
	}
}