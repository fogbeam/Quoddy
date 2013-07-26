package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.ShareTarget;
import org.fogbeam.quoddy.stream.StreamItemBase;
import org.fogbeam.quoddy.stream.BusinessEventSubscriptionItem;
import org.fogbeam.quoddy.subscription.BusinessEventSubscription;

class EventSubscriptionService
{
	
	static expose = ['jms']
	static destination = "eventSubscriptionInQueue";
	
	def userService;
	def jmsService;
	def existDBService;
	def eventStreamService;
	
	def onMessage( msg ) 
	{
		
		// create an event for this, and store all the attributes needed
		// to pull this into the user event stream when retrieved later.
		// save one of these for every registered subscriber to this 
		// event.
		String subscribers = msg.getStringProperty('subscribers');
		String subscribersWithSubId = msg.getStringProperty( "subscribersWithSubId" );
		String eventUuid = msg.getStringProperty( 'eventUuid' );
		String matchedExpression = msg.getStringProperty( 'matchedExpression' );
		String summary = msg.getStringProperty( "summary" );
		
		println "subscribersWithSubId: ${subscribersWithSubId}";
		
		ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
		if( subscribersWithSubId != null && !subscribersWithSubId.isEmpty() )
		{
			println "Subscribers: ${subscribers}";
			
			List<String> subscriberList = subscribersWithSubId.tokenize( " " );
			for( String subscriber : subscriberList ) {
				List<String> subParts = subscriber.tokenize( ";" );
				String subscriberUuid = subParts.get(0);
				String subscriptionUuid = subParts.get(1);
				
				User owner = userService.findUserByUuid( subscriberUuid );
				if( owner == null ) 
				{
					println "Could not find Subscription Owner: ${subscriberUuid}";	
				}
				
				BusinessEventSubscription owningSubscription = this.findByUuid( subscriptionUuid );
				if( owningSubscription == null ) 
				{
					println "Could not locate Owning Subscription with uuid: ${subscriptionUuid}";	
				}
				
				
				BusinessEventSubscriptionItem subEvent = new BusinessEventSubscriptionItem();
				subEvent.owner = owner;
				subEvent.owningSubscription = owningSubscription;
				subEvent.xmlUuid = eventUuid;
				subEvent.targetUuid = streamPublic.uuid;
				subEvent.name = matchedExpression;
				subEvent.effectiveDate = new Date(); // TODO: should take this from the JMS message
				if( summary != null )
				{
					subEvent.summary = summary;
				}
				else 
				{
					subEvent.summary = "";
				}

				// saving this and sending the UI notification should
				// happen in a transaction
				this.saveEvent( subEvent );
				
				
				// if the event update was successful
				ActivityStreamItem activity = new ActivityStreamItem(content:subEvent.summary);
				
				activity.title = "Business Event Subscription Item Received";
				activity.url = new URL( "http://www.example.com" );
				activity.verb = "business_event_subscription_item_received";
				activity.published = new Date(); // set published to "now"
				activity.targetUuid = streamPublic.uuid;
				activity.owner = owner;
				activity.streamObject = subEvent;
				activity.objectClass = subEvent.class.getName();
				
				// NOTE: we added "name" to StreamItemBase, but how is it really going
				// to be used?  Do we *really* need this??
				activity.name = activity.title;
				
				// activity.effectiveDate = activity.published;
				
				eventStreamService.saveActivity( activity );
				
				
				def newContentMsg = [msgType:'NEW_BUSINESS_EVENT_SUBSCRIPTION_ITEM', activityId:activity.id, activityUuid:activity.uuid ];
				
				println "sending messages to JMS";
				
				// send message to request search indexing
				sendJMSMessage("quoddySearchQueue", newContentMsg );
			
				
				// send message for UI notification
				jmsService.send( queue: 'uitestActivityQueue', subEvent, 'standard', null );
				
			}
			   
			   
		}
	
	}
	
	
	public BusinessEventSubscription findByUuid( final String uuid )
	{
		BusinessEventSubscription subscription = BusinessEventSubscription.findByUuid( uuid );
		return subscription;	
	}
	
	// note: would it make sense to wrap "save and notify" into one message and
	// take advantage of Spring's method level transaction demarcation stuff?
	public void saveEvent( StreamItemBase event )
	{
		if( ! event.save() )
		{
			println( "Saving Event FAILED");
			event.errors.allErrors.each { println it }
		}
	}
	
	public List<BusinessEventSubscription> getAllSubscriptionsForUser( final User user )
	{
		List<BusinessEventSubscription> subscriptions = new ArrayList<BusinessEventSubscription>();
		
		List<UserList> tempSubscriptions = BusinessEventSubscription.executeQuery( "select subscription from BusinessEventSubscription as subscription where subscription.owner = :owner",
			['owner':user] );
		
		if( tempSubscriptions )
		{
			subscriptions.addAll( tempSubscriptions );
		}

		return subscriptions;
	}

	
	public List<BusinessEventSubscriptionItem> getRecentEventsForSubscription( final BusinessEventSubscription subscription,  final int maxCount )
	{
	
		println "getRecentEventsForSubscription: ${subscription.id}";
		
		List<BusinessEventSubscriptionItem> recentEvents = new ArrayList<BusinessEventSubscriptionItem>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
	
		println "Using ${cutoffDate} as cutoffDate";

	
		List<BusinessEventSubscriptionItem> queryResults =
			BusinessEventSubscriptionItem.executeQuery(
					"select subevent from SubscriptionEvent as subevent where subevent.dateCreated >= :cutoffDate " +
					" and subevent.owningSubscription = :owningSub order by subevent.dateCreated desc",
			  ['cutoffDate':cutoffDate, 'owningSub':subscription], ['max': maxCount ]);
			
		if( queryResults )
		{
			println "adding ${queryResults.size()} events read from DB";
			recentEvents.addAll( queryResults );
			
			recentEvents.each {
				existDBService.populateSubscriptionEventWithXmlDoc( it );
			} 
		}
		else {
			println "NO results found!";	
		}
	
		return recentEvents;
			
	}
		
}
