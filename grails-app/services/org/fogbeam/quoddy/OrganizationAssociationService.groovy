package org.fogbeam.quoddy

import org.fogbeam.quoddy.profile.OrganizationAssociation

public class OrganizationAssociationService 
{
	public OrganizationAssociation findByName( final String name )
	{
		OrganizationAssociation org = OrganizationAssociation.findByName( name );
		
		return org;
	}
	
	public OrganizationAssociation save( final OrganizationAssociation orgToSave )
	{
		if( !orgToSave.save(flush:true ) )
		{
			orgToSave.errors.allErrors.each { log.error( it.toString() ) }
		}

		return orgToSave;
	}
}
