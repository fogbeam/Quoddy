package org.fogbeam.quoddy

import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class LocalLoginController
{
	
	def index()
	{
		[:];
	}
	
	def login()
	{	
		// take the supplied credentials...
		String username = params.username;
		String password = params.password;
		
				
		/* make a REST call to the CAS server to get a ticket */
		String url = "https://sso.fogbeam.com/cas/v1/tickets";
		log.info( "CAS server url: ${url}");
		
		String USER_AGENT = "CasRestClientMain";
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
			
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader( "Content-Type", "application/x-www-form-urlencoded" );
		
		username = URLEncoder.encode(username, "UTF-8");
		StringEntity requestString = new StringEntity( "username=${username}&password=${password}" );
		
		post.setEntity( requestString );

		HttpResponse response = httpClient.execute(post);
		
		log.info( "Response Code : " + response.getStatusLine().getStatusCode() );
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		
		
		Header[] headers = response.getAllHeaders();
		
		String ticketLocation = "";
		for( Header header : headers )
		{
			// System.out.println( "header: " + header );
			if( header.getName().equalsIgnoreCase( "Location" ))
			{
				ticketLocation = header.getValue();
			}
		}
	
		log.info( "ticketLocation: ${ticketLocation}" );
		
		if( ticketLocation == null || ticketLocation.isEmpty() )
		{
			redirect(uri:"/auth/login" );
			return;
		}
			
		/* now make another request to get our service ticket */
		post = new HttpPost(ticketLocation);
		
		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader( "Content-Type", "application/x-www-form-urlencoded" );
		
		
		requestString = new StringEntity( "service=http://localhost:8080/login/cas" );
		
		post.setEntity( requestString );
		
		response = httpClient.execute(post);
		log.info("Response Code : " + response.getStatusLine().getStatusCode());
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		
		headers = response.getAllHeaders();
		
		for( Header header : headers )
		{
			System.out.println( "header: " + header );
		}
		
		String responseStr = EntityUtils.toString( response.getEntity() );
			
		
		log.info( "responseStr: " + responseStr );
		System.out.println( "responseStr: " + responseStr );
		
		def returnUrl = params.returnUrl;
		// SavedRequest savedRequest = new MySavedRequest(request, returnUrl);
		// session.setAttribute( WebUtils.SAVED_REQUEST_KEY, savedRequest );
			
		// redirect the user to the CAS endpoint with the ticket attached to the request
		redirect(uri:"/login/cas?ticket=" + responseStr );
	}
	
	def oneTimeLogin()
	{
		// TODO: receive a special one-time login token that allows you to change your password but nothing else
		
		
		[:];
		
	}
	
	def logout()
	{	
		session.user = null;	
		log.info( "Redirecting to /logoff" );
		redirect(uri:"/logoff");
	}
}