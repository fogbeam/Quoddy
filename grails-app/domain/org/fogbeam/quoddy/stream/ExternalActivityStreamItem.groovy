package org.fogbeam.quoddy.stream

/* Represents a "external" activity stream item... that is, something
 * that happened on, and completely within the boundaries of, some
 * external system that doesn't know anything about Quoddy.  None of the
 * entities (Actor, Object, Target, etc.) or their uuids, will be known
 * to us.
 * 
 * The model here would be, an external system pushes one of these
 * messages to us, and it is delivered to any users who have setup
 * a matching subscription (in other words, very much the same mechanism
 * as Business Event Subscriptions)
 */
class ExternalActivityStreamItem extends StreamItemBase
{
	/* because we model this as hanging off of an instance of
	 * ActivityStreamItem, this basically needs no fields, at
	 * least not the ones from the activitytrea.ms protocol.  This
	 * winds up being more of a marker, and a place to hold
	 * a template name that is relevant for rendering this kind
	 * of event.
	 *
	 */
	
	public String getTemplateName()
	{
		return "/renderExternalActivityStreamItem";
	}
}
