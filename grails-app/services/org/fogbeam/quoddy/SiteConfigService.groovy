package org.fogbeam.quoddy

import org.fogbeam.quoddy.system.settings.SiteConfigEntry


class SiteConfigService {

	public String getSiteConfigEntry( final String name )
	{
		println "siteConfigEntry request for ${name}";
		
		SiteConfigEntry entry = SiteConfigEntry.findByName( name );
		
		if( entry != null )
		{
			println "found a value for ${name}";	
		}
		else
		{
			println "found no value for ${name}";
		}
		
		return entry?.value;
	}
}
