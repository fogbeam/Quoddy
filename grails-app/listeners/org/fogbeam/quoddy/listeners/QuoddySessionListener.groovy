package org.fogbeam.quoddy.listeners

import javax.servlet.http.HttpSession 
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH;

class QuoddySessionListener implements HttpSessionListener 
{
	public void sessionCreated( HttpSessionEvent event )
	{
		println "sessionCreated, putting config options in session";
		
		HttpSession newSession = event.getSession();
		def enableSelfReg = CH.config.enable.self.registration;
		
		if( enableSelfReg )
		{
			newSession.setAttribute( "enable_self_registration", true );
		}
		else
		{
			newSession.setAttribute( "enable_self_registration", false );
		}
	}

	public void sessionDestroyed(HttpSessionEvent arg0) 
	{
		// 
	}

}
