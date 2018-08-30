package org.fogbeam.quoddy

import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

class LocalLoginController
{
    def authenticationManager;
    def authenticationDetailsSource;
    
	def index()
	{
		[:];
	}
	
	def login()
	{	
		// take the supplied credentials...
		String username = params.username;
		String password = params.password;
        
        
        boolean useLocal = false;
        if( params.useLocal )
        {
            useLocal = Boolean.parseBoolean( params.useLocal )
        }
        
        if( !useLocal )
        {
			log.warn( "Doing CAS Login!" );
            
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
        else
        {
            
            log.warn( "Doing LOCAL login!" );
            
            log.info( "username: \"${username}\", password: \"${password}\"");
            
            
             // no CAS, do purely local login by calling the authenticationManager
             // directly
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken( username, password );
            authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
            
            
            Authentication authentication = null;
            String urlAfterAuthentication = "/";
            try
            {
				log.info( "Trying authentication" );
                authentication = authenticationManager.authenticate(authRequest);
            
                log.info( "got Authentication: ${authentication}");
            
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
            }
            catch( BadCredentialsException bce )
            {
                // would we want to send the user to another page here?
                // urlAfterAuthentication = "/"
                log.error( "Exception doing Login: ", bce );
                
                flash.message = "Login Failed";
            }
            catch( Exception e )
            {
				log.error( "Exception doing Login: ", bce );
			}
			
            redirect( uri: urlAfterAuthentication );
        }
	}
	
    
    
	def oneTimeLogin()
	{
		// TODO: receive a special one-time login token that allows you to change your password but nothing else
        
		[:];
		
	}
	
    def checkSession()
    {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        
        // log.info( "detected authentication: ${authentication}");
        
        if( authentication != null && authentication.isAuthenticated() && authentication.principal.isEnabled())
        {
            render( status:200, text: "OK" );
        }
        else
        {
            render( status:405, text: "No Session");
        }
    }
    
	def logout()
	{	
		session.user = null;	
		log.info( "Redirecting to /logoff" );
		redirect(uri:"/logoff");
	}
}