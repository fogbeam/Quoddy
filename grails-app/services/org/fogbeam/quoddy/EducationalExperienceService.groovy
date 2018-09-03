package org.fogbeam.quoddy

import org.fogbeam.quoddy.profile.EducationalExperience

class EducationalExperienceService 
{
	public EducationalExperience save( final EducationalExperience experienceToSave )
	{
		if( !experienceToSave.save(flush:true ))
		{
			experienceToSave.errors.allErrors.each { log.error( it.toString() ) }	
		}
		
		return experienceToSave;
	}
}
