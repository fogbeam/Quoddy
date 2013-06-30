package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.RssFeedItem
import org.fogbeam.quoddy.subscription.RssFeedSubscription

class RssFeedSubscriptionService
{
	public RssFeedSubscription findByUuid( final String uuid )
	{
		throw new RuntimeException( "not implemented yet...");
	}
	
	public RssFeedSubscription saveSubscription( final RssFeedSubscription subscription )
	{
		throw new RuntimeException( "not implemented yet...");
	}
	
	public List<RssFeedSubscription> getAllSubscriptionsForUser( final User user )
	{
		List<RssFeedSubscription> subscriptions = new ArrayList<RssFeedSubscription>();
		
		List<RssFeedSubscription> tempSubscriptions = RssFeedSubscription.executeQuery( "select subscription from RssFeedSubscription as subscription where subscription.owner = :owner",
			['owner':user] );
		
		if( tempSubscriptions )
		{
			subscriptions.addAll( tempSubscriptions );
		}

		return subscriptions;
	}

	public List<ActivityStreamItem> getRecentItemsForSubscription( final RssFeedSubscription subscription,  final int maxCount )
	{
	
		println "getRecentItemsForSubscription: ${subscription.id}";
		
		List<ActivityStreamItem> recentEvents = new ArrayList<ActivityStreamItem>();
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2160 );
		Date cutoffDate = cal.getTime();
	
		println "Using ${cutoffDate} as cutoffDate";

	
		List<ActivityStreamItem> queryResults =
			ActivityStreamItem.executeQuery(
					"select actItem from ActivityStreamItem as actItem where actItem.dateCreated >= :cutoffDate " +
					"and actItem.streamObject in ( select sib from StreamItemBase as sib where sib.owningSubscription = :owningSub) order by actItem.dateCreated desc",
			  ['cutoffDate':cutoffDate, 'owningSub':subscription], ['max': maxCount ]);
					  			
		if( queryResults )
		{
			println "adding ${queryResults.size()} events read from DB";
			recentEvents.addAll( queryResults );
		}
		else {
			println "NO results found!";
		}
	
		return recentEvents;
			
	}
	
}
