package org.fogbeam.quoddy

import org.fogbeam.quoddy.profile.ContactAddress

public class ContactAddressService 
{	
	public ContactAddress save( final ContactAddress addressToSave )
	{
		if( !addressToSave.save(flush:true))
		{
			addressToSave.errors.allErrors.each { log.error( it.toString() ) }
		}
		
		return addressToSave;		
	}
	
	public ContactAddress findById( final Long id )
	{
		ContactAddress address = ContactAddress.findById( id );
		
		return address;
	}
}
