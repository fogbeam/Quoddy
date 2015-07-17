package org.fogbeam.quoddy.stream

import java.io.Serializable;

import org.fogbeam.quoddy.User;

class StatusUpdate extends StreamItemBase implements Serializable
{

	String text;
	User creator;
	Date dateCreated;
	String enhancementJSON;
	
	static constraints =
	{
		enhancementJSON(empty:true);
	}
	
	static mapping =
	{
		enhancementJSON type: 'text'
	}
	
	static belongsTo = [User];
	static transients = ['templateName'];
	
	public String getTemplateName()
	{
		// println "returning /renderActivity from StatusUpdate";
		return "/renderActivity";
	}
	
}
