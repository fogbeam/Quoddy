package org.fogbeam.quoddy

class EventSubscriptionService
{
	
	static expose = ['jms']
	static destination = "eventSubscriptionInQueue";
	
	def userService;
	def jmsService;
	def existDBService;
	
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
				
				EventSubscription owningSubscription = this.findByUuid( subscriptionUuid );
				if( owningSubscription == null ) 
				{
					println "Could not locate Owning Subscription with uuid: ${subscriptionUuid}";	
				}
				
				
				SubscriptionEvent subEvent = new SubscriptionEvent();
				subEvent.owner = owner;
				subEvent.owningSubscription = owningSubscription;
				subEvent.xmlUuid = eventUuid;
				subEvent.targetUuid = streamPublic.uuid;
				subEvent.name = matchedExpression;
				subEvent.effectiveDate = new Date(); // TODO: should take this from the JMS message
				
				// saving this and sending the UI notification should
				// happen in a transaction
				this.saveEvent( subEvent );
				
				println "sending message to JMS";
				// Map uiNotificationMsg = new HashMap();
				// uiNotificationMsg.creator = subEvent.owner.userId;
				// uiNotificationMsg.text = "Business Event Subscription";
				// uiNotificationMsg.targetUuid = subEvent.targetUuid;
				// msg.published = activity.published;
				// uiNotificationMsg.originTime = subEvent.dateCreated.time;
				// TODO: figure out what to do with "effectiveDate" here
				// uiNotificationMsg.effectiveDate = subEvent.dateCreated.time;
				
				// uiNotificationMsg.actualEvent = subEvent;
				
				jmsService.send( queue: 'uitestActivityQueue', /* uiNotificationMsg */ subEvent, 'standard', null );
			}
			   
			   
		}
	
	}
	
	
	public EventSubscription findByUuid( final String uuid )
	{
		EventSubscription subscription = EventSubscription.findByUuid( uuid );
		return subscription;	
	}
	
	// note: would it make sense to wrap "save and notify" into one message and
	// take advantage of Spring's method level transaction demarcation stuff?
	public void saveEvent( EventBase event )
	{
		if( ! event.save() )
		{
			println( "Saving Event FAILED");
			event.errors.allErrors.each { println it }
		}
	}
	
	public List<EventSubscription> getAllSubscriptionsForUser( final User user )
	{
		List<EventSubscription> subscriptions = new ArrayList<EventSubscription>();
		
		List<UserList> tempSubscriptions = EventSubscription.executeQuery( "select subscription from EventSubscription as subscription where subscription.owner = :owner",
			['owner':user] );
		
		if( tempSubscriptions )
		{
			subscriptions.addAll( tempSubscriptions );
		}

		return subscriptions;
	}

	
	public List<SubscriptionEvent> getRecentEventsForSubscription( final EventSubscription subscription,  final int maxCount )
	{
	
		println "getRecentEventsForSubscription: ${subscription.id}";
		
		List<SubscriptionEvent> recentEvents = new ArrayList<SubscriptionEvent>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
	
		println "Using ${cutoffDate} as cutoffDate";

	
		List<SubscriptionEvent> queryResults =
			SubscriptionEvent.executeQuery(
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
