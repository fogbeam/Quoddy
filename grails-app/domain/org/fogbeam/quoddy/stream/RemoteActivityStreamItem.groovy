package org.fogbeam.quoddy.stream

/* Represents a "remote" activity stream item... that is, something
 * that didn't happen inside Quoddy, but is part of the Fogcutter
 * suite, or is otherwise directly integrated with us, and uses Quoddy
 * entities for Actor, Object, Target, etc.
 */
class RemoteActivityStreamItem extends StreamItemBase
{
	
	/* because we model this as hanging off of an instance of
	 * ActivityStreamItem, this basically needs no fields, at
	 * least not the ones from the activitytrea.ms protocol.  This
	 * winds up being more of a marker, and a place to hold
	 * a template name that is relevant for rendering this kind
	 * of event.
	 * 
	 */
	
	String remoteObjectType;
	
	public String getTemplateName()
	{
		switch( remoteObjectType )
		{
			case "NeddickLink":
				return "/renderNeddickLink";
			default:
				return "/renderRemoteActivityStreamItem";
		}
	}
	
}
