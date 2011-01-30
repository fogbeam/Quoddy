package org.fogbeam.quoddy.profile;

import org.fogbeam.quoddy.User;
import org.fogbeam.quoddy.images.Photograph 

class Profile {

	
	static constraints = {
		summary(nullable:true)
		photo(nullable:true)
		location(nullable:true)
		hometown(nullable:true)
		languages(nullable:true)
		interests(nullable:true)
		skills(nullable:true)
		organizations(nullable:true)
		employmentHistory(nullable:true)
		educationHistory(nullable:true)
		links(nullable:true)
		contactAddresses(nullable:true)
		favorites(nullable:true)
		projects(nullable:true)
		dateCreated()
	}
	
	
	// User owner;
	Date dateCreated;
	
	String summary;
	
	// Photo
	Photograph photo;
	
	// birthday
	int birthYear;
	int birthMonth;
	int birthDayOfMonth;
	
	// sex
	int sex;
	
	// location
	String location;
	
	// hometown
	String hometown
	
	// languages
	Set<Language> languages;
	
	// interests
	Set<Interest> interests;
	
	// skills
	Set<Skill> skills;
	
	// groups and associations
	Set<OrganizationAssociation> organizations;
	
	// employment history
	Set<HistoricalEmployer> employmentHistory;
	
	// education history
	Set<EducationalExperience> educationHistory;
	
	// links
	Set<String> links;
	
	// contact addresses (Twitter, XMPP, AIM, Yahoo, Phone, etc)
	Set<ContactAddress> contactAddresses;
	
	// favorites (books, TV shows, movies, etc)
	Set<Favorite> favorites;
	
	// publications
	// TODO: publications.
	
	// projects
	Set<Project> projects;
	
	static hasMany = [languages:Language, interests:Interest, skills:Skill, \
					organizations:OrganizationAssociation, employmentHistory:HistoricalEmployer, \
					educationHistory:EducationalExperience, links:String, contactAddresses:ContactAddress, favorites: Favorite ];
	
	static belongsTo = [owner:User];	

	public String getUserUuid()
	{
		return owner?.uuid;	
	}
	
	public void setUserUuid( String userUuid ) {}					
}
