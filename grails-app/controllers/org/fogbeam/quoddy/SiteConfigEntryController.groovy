package org.fogbeam.quoddy

import org.fogbeam.quoddy.system.settings.SiteConfigEntry

import grails.plugin.springsecurity.annotation.Secured

class SiteConfigEntryController 
{
	def siteConfigService;
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{
		redirect(controller:"siteConfigEntry", action:"list");
	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def list()
	{
		List<SiteConfigEntry> allEntries = siteConfigService.listAll();
		
		log.info( "Found ${allEntries?.size()} siteConfigEntry records" );
		
		[allEntries:allEntries];	
	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def edit()
	{
		SiteConfigEntry theEntry = siteConfigService.getById( Long.parseLong( params.id ) );
		
		[ theEntry: theEntry ];	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def create() 
	{
		
		[:];	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def save()
	{
		SiteConfigEntry theEntry = new SiteConfigEntry();
		theEntry.name = params.entryName;
		theEntry.value = params.entryValue;

		siteConfigService.save( theEntry );		
		
		redirect( controller:"siteConfigEntry", action:"list" );
	}
			
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def update()
	{	
		SiteConfigEntry theEntry = siteConfigService.getById( Long.parseLong( params.entryId ) );
		theEntry.name = params.entryName;
		theEntry.value = params.entryValue;
		
		siteConfigService.save( theEntry );

		redirect( controller:"siteConfigEntry", action:"list" );	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def delete()
	{
		log.info( "SiteConfigEntryController.delete() called. ${params.entryId}");
		
		siteConfigService.deleteById( Long.parseLong( params.entryId ) );
				
		redirect( controller:"siteConfigEntry", action:"list" );
	}
}