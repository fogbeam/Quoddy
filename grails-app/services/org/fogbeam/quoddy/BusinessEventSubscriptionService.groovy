package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.BusinessEventSubscriptionItem
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.StreamItemBase
import org.fogbeam.quoddy.stream.constants.EventTypes
import org.fogbeam.quoddy.subscription.BusinessEventSubscription

class BusinessEventSubscriptionService
{
	
	static expose = ['jms']
	static destination = "eventSubscriptionInQueue";
	
	
	def userService;
	def jmsService;
	def existDBService;
	def eventStreamService;
	
	def onMessage( msg ) 
	{
		
		// TODO: run this content through Stanbol, and insert any relevant
		// triples into the Jena KB.  We especially want to make sure things like
		// customer numbers, part numbers, and the like get recorded. 
		
		
		
		
		// create an event for this, and store all the attributes needed
		// to pull this into the user event stream when retrieved later.
		// save one of these for every registered subscriber to this 
		// event.
		String subscribers = msg.getStringProperty('subscribers');
		String subscribersWithSubId = msg.getStringProperty( "subscribersWithSubId" );
		String eventUuid = msg.getStringProperty( 'eventUuid' );
		String matchedExpression = msg.getStringProperty( 'matchedExpression' );
		String summary = msg.getStringProperty( "summary" );
		
		log.debug( "subscribersWithSubId: ${subscribersWithSubId}");
		
		ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
		if( subscribersWithSubId != null && !subscribersWithSubId.isEmpty() )
		{
			log.debug( "Subscribers: ${subscribers}" );
			
			List<String> subscriberList = subscribersWithSubId.tokenize( " " );
			for( String subscriber : subscriberList ) {
				List<String> subParts = subscriber.tokenize( ";" );
				String subscriberUuid = subParts.get(0);
				String subscriptionUuid = subParts.get(1);
				
				User owner = userService.findUserByUuid( subscriberUuid );
				if( owner == null ) 
				{
					log.warn( "Could not find Subscription Owner: ${subscriberUuid}");	
				}
				
				BusinessEventSubscription owningSubscription = this.findByUuid( subscriptionUuid );
				if( owningSubscription == null ) 
				{
					log.warn( "Could not locate Owning Subscription with uuid: ${subscriptionUuid}");	
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
				activity.actorObjectType = "BusinessEventSubscription";
				activity.actorUuid = owningSubscription.uuid;
				activity.targetObjectType = "STREAM_PUBLIC";
				activity.published = new Date(); // set published to "now"
				activity.targetUuid = streamPublic.uuid;
				activity.owner = owner;
				activity.streamObject = subEvent;
				activity.objectClass = EventTypes.BUSINESS_EVENT_SUBSCRIPTION_ITEM.name;				
				
				
				// NOTE: we added "name" to StreamItemBase, but how is it really going
				// to be used?  Do we *really* need this??
				activity.name = activity.title;
				
				// activity.effectiveDate = activity.published;
				
				eventStreamService.saveActivity( activity );
				
				
				def newContentMsg = [msgType:'NEW_BUSINESS_EVENT_SUBSCRIPTION_ITEM', activityId:activity.id, activityUuid:activity.uuid ];
				
				log.debug( "sending messages to JMS" );
				
				// send message to request search indexing
				jmsService.send("quoddySearchQueue", newContentMsg );
				
				// send message for UI notification
				jmsService.send( queue: 'uitestActivityQueue', activity, 'standard', null );
				
			}
			   
			   
		}
	
	}
	
	
	public BusinessEventSubscription findByUuid( final String uuid )
	{
		BusinessEventSubscription subscription = BusinessEventSubscription.findByUuid( uuid );
		return subscription;	
	}

	public void saveSubscription( final BusinessEventSubscription subscriptionToSave ) 
	{
		if( !subscriptionToSave.save() )
		{
			log.error( "Saving BusinessEventSubscription FAILED");
			subscriptionToSave.errors.allErrors.each { log.debug( it ) };
		}

	}
		
	// note: would it make sense to wrap "save and notify" into one message and
	// take advantage of Spring's method level transaction demarcation stuff?
	public void saveEvent( StreamItemBase event )
	{
		if( ! event.save() )
		{
			log.error( "Saving Event FAILED");
			event.errors.allErrors.each { log.debug(it) }
		}
	}
	
	public List<BusinessEventSubscription> getAllSubscriptionsForUser( final User user )
	{
		log.debug( "getAllSubscriptionsForUser() called for user ${user.toString()}");
		
		List<BusinessEventSubscription> subscriptions = new ArrayList<BusinessEventSubscription>();
		
		List<BusinessEventSubscription> tempSubscriptions = BusinessEventSubscription.executeQuery( "select subscription from BusinessEventSubscription as subscription where subscription.owner = :owner",
			['owner':user] );
		
		if( tempSubscriptions != null )
		{
			subscriptions.addAll( tempSubscriptions );
		}

		return subscriptions;
	}

	
	public List<ActivityStreamItem> getRecentEventsForSubscription( final BusinessEventSubscription subscription,  final int maxCount )
	{
	
		log.debug( "getRecentEventsForSubscription: ${subscription.id}" );
		
		List<ActivityStreamItem> recentEvents = new ArrayList<ActivityStreamItem>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
	
		log.debug( "Using ${cutoffDate} as cutoffDate" );

	
		List<ActivityStreamItem> queryResults =
			ActivityStreamItem.executeQuery(
					"select actItem from ActivityStreamItem as actItem where actItem.dateCreated >= :cutoffDate " +
					"and actItem.streamObject in ( select sib from StreamItemBase as sib where sib.owningSubscription = :owningSub) order by actItem.dateCreated desc",
			  ['cutoffDate':cutoffDate, 'owningSub':subscription], ['max': maxCount ]);
			
		if( queryResults )
		{
			log.debug( "adding ${queryResults.size()} events read from DB" );
			recentEvents.addAll( queryResults );
			
			recentEvents.each {
				existDBService.populateSubscriptionEventWithXmlDoc( it.streamObject );
			} 
		}
		else {
			log.debug( "NO results found!" );	
		}
	
		return recentEvents;
			
	}
		
}
