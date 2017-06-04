package org.fogbeam.quoddy.bpm

import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.subscription.ActivitiUserTaskSubscription

class ActivitiUserTaskSubscriptionService
{
	public ActivitiUserTaskSubscription findByUuid( final String uuid )
	{
		throw new RuntimeException( "not implemented yet...");
	}
	
	public ActivitiUserTaskSubscription saveSubscription( final ActivitiUserTaskSubscription subscription )
	{

		if( !subscription.save() )
		{
			log.error( "Error saving ActivitiUserTaskSubscription");
			subscription.errors.allErrors.each { log.warn(it); } 	
		}
		
		subscription.refresh();
		
		
		return subscription;
		
	}
	
	public List<ActivitiUserTaskSubscription> getAllSubscriptionsForUser( final User user )
	{
		List<ActivitiUserTaskSubscription> subscriptions = new ArrayList<ActivitiUserTaskSubscription>();
		
		List<ActivitiUserTaskSubscription> tempSubscriptions = ActivitiUserTaskSubscription.executeQuery( "select subscription from ActivitiUserTaskSubscription as subscription where subscription.owner = :owner",
			['owner':user] );
		
		if( tempSubscriptions )
		{
			subscriptions.addAll( tempSubscriptions );
		}

		return subscriptions;
	}

	
	public List<ActivityStreamItem> getRecentItemsForSubscription( final ActivitiUserTaskSubscription subscription,  final int maxCount )
	{
	
		log.debug( "ActivitiUserTaskSubscriptionService.getRecentItemsForSubscription: ${subscription.id}");
		
		List<ActivityStreamItem> recentEvents = new ArrayList<ActivityStreamItem>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
	
		log.debug( "Using ${cutoffDate} as cutoffDate");

	
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
			log.debug( "NO results found!");
		}
	
		return recentEvents;
			
	}
		
}
