package org.fogbeam.quoddy.email

import org.fogbeam.quoddy.profile.ContactAddress


class AmazonSesEmailService implements EmailService {

	@Override
	public void deliverEmail( final ContactAddress to, final ContactAddress from, final String subject, final String body) 
	{
		
	}

}
