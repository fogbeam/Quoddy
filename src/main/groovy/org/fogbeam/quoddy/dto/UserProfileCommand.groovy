package org.fogbeam.quoddy.dto

import java.text.SimpleDateFormat;
import java.util.List;

import org.fogbeam.quoddy.profile.ContactAddress
import org.fogbeam.quoddy.profile.EducationalExperience
import org.fogbeam.quoddy.profile.HistoricalEmployer
import org.fogbeam.quoddy.profile.Interest
import org.fogbeam.quoddy.profile.OrganizationAssociation
import org.fogbeam.quoddy.profile.Profile
import org.fogbeam.quoddy.profile.Skill

class UserProfileCommand
{
	public UserProfileCommand()
	{
		
	}
	
	/* note: work out the validation rules for dates on historical employment stuff.
	 * If you enter a month for FROM or TO, you must enter a year? If you enter a year,
	 * you may leave month blank (We'll default to Jan 1 of the year for sorting purposes?)
	 * If you enter a TO date, you must enter a FROM date.  Again, entering a month requires
	 * entering a corresponding year, but you can put a year without a month. 
	 */
	
	public UserProfileCommand( Profile profile, List months )
	{
		this.title = profile.title;
		this.userId = profile.owner.userId;
		this.userUuid = profile.owner.uuid;		
		this.sex = profile.sex;
		this.birthDayOfMonth = profile.birthDayOfMonth;
		this.birthMonth = profile.birthMonth;
		this.birthYear = profile.birthYear;
		this.summary = profile.summary;
		this.location = profile.location;
		this.hometown = profile.hometown;
		this.months = months;
		this.contactAddresses = new ArrayList<ContactAddress>();
		this.educationHistory = new ArrayList<EducationalExperience>();
		this.primaryPhone = profile.primaryPhoneNumber?.address;
		this.primaryEmail = profile.primaryEmailAddress?.address;
		this.primaryInstantMessenger = profile.primaryInstantMessenger?.address;		
		this.dotPlan = profile.dotPlan;
		
		this.phoneNumbers.addAll( profile.phoneNumbers );
		this.emailAddresses.addAll( profile.emailAddresses );
		this.instantMessengers.addAll( profile.instantMessengerAddresses );
		
		// deal with contact addresses...
		Set<ContactAddress> contactAddressSet = profile.contactAddresses;
		
		if( contactAddressSet )
		{
			this.contactAddressCount = contactAddressSet.size();
			this.contactAddresses.addAll( contactAddressSet );
		}
		else
		{
			this.contactAddressCount = 0;	
		}
		
		def sortClosure = { o1, o2 ->
				
				println "o1: \n" + o1;
				println "o2: \n" + o2;
				
				String monthTo1;
				String monthTo2;
				String monthFrom1;
				String monthFrom2;
				String yearTo1;
				String yearTo2;
				String yearFrom1;
				String yearFrom2;
				
				if( o1.monthTo != null )
				{
					monthTo1 = months.getAt( Integer.parseInt( o1.monthTo ) -1 ).text;
				}
				else
				{
					monthTo1 = "January";
				}
				
				if( o2.monthTo != null )
				{
					monthTo2 = months.getAt( Integer.parseInt( o2.monthTo ) -1 ).text;
				}
				else
				{
					monthTo2 = "January";
				}
				
				if( o1.monthFrom != null )
				{
					monthFrom1 = months.getAt( Integer.parseInt( o1.monthFrom ) -1 ).text;
				}
				else
				{
					monthFrom1 = "January";
				}
				
				if( o2.monthFrom != null )
				{
					monthFrom2 = months.getAt( Integer.parseInt( o2.monthFrom ) -1 ).text;	
				}
				else
				{
					monthFrom2 = "January";
				}
				
				if( o1.yearTo != null )
				{
					yearTo1 = o1.yearTo;
				}
				
				if( o2.yearTo != null )
				{
					yearTo2 = o2.yearTo;
				}
				
				if( o1.yearFrom != null )
				{
					yearFrom1 = o1.yearFrom;
				}
				
				if( o2.yearFrom != null )
				{
					yearFrom2 = o2.yearFrom;
				}
				
				
				Date fromDate1 = null;
				Date fromDate2 = null;
				Date toDate1 = null;
				Date toDate2 = null;
				if( monthFrom1 && yearFrom1 )
				{
					fromDate1 = dateFormat.parse( "$monthFrom1 $yearFrom1" );
				}
				else
				{
					println "NOT setting fromDate1!";	
				}
				
				if( monthFrom2 && yearFrom2 )
				{	
					fromDate2 = dateFormat.parse( "$monthFrom2 $yearFrom2" );
				}
				else
				{
					println "NOT setting fromDate2!  monthFrom2: $monthFrom2, yearFrom2: $yearFrom2";
				}
				
				if( monthTo1 && yearTo1 )
				{
					toDate1 = dateFormat.parse( "$monthTo1 $yearTo1" );
				}
				else
				{
					println "NOT setting toDate1!";
				}
				
				if( monthTo2 && yearTo2 )
				{
					toDate2 = dateFormat.parse( "$monthTo2 $yearTo2" );
				}
				else
				{
					println "NOT setting toDate2!";
				}
				
				// the possible scenarios we have to deal with? but default values above
				// eliminate what?

				// if there's start date and end date for both
				if( fromDate1 && toDate1 && fromDate2 && toDate2 )
				{
					println "have all data";
					
					// whichever began first should appear earlier in the sort order.
					// if began at the same time?
					if( !fromDate1.equals( fromDate2 ))
					{
						println "1. fromDates are NOT equal";
						
						if( fromDate1.getTime() < fromDate2.getTime() )
						{
							println "fromDate1 < fromDate 2, ret 1";
							return 1;	
						}
						else
						{
							println "fromDate1 > fromDate2, ret -1";
							return -1;	
						}
					}
					else
					{
						println "fromDates are the SAME";
						
						// they started at the same time, but we have end dates for both, how
						// would we factor that into the sort order?
						if( toDate1.getTime() < toDate2.getTime() )
						{
							println "toDate1 < toDate2, ret 1";
							return 1;	
						}
						else if( toDate1.getTime() > toDate2.getTime() )
						{
							println "toDate1 > toDate2, ret -1";
							return -1;	
						}
						else
						{
							println "toDate1 == toDate2, ret 0";
							return 0;	
						}
					}	
				}

				// if there's a start date but no end date for both
				else if( fromDate1 && fromDate2 && (!toDate1 && !toDate2))
				{
					println "there's a start date but no end date for both";
					
					if( fromDate1.getTime() < fromDate2.getTime() )
					{
						return 1;	
					}	
					else if( fromDate1.getTime() > fromDate2.getTime())
					{
						return -1;	
					}
					else
					{
						return 0;	
					}
				}
				
				// if there's a start date but no end date for o1
				else if( fromDate1 && !toDate1 )
				{
					println "there's a start date but no end date for o1";
					
					// implies there is a endDate for o2, right?	
					if( fromDate1.getTime() < fromDate2.getTime())
					{
						return 1;	
					}
					else if( fromDate1.getTime() > fromDate2.getTime())
					{
						return -1;
					}
					else
					{
						// use the presence of a toDate to break the tie?	
						return -1;
					}
				}
				
				// if there's a start date but no end date for o2
				else if( fromDate2 && !toDate2 )
				{
					println "there's a start date but no end date for o2";
					// implies there is an endDate for o1, right?
					if( fromDate1.getTime() < fromDate2.getTime())
					{
						return 1;
					}
					else if( fromDate1.getTime() > fromDate2.getTime())
					{
						return -1;
					}
					else
					{
						// use the presence of a toDate to break the tie?
						return 1;
					}
				}
				
				// o1 has no dates at all, but o2 does
				else if( !fromDate1 && !toDate1 && fromDate2 && toDate2 )
				{
					return 1;
				}
				// o2 has no dates at all, but o1 does
				else if( fromDate1 && toDate1 && !fromDate2 && !toDate2 )
				{
					return -1;
				}
				// if there's no dates for either?
				else if( !fromDate1 && !toDate1 && !fromDate2 && !toDate2 )
				{
					
					println "there's no dates for either?";
					return 1;
				}				
				
				else
				{
					println "WTF?!?";
					// has to be an error, can we ignore with some default handling? Since this is just sorting for
					// display purposes, probably yes.	
					return 1;
				}
			}
		
		
		println "Setting this.employmentHistory to: ${profile.employmentHistory}\n";
		if( profile.employmentHistory != null && profile.employmentHistory.size() > 0 )
		{
			this.employerCount = profile.employmentHistory.size();
			
			List<HistoricalEmployer> sortedEmploymentHistory = new ArrayList<HistoricalEmployer>();
			
			// sort the employment history set
			
			sortedEmploymentHistory = profile.employmentHistory.sort( sortClosure ); 
			
			this.employmentHistory = sortedEmploymentHistory;
		}
		else
		{
			this.employerCount = 0;
		}	

		// deal with contact addresses...
		Set<EducationalExperience> educationHistorySet = profile.educationHistory;
		
		if( educationHistorySet )
		{
			this.educationHistoryCount = educationHistorySet.size();
			
			List<EducationalExperience> sortedEducationalHistory = new ArrayList<EducationalExperience>();
			
			sortedEducationalHistory = educationHistorySet.sort( sortClosure );
			
			this.educationHistory = sortedEducationalHistory;
		}
		else
		{
			this.educationHistoryCount = 0;	
		}		
	
		Set<Interest> interests = profile.interests;
		for( Interest interest: interests )
		{
			this.interests += ( interest.name + "\n" );	
		}
		
		// TODO: deal with skills
		Set<Skill> skills = profile.skills;
		for( Skill skill: skills )
		{
			this.skills += (skill.name + "\n");
		}
		
		// TODO: deal with groupsOrgs	
		Set<OrganizationAssociation> organizations = profile.organizations;
		for( OrganizationAssociation organization: organizations )
		{
			this.groupsOrgs += (organization.name + "\n");
		}
	}
	
	String title;
	String userId;
	String userUuid;
	String sex;
	String birthYear;
	String birthMonth;
	String birthDayOfMonth;
	String summary;
	String location;
	String hometown;
	String languages;
	String interests = "";
	String skills = "";
	String groupsOrgs = "";
	List<HistoricalEmployer> employmentHistory;
	Integer employerCount;
	List<EducationalExperience> educationHistory;
	Integer educationHistoryCount;
	String links;
	// String contactAddresses;
	List<ContactAddress> contactAddresses;
	Integer contactAddressCount;
	String favorites;
	String projects;
	List months;
	String primaryPhone;
	String primaryEmail;
	String primaryInstantMessenger;
	String dotPlan;
	List<ContactAddress> phoneNumbers = new ArrayList<ContactAddress>();
	List<ContactAddress> emailAddresses = new ArrayList<ContactAddress>();
	List<ContactAddress> instantMessengers = new ArrayList<ContactAddress>();
	
	
	SimpleDateFormat dateFormat = new SimpleDateFormat( "MMM yyyy" );
}
