package org.fogbeam.quoddy.email;

import org.fogbeam.quoddy.profile.ContactAddress;

public interface EmailService 
{
	public void deliverEmail( final ContactAddress to, final ContactAddress from, final String subject, final String body );
}
