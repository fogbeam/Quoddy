package org.fogbeam.quoddy.integration.activitystream

class ActivityStreamEntry
{
	String content;
	String published;
	String title;
	String url;
	String verb;
	
	Actor actor;
	org.fogbeam.quoddy.integration.activitystream.Object object;
	Target target;	
	
	
	public String toString()
	{
		return "ActivityStreamEntry: published: ${published}, verb: ${verb}, actor: ${actor}, object: ${object}, target: ${target}";	
	}
}
