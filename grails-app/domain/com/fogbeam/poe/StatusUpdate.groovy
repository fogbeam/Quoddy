package com.fogbeam.poe

class StatusUpdate {

	String text;
	User creator;
	Date dateCreated;

	static belongsTo = [User];
}
