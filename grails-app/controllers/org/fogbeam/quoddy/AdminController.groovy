package org.fogbeam.quoddy;

import grails.plugin.springsecurity.annotation.Secured

@Secured("ROLE_ADMIN")
public class AdminController
{
	
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
	def userService;
	def searchService;
	
	def index()
	{

	}

	
	def importUsers() 
	{
		log.debug( "importUsers" );
		
		[:];
	}
		
}
