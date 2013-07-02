package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.RssFeedItem

class RssFeedItemService
{
	public RssFeedItem saveItem( final RssFeedItem rssFeedItem )
	{
		if( !rssFeedItem.save() )
		{
			println "Error saving RssFeedItem";
			rssFeedItem.errors.allErrors.each { println it }
		}
		
		return rssFeedItem;
	}
}
