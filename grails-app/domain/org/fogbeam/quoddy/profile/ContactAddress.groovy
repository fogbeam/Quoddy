package org.fogbeam.quoddy.profile

import java.io.Serializable;

class ContactAddress implements Serializable
{
	
	public static Map typeMapping = [1:"Jabber", 2:"Yahoo", 3:"MSN", 4:"AOL", 5:"Email", 6:"Twitter", 7:"Phone" ];
	
	public static final int JABBER_IM = 1;
	public static final int YAHOO_IM = 2;
	public static final int MSN_IM = 3;
	public static final int AOL_IM = 4;
	public static final int EMAIL = 5;
	public static final int TWITTER = 6;
	public static final int PHONE = 7;
	
	
	int serviceType;
	String address;
	Date dateCreated;
	Profile profile;
	boolean primaryInType;
	
	static belongsTo = [Profile];
}
