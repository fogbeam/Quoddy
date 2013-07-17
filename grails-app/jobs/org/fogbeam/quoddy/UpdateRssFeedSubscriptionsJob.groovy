package org.fogbeam.quoddy



import grails.util.GrailsNameUtils
import net.fortuna.ical4j.model.component.*

import org.apache.http.HttpEntity
import org.apache.http.HttpException
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.Parser
import org.apache.tika.sax.BodyContentHandler
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.RssFeedItem
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.subscription.RssFeedSubscription

import com.sun.syndication.feed.synd.SyndEntry
import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.io.XmlReader


class UpdateRssFeedSubscriptionsJob 
{
	
	def group = "MyGroup";
	def volatility = false;
	def jmsService;
	def eventStreamService;
	def rssFeedItemService;
	
	static triggers = {
		
	}
	
    def execute() 
	{
		
		ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
		
		
		println "executing UpdateRssFeedSubscriptionsJob";
		
		// TODO: switch this to a service call
     	// get a list of all the active CalendarFeedSubscription objects
		List<RssFeedSubscription> allSubscriptions = RssFeedSubscription.findAll();
		
		allSubscriptions.each {
			RssFeedSubscription sub = it;
			
			println "processing subscription for url: ${sub.url}";
			
			URL feedUrl = new URL(sub.url);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = null;
			XmlReader reader = null;
			
			
			try
			{
				reader = new XmlReader(feedUrl)
				feed = input.build(reader);
				println( "Feed: ${feed.getTitle()}" );
				
				List<SyndEntry> entries = feed.getEntries();
				
				println( "processing ${entries.size()} entries!" );
				int good = 0;
				int bad = 0;
				
				for( SyndEntry entry in entries )
				{
					String linkUrl = entry.getLink();
					String linkTitle = entry.getTitle();
					
					RssFeedItem testForExisting = rssFeedItemService.findRssFeedItemByUrlAndSubscription( linkUrl, sub );
					if( testForExisting != null )
					{
						println( "An RssFeedItem entry for this link (${linkUrl}) already exists. Skipping" );							
						continue;
					}
					else
					{	
						
						println( "creating and adding entry for link: ${linkUrl} with title: ${linkTitle}" );
			
						// Entry newEntry = new Entry( url: linkUrl, title: linkTitle, submitter: anonymous );
						RssFeedItem rssFeedItem = new RssFeedItem();
						
						
						// TODO: figure out what fields this thing should have and how to populate those fields
						// with the data we have here.
						HttpClient client = new DefaultHttpClient();
						
						//establish a connection within 10 seconds
						// client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
						HttpGet httpget = new HttpGet(linkUrl);
						InputStream httpStream = null;
						try 
						{
							HttpResponse httpResponse = client.execute(httpget);
							HttpEntity entity = httpResponse.getEntity();
							
							httpStream = entity.content;
							
						} 
						catch ( HttpException he) {
						   log.error("Http error connecting to '" + url + "'");
						   log.error(he.getMessage());
						   continue;
						} 
						catch (IOException ioe){
						   // ioe.printStackTrace();
						   log.error("Unable to connect to '" + url + "'");
						   log.error( ioe );
						   continue;
						}
				   
						// extract text with Tika
						try
						{
							org.xml.sax.ContentHandler textHandler = new BodyContentHandler(-1);
							Metadata metadata = new Metadata();
							Parser parser = new AutoDetectParser();
							parser.parse(httpStream, textHandler, metadata);
						
							rssFeedItem.name = metadata.get(Metadata.TITLE );
							rssFeedItem.title = metadata.get(Metadata.TITLE );
							rssFeedItem.description = metadata.get( Metadata.DESCRIPTION );
							rssFeedItem.linkUrl = linkUrl;
							rssFeedItem.leadingSnippet = textHandler.toString()?.replaceAll('\\s+', ' ').substring(0, 255);
							rssFeedItem.datePublished = null; 
							rssFeedItem.effectiveDate = new Date(); // right now
							rssFeedItem.targetUuid  = streamPublic.uuid;
							rssFeedItem.owner = sub.owner;
							rssFeedItem.owningSubscription = sub;
							
							
							rssFeedItem = rssFeedItemService.saveItem( rssFeedItem );
							
						}
						catch( Exception e )
						{
							e.printStackTrace();
							rssFeedItem = null;
						}
						

						
						
						if( rssFeedItem )
						{
							good++;
							log.debug( "saved new RssFeedItem entry with id: ${rssFeedItem.id}" );
							
							
							ActivityStreamItem activity = new ActivityStreamItem(content:"RSSFeedItem");
							
							activity.title = "RssFeedItem Received";
							activity.url = new URL( "http://example.com/" );
							activity.verb = "rss_feed_item_received";
							activity.published = new Date(); // set published to "now"
							activity.targetUuid = streamPublic.uuid;
							activity.owner = sub.owner;
							activity.streamObject = rssFeedItem;
							activity.objectClass = GrailsNameUtils.getShortName( rssFeedItem.class );
							
							// NOTE: we added "name" to StreamItemBase, but how is it really going
							// to be used?  Do we *really* need this??
							activity.name = activity.title;
							
							// activity.effectiveDate = activity.published;
							
							eventStreamService.saveActivity( activity );
							
							
							// send JMS message saying "new entry submitted"
							def newContentMsg = [msgType:'NEW_RSS_FEED_ITEM', activityId:activity.id, activityUuid:activity.uuid ];
							
							println "sending messages to JMS";
							log.debug( "sending new entry message to JMS quoddySearchQueue" );
							
							// send message to request search indexing
							jmsService.send( queue: 'quoddySearchQueue', newContentMsg, 'standard', null );
							
			

							// TODO: send a JMS message for UI update notification
							// sendJMSMessage("uiNotificationQueue", newEntryMessage );
	
						
						}
						else
						{
							bad++;
							// failed to save newEntry
							log.debug( "Failed processing RssFeedItem!" );
						}
						
					}
				}
				
				log.debug( "Good entries: ${good}, bad entries:${bad}" );
				
			}
			catch( Exception e )
			{
				println "Caught Exception in RssFeedSubscription Processing Loop!";
				
				e.printStackTrace();
				
				println "Continuing to next RssFeedSubscription";
			}
			finally 
			{
				if( reader != null ) 
				{
					reader.close();	
				}	
			}
			
			
		}
		
	}
}