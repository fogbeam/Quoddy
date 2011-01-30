package org.fogbeam.quoddy.profile

class ContactAddress 
{
	public static final int JABBER_IM = 1;
	public static final int YAHOO_IM = 2;
	public static final int MSN_IM = 3;
	public static final int AOL_IM = 4;
	public static final int EMAIL = 5;
	public static final int TWITTER = 6;
	
	
	int serviceType;
	String address;
	Date dateCreated;
	
	static belongsTo = [Profile];
}
