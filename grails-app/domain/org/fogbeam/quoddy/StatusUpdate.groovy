package org.fogbeam.quoddy

class StatusUpdate {

	String text;
	User creator;
	Date dateCreated;

	static belongsTo = [User];
}
