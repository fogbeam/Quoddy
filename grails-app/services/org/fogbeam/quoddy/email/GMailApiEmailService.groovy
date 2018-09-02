package org.fogbeam.quoddy.email

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.fogbeam.quoddy.profile.ContactAddress
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import grails.core.GrailsApplication

// NOTE: make sure we inject the grailsApplication since we define this bean manually...
class GMailApiEmailService implements EmailService, InitializingBean 
{
	@Autowired
	GrailsApplication grailsApplication;
	
	/** Application name. */
	private static final String APPLICATION_NAME = "Gmail API Java Quickstart";

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 * 
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/gmail-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList( GmailScopes.GMAIL_COMPOSE );


	public	void afterPropertiesSet()
	{
		log.info( "GMailApiEmailService.afterPropertiesSet()");
		
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
		
	
	@Override
	public void deliverEmail( final ContactAddress to, final ContactAddress from, final String subject, final String body)
	{
		
		println "in deliverEmail() call";

		// Build a new authorized API client service.
		Gmail service = getGmailService();
		println "got Gmail service"
		
		UUID uuid = UUID.randomUUID();
		println "got UUID: " + uuid.toString()		

		MimeMessage msg = createEmail( to.getAddress(), from.getAddress(), subject + ": "  + uuid.toString(), body );
		
		println "created MimeMessage instance"
		Message message = createMessageWithEmail( msg );
		println "created Message instance"
		service.users().messages().send("me", message).execute();
		println "sent message"
		
	}
	
	/**
	 * Creates an authorized Credential object.
	 *
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public Credential authorize() throws Exception 
	{
		String privateKeyPath = grailsApplication.config.gmailapi.serviceaccount.privateKey; 
		log.info( "privateKeyPath: ${privateKeyPath}" );
		
		File p12File = new File( privateKeyPath );
		
		if( !p12File.exists())
		{
			log.error( "File at ${privateKeyPath} does not exist!" );
		}
		
		GoogleCredential credential = new GoogleCredential.Builder()
		.setTransport(HTTP_TRANSPORT)
		.setJsonFactory(JSON_FACTORY)
		.setServiceAccountId( grailsApplication.config.gmailapi.serviceaccount.accountId ) /*  */
		.setServiceAccountPrivateKeyFromP12File(p12File)
		.setServiceAccountScopes(SCOPES)
		.setServiceAccountUser( grailsApplication.config.gmailapi.serviceaccount.user )
		.build();

		return credential;
	}

	
	/**
	 * Build and return an authorized Gmail client service.
	 *
	 * @return an authorized Gmail client service
	 * @throws IOException
	 */
	public Gmail getGmailService() throws IOException 
	{
		Credential credential = authorize();
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	public MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException 
	{
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO,
				new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	public Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException 
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);
		byte[] bytes = buffer.toByteArray();
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
		Message message = new Message();
		message.setRaw(encodedEmail);

		return message;
	}
}
