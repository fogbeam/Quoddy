package org.fogbeam.quoddy

import java.text.SimpleDateFormat

class Comment implements Comparable {

	// TODO: inject these formatters, or move all of this
	// formatting stuff into a wrapper class
	private SimpleDateFormat fullDateFormatter = new SimpleDateFormat( "EEE, d MMM yyyy HH:mm:ss" );
	private SimpleDateFormat timeOnlyFormatter = new SimpleDateFormat( "h:mm a" );
	
	static transients = ['formattedCreateDate', 'formattedCreateTime'];
	
	public Comment() {
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
	String uuid;
	String text;
	Date dateCreated;
	User creator;
	
	static belongsTo = [event:EventBase];

	@Override
	public int compareTo(Object o) {
		return -1 * ( dateCreated.compareTo( o.dateCreated ) );
	}
	
	public String getFormattedCreateTime()
	{
		return timeOnlyFormatter.format( dateCreated );
	}
	
	public String getFormattedCreateDate()
	{
		return fullDateFormatter.format( dateCreated );
	}
	
}
