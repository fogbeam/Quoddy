package org.fogbeam.quoddy

class EventSubscriptionService
{
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
	
}
