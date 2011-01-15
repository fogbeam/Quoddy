package org.fogbeam.quoddy.profile

class HistoricalEmployer 
{
	String companyName;
	String monthFrom;
	String yearFrom;
	String monthTo;
	String yearTo;
	String title;
	String description;
	
	static belongsTo = [Profile];
	
}
