package org.fogbeam.quoddy

import org.fogbeam.quoddy.profile.HistoricalEmployer

class HistoricalEmployerService 
{
	public HistoricalEmployer save( final HistoricalEmployer employerToSave )
	{
		if( !employerToSave.save(flush:true))
		{
			employerToSave.errors.allErrors.each { log.error( it.toString() ) }
		}
		
		return employerToSave;
	}
}
