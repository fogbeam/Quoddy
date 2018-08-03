package org.fogbeam.quoddy.stream

class ResharedActivityStreamItem extends ActivityStreamItem
{
	ActivityStreamItem originalItem;
	
	public String getTemplateName()
	{
		return "/renderResharedActivity";
	}
}
