package org.fogbeam.quoddy

import javax.mail.Message
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

import org.fogbeam.quoddy.profile.ContactAddress

class EmailService
{
	public void deliverEmail( ContactAddress to, ContactAddress from, String subject, String body )
	{
		log.debug( "in sendEmail()" );
		try
		{
			// TODO: make this configurable
			String host = "smtp.gmail.com";
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable",true);
			props.put("mail.smtp.ssl.trust", host);
			props.put("mail.smtp.auth", true);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");
	 
			javax.mail.Session session = javax.mail.Session.getDefaultInstance( props, null );
			MimeMessage message = new MimeMessage( session );
			
			message.setFrom(new InternetAddress( from.address ) );
			InternetAddress toAddress = new InternetAddress( to.address );
			message.addRecipient( Message.RecipientType.TO, toAddress );
			message.setSubject(  subject );
			message.setText( body );
	 
			Transport transport = session.getTransport("smtp");
	 
			// TODO: fix this
			transport.connect(host, "motley.crue.fan@gmail.com", "7800seriesIC");
	 
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		}
		catch( Exception e )
		{
			log.error( "Error sending email!", e );
				
		}
		
		log.info( "email sent!" );
	}
	
}
