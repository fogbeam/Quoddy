package org.fogbeam.quoddy.integration.activitystream

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class Actor 
{

    static constraints = 
	{
    	
	}

	@XmlElement
	String displayName;
	@XmlElement
	String objectType;
	@XmlElement
	String id;
	@XmlElement
	String url;
	@XmlElement
	Image image;
	
	public String toString()
	{
		return "Actor: displayName: ${displayName}, objectType: ${objectType}, id: ${id}, url: ${url}, image: ${image}";	
	}		
}
