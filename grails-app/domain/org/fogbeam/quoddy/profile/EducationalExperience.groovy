package org.fogbeam.quoddy.profile

class EducationalExperience {

	static constraints = {
		institutionName(nullable:true)
		monthFrom(nullable:true)
		monthTo(nullable:true)
		yearFrom(nullable:true)
		yearTo(nullable:true)
		courseOfStudy(nullable:true)
		description(nullable:true)
	}
	
	String institutionName;
	String monthFrom;
	String yearFrom;
	String monthTo;
	String yearTo;
	String courseOfStudy;
	String description;
	
	static belongsTo = [Profile];
}
