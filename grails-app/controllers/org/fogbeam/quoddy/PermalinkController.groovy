package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem

class PermalinkController
{
	def index =
	{
		String uuid = params.uuid;
		ActivityStreamItem item = ActivityStreamItem.findByUuid( uuid );
		
		[item:item];
	}
}
