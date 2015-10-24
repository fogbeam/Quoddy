package org.fogbeam.quoddy

import org.fogbeam.quoddy.profile.Profile 

class ProfileService 
{

	public void updateProfile( final Profile profile )
	{
		if( !profile.save() )
		{
			profile.errors.allErrors.each { log.debug( it ) };
			throw new RuntimeException( "couldn't update profile record for profile: ${profile.id}" );
			
		}	
	}	
}
