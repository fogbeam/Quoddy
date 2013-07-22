package org.fogbeam.quoddy.integration.activitystream

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class Object
{
	@XmlElement
	String url;
	@XmlElement
	String id;
	@XmlElement
	String objectType;
	
	public String toString()
	{
		return "Object: url: ${url}, id: ${id}";	
	}
}
