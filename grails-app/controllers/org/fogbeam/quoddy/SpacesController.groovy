package org.fogbeam.quoddy

import grails.plugin.springsecurity.annotation.Secured

class SpacesController 
{
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def index()
	{
		
	}

}
