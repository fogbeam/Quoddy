package org.fogbeam.quoddy

class EventSubscriptionService
{
	
	static expose = ['jms']
	static destination = "eventSubscriptionInQueue";
	
	def userService;
	def jmsService;
	
	def onMessage( msg ) 
	{
		
		// TODO: we need to pass over a subscription name, or id, or something
		// so we can lookup the "owning subscription" for this and attach it.
		// we'll need this later when doing "display by subscription."
		
		
		// create an event for this, and store all the attributes needed
		// to pull this into the user event stream when retrieved later.
		// save one of these for every registered subscriber to this 
		// event.
		String subscribers = msg.getStringProperty('subscribers');
		String eventUuid = msg.getStringProperty( 'eventUuid' );
		String matchedExpression = msg.getStringProperty( 'matchedExpression' );
		ShareTarget streamPublic = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
		if( subscribers != null && !subscribers.isEmpty() )
		{
			println "Subscribers: ${subscribers}";
			
			List<String> subscriberList = subscribers.tokenize();
			for( String subscriber : subscriberList ) {
			
				User owner = userService.findUserByUserId( subscriber );
				
				SubscriptionEvent subEvent = new SubscriptionEvent();
				subEvent.owner = owner;
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
	
		println "getRecentEventsForSubscription: ${subscription.id} - ${maxCount}";
		
		List<SubscriptionEvent> recentEvents = new ArrayList<SubscriptionEvent>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -600 );
		Date cutoffDate = cal.getTime();
	
		println "Using ${cutoffDate} as cutoffDate";

	
		List<SubscriptionEvent> queryResults =
			SubscriptionEvent.executeQuery(
					"select activity from Activity as activity, UserList as ulist where activity.dateCreated >= :cutoffDate " +
					" and activity.owner in elements(ulist.members) and ulist = :thelist order by activity.dateCreated desc",
			  ['cutoffDate':cutoffDate, 'thelist':list], ['max': maxCount ]);
			
		if( queryResults )
		{
			println "adding ${queryResults.size()} events read from DB";
			recentEvents.addAll( queryResults );
		}
	
		return recentEvents;
			
	}
		
}
