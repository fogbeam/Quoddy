package org.fogbeam.quoddy.listeners

import javax.servlet.ServletContextAttributeEvent
import javax.servlet.ServletContextAttributeListener
import javax.servlet.http.HttpSessionAttributeListener
import javax.servlet.http.HttpSessionBindingEvent
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.fogbeam.quoddy.User
import org.springframework.context.ApplicationContext

class UITestSessionListener implements
		HttpSessionAttributeListener, HttpSessionListener, ServletContextAttributeListener 
{

	ApplicationContext ctx;
	def eventQueueService;
	
	// HttpSessionListener
	@Override
	public void sessionCreated(HttpSessionEvent event) 
	{
		println "sessionCreated";
	}

	// HttpSessionListener
	@Override
	public void sessionDestroyed(HttpSessionEvent event) 
	{
		println "sessionDestroyed";
	}

	// HttpSessionAttributeListener
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) 
	{
		println "attributeAdded";
		
		// println "Name: ${event.getName()}";
		// println "Value: ${event.getValue()}";
		// println "Source: ${event.getSource()}";
		
		println "Note: When we add our logged in user...";
		String attName = event.getName();
		if( attName.equalsIgnoreCase( "user" ))
		{
			User user = event.getValue();
			def userId = user.userId;
			println "user ${userId} added to session.";	
		
			// TODO: Register an event queue for user
			println "Register an event queue for user ${userId}";
			eventQueueService.registerEventQueueForUser( userId );
		}
		
	}

	// HttpSessionAttributeListener
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) 
	{
		println "attributeRemoved";
	}

	// HttpSessionAttributeListener
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) 
	{
		println "attributeReplaced";
	}

	void attributeAdded(ServletContextAttributeEvent event) 
	{
		println "attribute ${event.name} added to ServletContext!";
		if( event.getName().equalsIgnoreCase( GrailsApplicationAttributes.APPLICATION_CONTEXT ))
		{
			println "BING!";
			ctx = event.getValue();
			eventQueueService = ctx.getBean( "eventQueueService" );
		}
	}	

	void attributeRemoved(ServletContextAttributeEvent event)
	{

	}
	
	void attributeReplaced(ServletContextAttributeEvent event) 
	{

	}
		
}