package org.fogbeam.quoddy.subscription;

import java.io.Serializable;

import org.fogbeam.quoddy.User

public class RssFeedSubscription extends BaseSubscription implements Serializable
{
	static constraints = {
		url( nullable:false);
	}
	
	String url;
}
