package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.ActivityStreamItem

import grails.plugin.springsecurity.annotation.Secured

class PermalinkController
{
	def eventStreamService;
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{
		String uuid = params.uuid;
		ActivityStreamItem item = eventStreamService.getActivityStreamItemByUuid( uuid );
		
		[item:item];
	}
}