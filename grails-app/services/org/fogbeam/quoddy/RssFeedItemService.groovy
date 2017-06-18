package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.RssFeedItem
import org.fogbeam.quoddy.subscription.RssFeedSubscription

class RssFeedItemService
{
	public RssFeedItem saveItem( final RssFeedItem rssFeedItem )
	{
		if( !rssFeedItem.save(flush:true) )
		{
			log.error( "Error saving RssFeedItem");
			rssFeedItem.errors.allErrors.each { log.debug(it) }
		}
		
		return rssFeedItem;
	}
	
	
	public RssFeedItem findRssFeedItemByUrlAndSubscription( final String url, final RssFeedSubscription subscription )
	{
		RssFeedItem item = null;
		
		List<RssFeedItem> items = RssFeedItem.executeQuery( "select rssItem from RssFeedItem as rssItem where rssItem.linkUrl = :url and rssItem.owningSubscription = :sub",
											[url:url, sub:subscription] );
		
		if( items != null && items.size == 1 )
		{
			item = items.get(0);
		}								
		else if( items != null && items.size > 1 ) 
		{
			log.error( "More than one entry found for this combination of linkUrl and owning sub!  ${url}, ${subscription.toString()}");
			item = items.get(0);
		}								
		
		return item;
		
	}	
}
