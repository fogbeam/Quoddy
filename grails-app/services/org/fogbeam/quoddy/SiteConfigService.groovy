package org.fogbeam.quoddy

import org.fogbeam.quoddy.system.settings.SiteConfigEntry


public class SiteConfigService 
{
	public String getSiteConfigEntry( final String name )
	{
		log.info( "siteConfigEntry request for ${name}");
		
		SiteConfigEntry entry = SiteConfigEntry.findByName( name );
		
		if( entry != null )
		{
			log.info( "found a value for ${name}");	
		}
		else
		{
			log.warn( "found no value for ${name}" );
		}
		
		return entry?.value;
	}
	
	public void deleteById( final Long id )
	{
		SiteConfigEntry theEntry = SiteConfigEntry.findById( id );
		theEntry.delete();
	}
	
	public void save( final SiteConfigEntry entryToSave )
	{
		if( !entryToSave.save(flush:true))
		{
			log.error( "Failed to save entry" );
			entryToSave.errors.allErrors.each { log.error( it.toString() ) };
		}
	}
	
	public SiteConfigEntry getById( final Long id)
	{
		SiteConfigEntry theEntry = SiteConfigEntry.findById( id );
		
		return theEntry;
	}
	
	public List<SiteConfigEntry> listAll()
	{
		List<SiteConfigEntry> allEntries = new ArrayList<SiteConfigEntry>();
		
		Collection queryResults = SiteConfigEntry.findAll();
		
		allEntries.add( queryResults );
		
		return allEntries;
	}
}
