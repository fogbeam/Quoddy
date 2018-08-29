package org.fogbeam.quoddy.spring.listeners;

import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext

public class HttpSessionServletListener implements HttpSessionListener, ApplicationContextAware
{
	Logger log = LoggerFactory.getLogger( HttpSessionServletListener.class );
  
	ApplicationContext applicationContext;
	
	public void setApplicationContext( final ApplicationContext applicationContext )
	{
		this.applicationContext = applicationContext;
	}
	
	
    // called by servlet container upon session creation
    void sessionCreated(HttpSessionEvent event) 
    {
		log.info( "sessionCreated: ${event.session}");
    }

    // called by servlet container upon session destruction
    void sessionDestroyed(HttpSessionEvent event) 
    {
		log.info( "sessionDestroyed: ${event.session}");
		event.getSession().attributeNames.each { log.info( "attributeName: ${it}"); }
		
		
		SecurityContext securityContext = (SecurityContext)event.getSession().getAttribute( "SPRING_SECURITY_CONTEXT" );
		
		if( securityContext == null )
		{
			return;
		}
		else
		{
			Authentication auth = securityContext.getAuthentication();
		
			if( auth != null )
			{
				log.info( "calling LogoutEventListener to unregister eventQueue for auth = ${auth}" );
				LogoutEventListener logoutListener = (LogoutEventListener)applicationContext.getBean( "logoutEventListener" ); 
		
				logoutListener.logout( null, null, auth );
			}
			else
			{
				log.warn( "auth is null, doing nothing...");
			}
		
		}
    }
}