package org.fogbeam.quoddy

import grails.plugin.springsecurity.annotation.Secured

class TagController 
{
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def list()
	{
		[:]	
	}
}
