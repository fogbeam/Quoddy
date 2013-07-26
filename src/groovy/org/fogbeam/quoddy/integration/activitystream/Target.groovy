package org.fogbeam.quoddy.integration.activitystream

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class Target
{
	@XmlElement
	String url;
	@XmlElement
	String objectType;
	@XmlElement
	String id;
	@XmlElement
	String displayName;
	
	public String toString()
	{
		return "Target: url: ${url}, objectType: ${objectType}, id: ${id}, displayName: ${displayName}";	
	}
}
