package org.fogbeam.quoddy

import org.fogbeam.quoddy.profile.Interest

public class InterestService 
{
	public Interest findByName( final String name )
	{
		Interest interest = Interest.findByName( name );
		
		return interest;
	}
	
	public Interest save( final Interest interestToSave )
	{
		if( !interestToSave.save(flush:true) )
		{
			interestToSave.errors.allErrors.each { log.error( it.toString() ) }
		}
	}
}
