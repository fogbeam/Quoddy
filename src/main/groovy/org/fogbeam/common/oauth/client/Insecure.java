package org.fogbeam.common.oauth.client;

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class Insecure 
{
	
	public static HttpClient getInsecureHttpClient() throws GeneralSecurityException {
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
