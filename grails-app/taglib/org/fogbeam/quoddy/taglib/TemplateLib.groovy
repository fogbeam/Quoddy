package org.fogbeam.quoddy.taglib

import grails.gsp.PageRenderer
import groovy.text.Template

import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine

class TemplateLib 
{
	static namespace = "fogcutter";
	static defaultEncodeAs = "raw";

	PageRenderer groovyPageRenderer;
	
	
	def externalTemplate = { attrs, body ->
		
		
		String quoddyHome = System.getProperty("quoddy.home");
		if( !quoddyHome.endsWith("/"))
		{
			quoddyHome = quoddyHome + "/";
		}
		
		String externalTemplateName = attrs.externalTemplate

		File externalTemplateFile = new File( quoddyHome + externalTemplateName ); 
		
		GroovyPagesTemplateEngine groovyPagesTemplateEngine = new GroovyPagesTemplateEngine();
		
		StringWriter sw = new StringWriter();
		
		Template compiledContent = groovyPagesTemplateEngine.createTemplate(externalTemplateFile.text, UUID.randomUUID().toString());
		
		compiledContent?.make()?.writeTo(sw)
		String renderedContent = sw.toString()
		
		out << renderedContent;
	}
}
	