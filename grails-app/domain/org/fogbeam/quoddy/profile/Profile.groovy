package org.fogbeam.quoddy.profile;

import java.io.Serializable;

import org.fogbeam.quoddy.User;
import org.fogbeam.quoddy.images.Photograph 

class Profile implements Serializable 
{

	
	static constraints = {
		summary(nullable:true)
		title( nullable:true)
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
		dotPlan(nullable:true)
		dateCreated()
	}
	
	
	// User owner;
	Date dateCreated;
	
	// other
	String summary;
	String title;
	String dotPlan;
	
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
	
    String userUuid;
    
	// publications
	// TODO: publications.
	
	// projects
	Set<Project> projects;
	
	static hasMany = [languages:Language, interests:Interest, skills:Skill, \
					organizations:OrganizationAssociation, employmentHistory:HistoricalEmployer, \
					educationHistory:EducationalExperience, links:String, contactAddresses:ContactAddress, favorites: Favorite ];
	
	static belongsTo = [owner:User];	

    static transients = ['userUuid']
    
	public String getUserUuid()
	{
		return owner?.uuid;	
	}
	
	public void setUserUuid( String userUuid ) {}
	
	public ContactAddress getPrimaryEmailAddress()
	{
		ContactAddress primaryEmail = null;
		List<ContactAddress> primaryEmails = ContactAddress.executeQuery( "select ca from ContactAddress as ca where ca.serviceType = ${ContactAddress.EMAIL} and " +
																		  " ca.profile = :profile and ca.primaryInType = true", [profile:this]  );
																	  
		if( primaryEmails != null && !primaryEmails.isEmpty() )
		{
			primaryEmail = primaryEmails.get(0);
		}
		
		return primaryEmail;
	}
	
	public ContactAddress getPrimaryPhoneNumber()
	{
		ContactAddress primaryPhoneNumber = null;
		
		List<ContactAddress> primaryPhoneNumbers = ContactAddress.executeQuery(  "select ca from ContactAddress as ca where ca.serviceType = ${ContactAddress.PHONE} and " +
																		  " ca.profile = :profile and ca.primaryInType = true", [profile:this] );
																	  
		if( primaryPhoneNumbers != null && !primaryPhoneNumbers.isEmpty() )
		{
			primaryPhoneNumber = primaryPhoneNumbers.get(0);
		}
																	  
		return primaryPhoneNumber;
	}
	
	public ContactAddress getPrimaryInstantMessenger()
	{
		ContactAddress primaryInstantMessenger = null;

		List<ContactAddress> primaryInstantMessengers = ContactAddress.executeQuery(  "select ca from ContactAddress as ca where " +
																					    "( ca.serviceType = ${ContactAddress.JABBER_IM} " + 
																						"  or ca.serviceType = ${ContactAddress.AOL_IM} " + 
																						"  or ca.serviceType = ${ContactAddress.MSN_IM} " +
																						"  or ca.serviceType = ${ContactAddress.YAHOO_IM} " +
																						") and ca.profile = :profile and ca.primaryInType = true", [profile:this] );
		
		if( primaryInstantMessengers != null && !primaryInstantMessengers.isEmpty() )
		{
			primaryInstantMessenger = primaryInstantMessengers.get(0);
		}
		
		return primaryInstantMessenger;
	}
	
	public List<ContactAddress> getPhoneNumbers()
	{
		List<ContactAddress> phoneNumbers = new ArrayList<ContactAddress>();
		
		List<ContactAddress> queryResults = ContactAddress.executeQuery( "select ca from ContactAddress as ca where " +
																		 " ca.serviceType = ${ContactAddress.PHONE} and " +
																		 " ca.profile = :profile", [profile: this] );
		if( queryResults != null )
		{
			phoneNumbers.addAll( queryResults );
		}
		
		return phoneNumbers;
	}
	
	public List<ContactAddress> getInstantMessengerAddresses()
	{
		List<ContactAddress> instantMessengerAddresses = new ArrayList<ContactAddress>();
		
		List<ContactAddress> queryResults = ContactAddress.executeQuery( "select ca from ContactAddress as ca where " +
																		 "( ca.serviceType = ${ContactAddress.JABBER_IM} " + 
																		 "  or ca.serviceType = ${ContactAddress.AOL_IM} " + 
																		 "  or ca.serviceType = ${ContactAddress.MSN_IM} " +
																		 "  or ca.serviceType = ${ContactAddress.YAHOO_IM} " +
																		 ") and ca.profile = :profile", [profile: this] );
		if( queryResults != null )
		{
			instantMessengerAddresses.addAll( queryResults );
		}
		
		return instantMessengerAddresses;
	}
	
	public List<ContactAddress> getEmailAddresses()
	{
		List<ContactAddress> emailAddresses = new ArrayList<ContactAddress>();
		
		List<ContactAddress> queryResults = ContactAddress.executeQuery( "select ca from ContactAddress as ca where " +
																		 " ca.serviceType = ${ContactAddress.EMAIL} and " +
																		 " ca.profile = :profile", [profile: this] );
		if( queryResults != null )
		{
			emailAddresses.addAll( queryResults );
		}
		
		return emailAddresses;
	}					
}
