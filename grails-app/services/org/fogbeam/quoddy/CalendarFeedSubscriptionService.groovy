package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.subscription.CalendarFeedSubscription

class CalendarFeedSubscriptionService
{
	
	public CalendarFeedSubscription findById( final Long id )
	{
		CalendarFeedSubscription subscription = CalendarFeedSubscription.findById( id );
		
		return subscription;
	}
	
	public CalendarFeedSubscription findByUuid( final String uuid )
	{
		CalendarFeedSubscription subscription = CalendarFeedSubscription.findByUuid( uuid );
		
		return subscription;

	}
	
	public void attachAndSave( final CalendarFeedSubscription subscriptionToSave )
	{
		// re-attach to Hibernate session
		if( !subscriptionToSave.isAttached())
		{
			subscriptionToSave.attach();
		}
		
		saveSubscription( subscriptionToSave );
	}
	
	public CalendarFeedSubscription saveSubscription( final CalendarFeedSubscription subscription )
	{
		if( !subscription.save(flush:true) )
		{
			log.error( "Saving CalendarFeedSubscription FAILED");
			subscription.errors.allErrors.each { log.error( it.toString() ) };
		}
		
		
		return subscription;
	}
	
	public List<CalendarFeedSubscription> getAllSubscriptionsForUser( final User user )
	{
		List<CalendarFeedSubscription> subscriptions = new ArrayList<CalendarFeedSubscription>();
		
		List<CalendarFeedSubscription> tempSubscriptions = CalendarFeedSubscription.executeQuery( "select subscription from CalendarFeedSubscription as subscription where subscription.owner = :owner",
			['owner':user] );
		
		if( tempSubscriptions )
		{
			subscriptions.addAll( tempSubscriptions );
		}

		return subscriptions;	
	}
	
	public List<ActivityStreamItem> getRecentItemsForSubscription( final CalendarFeedSubscription subscription,  final int maxCount )
	{
	
		log.debug( "CalendarFeedSubscriptionService.getRecentItemsForSubscription: ${subscription.id}");
		
		List<ActivityStreamItem> recentEvents = new ArrayList<ActivityStreamItem>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
	
		log.debug(  "Using ${cutoffDate} as cutoffDate");

	
		List<ActivityStreamItem> queryResults =
			ActivityStreamItem.executeQuery(
					"select actItem from ActivityStreamItem as actItem where actItem.dateCreated >= :cutoffDate " +
					"and actItem.streamObject in ( select sib from StreamItemBase as sib where sib.owningSubscription = :owningSub) order by actItem.dateCreated desc",
			  ['cutoffDate':cutoffDate, 'owningSub':subscription], ['max': maxCount ]);
				  
		  
		if( queryResults )
		{
			log.debug( "adding ${queryResults.size()} events read from DB");
			recentEvents.addAll( queryResults );
		}
		else {
			log.debug( "NO results found!" );
		}
	
		return recentEvents;
			
	}
	
}
