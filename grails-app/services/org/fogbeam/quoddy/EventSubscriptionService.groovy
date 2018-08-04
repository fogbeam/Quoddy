package org.fogbeam.quoddy

import org.fogbeam.quoddy.subscription.BaseSubscription

class EventSubscriptionService
{

	public BaseSubscription findByUuid( final String uuid )
	{
		return BaseSubscription.findByUuid( uuid );
	}
		
	public List<BaseSubscription> getAllSubscriptionsForUser( final User user )
	{
		log.debug( "getAllSubscriptionsForUser() called for user ${user.toString()}");
		
		List<BaseSubscription> subscriptions = new ArrayList<BaseSubscription>();
		
		List<BaseSubscription> tempSubscriptions = BaseSubscription.executeQuery( "select subscription from BaseSubscription as subscription where subscription.owner = :owner",
			['owner':user] );
		
		if( tempSubscriptions != null )
		{
			subscriptions.addAll( tempSubscriptions );
		}

		return subscriptions;
	}
	
}
