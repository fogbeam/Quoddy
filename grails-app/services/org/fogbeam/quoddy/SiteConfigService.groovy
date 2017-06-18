package org.fogbeam.quoddy

import org.fogbeam.quoddy.system.settings.SiteConfigEntry


class SiteConfigService {

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
}
