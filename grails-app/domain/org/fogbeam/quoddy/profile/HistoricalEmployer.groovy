package org.fogbeam.quoddy.profile

import java.io.Serializable;

class HistoricalEmployer implements Serializable
{
	static constraints = {
		monthFrom(nullable:true)
		monthTo(nullable:true)
		yearFrom(nullable:true)
		yearTo(nullable:true)
	}
	
	String companyName;
	String monthFrom;
	String yearFrom;
	String monthTo;
	String yearTo;
	String title;
	String description;
	
	static belongsTo = [Profile];

	
	public String toString()
	{
		return "companyName: $companyName, monthFrom: $monthFrom, yearFrom: $yearFrom\n" +
				"monthTo: $monthTo, yearTo: $yearTo, title: $title";	
	}	
}
