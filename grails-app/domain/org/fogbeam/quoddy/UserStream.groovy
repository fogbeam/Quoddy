package org.fogbeam.quoddy

/* a UserStream object defines one specific named "stream" of entries that will
 * appear in the user's feed.  The UserStream controls which users, which groups, etc.
 * are included, as well as an filters or other preferences that will affect which items
 * are included in the stream.
 */
class UserStream
{
	public static final String DEFINED_USER = "DEFINED_BY_USER";
	public static final String DEFINED_SYSTEM = "DEFINED_BY_SYSTEM";
	
	static constraints = {}
	
	String 	name;
	String 	definedBy;
	User 	owner;
	Date 	dateCreated;
}
