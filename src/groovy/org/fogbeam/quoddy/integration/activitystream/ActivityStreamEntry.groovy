package org.fogbeam.quoddy.integration.activitystream

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class ActivityStreamEntry
{
	@XmlElement
	String content;
	@XmlElement
	String published;
	@XmlElement
	String title;
	@XmlElement
	String url;
	@XmlElement
	String verb;
	
	@XmlElement
	Actor actor;
	@XmlElement
	org.fogbeam.quoddy.integration.activitystream.Object object;
	@XmlElement
	Target target;	
	
	
	public String toString()
	{
		return "ActivityStreamEntry: published: ${published}, verb: ${verb}, actor: ${actor}, object: ${object}, target: ${target}";	
	}
}
