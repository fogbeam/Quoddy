package org.fogbeam.quoddy

import org.fogbeam.quoddy.profile.Skill

public class SkillService 
{
	public Skill findByName( final String name )
	{
		Skill skill = Skill.findByName( name );
		
		return skill;
	}
	
	public Skill save( final Skill skillToSave )
	{
		if( !skillToSave.save(flush:true) )
		{
			skillToSave.errors.allErrors.each { log.error( it.toString() ) }
		}
	}
}
