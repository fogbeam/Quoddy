package org.fogbeam.quoddy.taglib

import grails.gsp.PageRenderer
import groovy.text.Template

import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine

class TemplateTagLib 
{
	static namespace = "fogcutter";
	static defaultEncodeAs = "raw";

	// PageRenderer groovyPageRenderer;
	def groovyPagesTemplateEngine;
	
	
	def externalTemplate = { attrs, body ->
		
		
		String quoddyHome = System.getProperty("quoddy.home");
		if( !quoddyHome.endsWith("/"))
		{
			quoddyHome = quoddyHome + "/";
		}
		
		String externalTemplateName = attrs.externalTemplate

		File externalTemplateFile = new File( quoddyHome + "templates/" + externalTemplateName ); 
				
		StringWriter sw = new StringWriter();
		
		String content = externalTemplateFile.text;
		// System.out.println( "***********************************************\n${content}\n**************************************************");
		
		Template compiledContent = groovyPagesTemplateEngine.createTemplate(content, UUID.randomUUID().toString() );
		
		compiledContent?.make()?.writeTo(sw)
		
		String renderedContent = sw.toString()
		
		
		out << renderedContent;
	}
}
	