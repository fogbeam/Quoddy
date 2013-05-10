package org.fogbeam.quoddy.stream;

public class RssFeedItem extends StreamItemBase
{
	public RssFeedItem()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	String uuid;
}
