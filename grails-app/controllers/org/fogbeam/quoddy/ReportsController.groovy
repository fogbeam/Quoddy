package org.fogbeam.quoddy

import grails.plugin.springsecurity.annotation.Secured

class ReportsController 
{
    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
	def index()
	{
		[]	
	}
}
