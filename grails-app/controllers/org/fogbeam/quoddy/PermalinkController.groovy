package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem

import grails.plugin.springsecurity.annotation.Secured

class PermalinkController
{
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{
		String uuid = params.uuid;
		ActivityStreamItem item = ActivityStreamItem.findByUuid( uuid );
		
		[item:item];
	}
}
