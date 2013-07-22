package org.fogbeam.quoddy.integration.activitystream

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class Image
{
	@XmlElement
	String url;
	@XmlElement
	String width;
	@XmlElement
	String height;
	
	public String toString()
	{
		return "Image: url: ${url}, width:${width}, height: ${height}";	
	}
}
