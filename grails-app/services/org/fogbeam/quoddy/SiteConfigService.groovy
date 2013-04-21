package org.fogbeam.quoddy

import org.fogbeam.quoddy.system.settings.SiteConfigEntry


class SiteConfigService {

	public String getSiteConfigEntry( final String name )
	{
		SiteConfigEntry entry = SiteConfigEntry.findByName( name );
		return entry?.value;
	}
}
