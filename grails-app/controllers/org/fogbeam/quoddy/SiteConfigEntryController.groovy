package org.fogbeam.quoddy

import org.fogbeam.quoddy.system.settings.SiteConfigEntry

import grails.plugin.springsecurity.annotation.Secured

class SiteConfigEntryController 
{
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{
		redirect(controller:"siteConfigEntry", action:"list");
	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def list()
	{
		List<SiteConfigEntry> allEntries = SiteConfigEntry.findAll();
		[allEntries:allEntries];	
	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def edit()
	{
		SiteConfigEntry theEntry = SiteConfigEntry.findById( params.id );
		
		[ theEntry: theEntry ];	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def create() 
	{
		
		[];	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def save()
	{
		SiteConfigEntry theEntry = new SiteConfigEntry();
		theEntry.name = params.entryName;
		theEntry.value = params.entryValue;
		
		if( !theEntry.save(flush:true))
		{
			flash.message = "Failed to save entry!";
			log.error( "Failed to save entry" );
			// theEntry.errors.allErrors.each { p rintln it };
		}
		
		redirect( controller:"siteConfigEntry", action:"list" );
	}
			
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def update()
	{	
		SiteConfigEntry theEntry = SiteConfigEntry.findById( params.entryId );
		theEntry.name = params.entryName;
		theEntry.value = params.entryValue;
		
		if( !theEntry.save(flush:true))
		{
			flash.message = "Failed to save entry!";
			log.error( "Failed to save entry" );	
			// theEntry.errors.allErrors.each { p rintln it };
		}

		redirect( controller:"siteConfigEntry", action:"list" );	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def delete()
	{
		SiteConfigEntry theEntry = SiteConfigEntry.findById( params.entryId );
		theEntry.delete();
		
		redirect( controller:"siteConfigEntry", action:"list" );
	}
}