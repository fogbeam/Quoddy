package org.fogbeam.quoddy.stream;

public class RssFeedItem extends StreamItemBase
{

	static constraints =
	{
		title( nullable:false)
		description( nullable:true)
		leadingSnippet(nullable:false)
		datePublished(nullable:true)
	}
	
	String title;
	String description;
	String leadingSnippet;
	String datePublished;
	String linkUrl;
	
	public String getTemplateName()
	{
		return "/renderRssFeedItem";
	}
	
}
