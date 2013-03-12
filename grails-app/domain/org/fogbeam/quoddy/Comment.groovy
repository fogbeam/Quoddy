package org.fogbeam.quoddy

class Comment implements Comparable {

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
}
