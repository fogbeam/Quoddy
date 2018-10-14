package org.fogbeam.oauth

import static javax.servlet.http.HttpServletResponseWrapper.SC_UNAUTHORIZED;

import java.security.GeneralSecurityException
import java.security.cert.X509Certificate

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

import org.apache.http.HttpEntity
import org.apache.http.HttpHeaders
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.ssl.SSLContextBuilder
import org.apache.http.ssl.TrustStrategy
import org.apache.http.util.EntityUtils
import org.springframework.security.core.Authentication

import org.fogbeam.common.oauth.client.Insecure

import groovy.json.JsonSlurper

public class OAuthInterceptor 
{
	int order = Integer.MAX_VALUE;
	
	def userService;
	
	public OAuthInterceptor()
	{
		match( uri: "/api/**" )
	}
	
    boolean before() 
	{ 
		// if fogbeam.devmode is enabled, then allow access to the API's based on 
		// a normal user session login
		String devMode = grailsApplication.config.fogbeam.devmode;
		if( ( devMode != null ) && Boolean.parseBoolean( devMode ))
		{	
			log.warn( "fogbeam.devmode IS enabled!!" );
			
			Authentication authentication = session.authentication;
			if( authentication != null )
			{
				return true;
			}
		}
		
		
		
		log.info( "OAuthInterceptor fired!");
		
		
		HttpClient httpClient = null;
		
		String insecureHttpClientFlag = grailsApplication.config.rest.oauth.httpclient.insecure;
		if( Boolean.parseBoolean( insecureHttpClientFlag ) )
		{
			httpClient = Insecure.getInsecureHttpClient();
		}
		else
		{
			httpClient = HttpClients.createDefault();
		}
		
		String clientId =  grailsApplication.config.rest.oauth.incoming.client.clientId; // "test_client";
		String clientSecret = grailsApplication.config.rest.oauth.incoming.client.clientSecret; // "clientSecret";
		
		
		String authorizationHeader = "Basic " + new String( Base64.getEncoder().encode( ( clientId + ":"+clientSecret).getBytes() ) );

		// introspect our token to see if it's good
		String oauthToken = request.getHeader('Authorization');
	
		System.out.println( "got (from Authorization header) oauthToken: " + oauthToken );
	
		/* A hack to let us get the token as a query string parameter. Useful in development
		 * mode. Delete this or hide it behind a feature flag before pushing for real 
		 */
		if( oauthToken == null || oauthToken.isEmpty() )
		{
			oauthToken = params.oauthToken;
			if(oauthToken != null )
			{
				oauthToken = "Bearer " + oauthToken;
			}
		}
		
				
		if( oauthToken != null && !oauthToken.isEmpty())
		{
			if( oauthToken.startsWith( "Bearer " ))
			{
				oauthToken = oauthToken.replace( "Bearer ", "" );
			}
			else
			{
				response.status = SC_UNAUTHORIZED
				return false
			}
		}
		else
		{
			response.status = SC_UNAUTHORIZED
			return false
		}
		
		// get oauth server host from config
		String oauthServerIntrospectUrl = grailsApplication.config.rest.oauth.incoming.server.introspect.url;
		log.info( "got oauthServerIntrospectUrl: ${oauthServerIntrospectUrl}");
		
		HttpPost httpPost = new HttpPost( oauthServerIntrospectUrl );
		httpPost.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
		
		List params = new ArrayList<NameValuePair>();
		params.add( new BasicNameValuePair( "token", oauthToken ));
				
		params.add( new BasicNameValuePair( "client_id", clientId ));
		
		httpPost.setEntity( new UrlEncodedFormEntity(params));
		
		HttpResponse introspectResponse = httpClient.execute( httpPost );
		
		boolean tokenValid = false; // now test this with token introspection
	
		try
		{
			System.out.println(introspectResponse.getStatusLine());
			HttpEntity entity1 = introspectResponse.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			String strResponse = EntityUtils.toString(entity1);
		
			System.out.println( "Response: " + strResponse );
		
			if( strResponse )
			{
				def jsonSlurper = new JsonSlurper()
				def object = jsonSlurper.parseText(strResponse);
				tokenValid = object.active;
			}
		}
		finally
		{}

		
        if(!tokenValid) 
		{ 
			System.out.println( "returning false" );
            response.status = SC_UNAUTHORIZED
            return false
        }
        else
        {
			System.out.println( "returning true" );
			return true
        }
	}

    boolean after() 
	{ 
		true 
	}

    void afterView() 
	{
        // no-op
    }
	

	static HttpClient getInsecureHttpClient() throws GeneralSecurityException {
		TrustStrategy trustStrategy = new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) {
				return true;
			}
		};

		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		return HttpClients.custom()
				.setSSLSocketFactory(new SSLConnectionSocketFactory(
						new SSLContextBuilder().loadTrustMaterial(trustStrategy).build(),
						hostnameVerifier))
				.build();
	}

	
}
