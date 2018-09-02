package org.fogbeam.quoddy;

import org.apache.commons.io.FilenameUtils
import org.fogbeam.quoddy.annotation.AnnotationResource
import org.fogbeam.quoddy.dto.UserProfileCommand
import org.fogbeam.quoddy.profile.ContactAddress
import org.fogbeam.quoddy.profile.EducationalExperience
import org.fogbeam.quoddy.profile.HistoricalEmployer
import org.fogbeam.quoddy.profile.Interest
import org.fogbeam.quoddy.profile.OrganizationAssociation
import org.fogbeam.quoddy.profile.Profile
import org.fogbeam.quoddy.profile.Skill
import org.fogbeam.quoddy.search.SearchResult
import org.fogbeam.quoddy.social.FriendRequest
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

import grails.plugin.springsecurity.annotation.Secured
import groovy.json.JsonBuilder


public class UserController 
{	
	def userService;
	def profileService;
	def searchService;
	def jenaService;

	def eventStreamService;
	
	
	def sexOptions = [new SexOption( id:1, text:"Male" ), new SexOption( id:2, text:"Female" ) ];
	def years =	 {
					def alist = [];
					Date now = new Date();
					(1900 .. (now.year + 1900)).each { num ->
						alist.add( new Year( id: num, text: "${num}" ) ); 
						}
					return alist;
				 }.call();
			
	def months = [ new Month( id:1, text:"January" ),
				   new Month( id:2, text:"February" ),
				   new Month( id:3, text:"March" ),
				   new Month( id:4, text:"April" ),
				   new Month( id:5, text:"May" ),
				   new Month( id:6, text:"June" ),
				   new Month( id:7, text:"July" ),
				   new Month( id:8, text:"August" ),
				   new Month( id:9, text:"September" ),
				   new Month( id:10, text:"October" ),
				   new Month( id:11, text:"November" ),
				   new Month( id:12, text:"December" ) ];
	
	def days = [ new Day( id:1, text:"01"),
				new Day( id:2, text:"02"),
				new Day( id:3, text:"03"),
				new Day( id:4, text:"04"),
				new Day( id:5, text:"05"),
				new Day( id:6, text:"06"),
				new Day( id:7, text:"07"),
				new Day( id:8, text:"08"),
				new Day( id:9, text:"09"),
				new Day( id:10, text:"10"),
				new Day( id:11, text:"11"),
				new Day( id:12, text:"12"),
				new Day( id:13, text:"13"),
				new Day( id:14, text:"14"),
				new Day( id:15, text:"15"),
				new Day( id:16, text:"16"),
				new Day( id:17, text:"17"),
				new Day( id:18, text:"18"),
				new Day( id:19, text:"19"),
				new Day( id:20, text:"20"),
				new Day( id:21, text:"21"),
				new Day( id:22, text:"22"),
				new Day( id:23, text:"23"),
				new Day( id:24, text:"24"),
				new Day( id:25, text:"25"),
				new Day( id:26, text:"26"),
				new Day( id:27, text:"27"),
				new Day( id:28, text:"28"),
				new Day( id:29, text:"29"),
				new Day( id:30, text:"30"),
				new Day( id:31, text:"31")
			];		   
		
	def contactTypes = [ new ContactTypeOption( id: ContactAddress.AOL_IM, text: "AOL IM"),
						new ContactTypeOption( id: ContactAddress.JABBER_IM, text: "Jabber (XMPP)"),
						new ContactTypeOption( id: ContactAddress.YAHOO_IM, text: "Yahoo IM"),
						new ContactTypeOption( id: ContactAddress.MSN_IM, text: "MSN Messenger"),
						new ContactTypeOption( id: ContactAddress.TWITTER, text: "Twitter"),
						new ContactTypeOption( id: ContactAddress.EMAIL, text: "Email" )
			];	

	def sampleForm = {
		[]	
	}	
				   

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def manageUsers()
	{
		List<User> users = new ArrayList<User>();
		
		String queryString = params.queryString;
		
		if( queryString )
		{
			// use search...
			List<SearchResult> searchResults = searchService.doUserSearch( queryString );
			for( SearchResult result : searchResults )
			{
				users.add( userService.findUserByUuid( result.uuid ));
			}
		}
		else
		{
			// otherwise, just grab everybody, up to the limit...
			String strPageNumber = params.pageNumber;
			int pageNumber = 1;
			if( strPageNumber )
			{
				pageNumber = Integer.parseInt( strPageNumber );
			}
			
			List<User> temp = userService.findAllUsers(30, pageNumber )
			if( temp )
			{
				users.addAll( temp );
			}
			
		}
		
		
		log.debug( "found ${users.size()} users" );
		
		[users:users];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def viewUser()
	{
		def userId = params.userId;
		def user = null;
		if( null != userId )
		{
			// lookup this specific user by params and put in the model for display	
			user = userService.findUserByUserId( userId );
            log.info( "Looked up User: ${user}");
		}
		else 
		{
			flash.message = "invalid userId";
		}
		
		
		List<ActivityStreamItem> activities = new ArrayList<ActivityStreamItem>();
		
		Map model = [user:user];
		if( user )
		{		
			activities = eventStreamService.getStatusUpdatesForUser( user );
            log.info( "adding user, activities to model");
			model.putAll( [user:user, activities:activities] );
			
		}
		
		
		/* load list of potential predicates for annotations */
		List<AnnotationResource> predicates = new ArrayList<AnnotationResource>();
		AnnotationResource hasSkill = new AnnotationResource( label: "Has Skill", shortName: "hasSkill", qualifiedName: "http://rdfs.org/resume-rdf/cv.rdfs#hasSkill" );
		predicates.add( hasSkill );
		AnnotationResource hasExpertise = new AnnotationResource(  label: "Has Expertise", shortName: "hasExpertise", qualifiedName: "http://schema.fogbeam.com/people#hasExpertise" );
		predicates.add( hasExpertise );
		model << [predicates:predicates];
		
		List<AnnotationResource> possibleObjects = new ArrayList<AnnotationResource>();
		AnnotationResource javaLanguage = new AnnotationResource(  label: "Java (Programming Language)", shortName: "Java", qualifiedName: "http://schema.fogbeam.com/skill#Java" );
		possibleObjects.add( javaLanguage );
		AnnotationResource marketing = new AnnotationResource(  label: "Marketing", shortName: "Marketing", qualifiedName: "http://schema.fogbeam.com/skill#Marketing" );
		possibleObjects.add( marketing);
		AnnotationResource finance = new AnnotationResource(  label: "Finance", shortName: "Finance", qualifiedName: "http://schema.fogbeam.com/skill#Finance" );
		possibleObjects.add( finance );
		AnnotationResource acmeWidgets = new AnnotationResource(  label: "Acme Widgets (Manufacturer)", shortName: "Acme Widgets", qualifiedName: "http://customers.fogbeam.com#Acme_Widgets" );
		possibleObjects.add( acmeWidgets);
		AnnotationResource boxerSteel = new AnnotationResource(  label: "Boxer Steel (Manufacturer)", shortName: "Boxer Steel", qualifiedName: "http://customers.fogbeam.com#Boxer_Steel" );
		possibleObjects.add( boxerSteel);
		AnnotationResource culletBoxes = new AnnotationResource(  label: "Cullet Boxes (Manufacturer)", shortName: "Cullet Boxes", qualifiedName: "http://customers.fogbeam.com#Cullet_Boxes" );
		possibleObjects.add( culletBoxes );
		
		model << [possibleObjects:possibleObjects];
		
		def forJSON = [ [qualifiedName: javaLanguage.qualifiedName, value:javaLanguage.label, tokens: [javaLanguage.shortName]],
						[qualifiedName: marketing.qualifiedName, value:marketing.label, tokens: [marketing.shortName]],
						[qualifiedName: finance.qualifiedName, value:finance.label, tokens: [finance.shortName]],
						[qualifiedName: acmeWidgets.qualifiedName,value:acmeWidgets.label, tokens: [acmeWidgets.shortName]],
						[qualifiedName: boxerSteel.qualifiedName,value:boxerSteel.label, tokens: [boxerSteel.shortName]],
						[qualifiedName: culletBoxes.qualifiedName, value:culletBoxes.label, tokens: [culletBoxes.shortName]]
						];
		
		
		JsonBuilder builder = new JsonBuilder();			
					
		builder( forJSON );
		
		log.debug( "JSON Output: ${builder.toPrettyString()}");
		
		model.putAt( "predicatesJSON", builder.toPrettyString());
		
		return model;
	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def editUser()
	{
		User user = userService.findUserByUuid(  params.id );
		
		[user:user];
	}
	
	
	/* TODO: Start a Webflow wizard here... */
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def adminAddUser()
	{
		
		[];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def adminSaveUser( UserRegistrationCommand urc )
	{	
		if( urc.hasErrors() )
		{
				log.warn( "UserRegistrationCommand object has errors");
				urc.errors.allErrors.each {log.debug( it) };
				flash.user = urc;
				flash.message = "Error creating user!";
				redirect( controller:'user', action:"adminAddUser" );
		}
		else
		{
			log.debug( "in adminSaveUser: about to call createUser()");
			
			def user = new User( urc.properties );
			user.password = urc.password;
			
			user = userService.createUser( user );
			
			if( user )
			{
					flash.message = "Account Created, ${urc.displayName ?: urc.userId}";
					redirect(controller:'user', action: 'manageUsers')
			}
			else
			{
				// maybe not unique userId?
				flash.user = urc;
				flash.message "Failed to create user account!";
				redirect( controller:'user', action:"adminAddUser" );
			}
		}
		
		
	}
	
	
	/* TODO: start a Webflow wizard here */
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def adminEditUser()
	{
		User user = userService.findUserByUuid(  params.id );
		
		[user:user];
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def adminUpdateUser( UserRegistrationCommand urc )
	{
		log.debug( "saving account for uuid: ${urc.uuid}");
		User user = userService.findUserByUuid( urc.uuid );
		if( user )
		{
			Map theNewProperties = new HashMap();
			theNewProperties.putAll( urc.properties );
			theNewProperties.remove( "userId" );
			user.properties = theNewProperties;
			
			log.debug( "updating user as: ${user.toString()}");
			
			user = userService.updateUser( user );
			
			if( user )
			{
					flash.message = "Account updated, ${urc.displayName ?: urc.userId}";
					log.debug(  "message: ${flash.message}");
					redirect(controller:'user', action: 'manageUsers')
			}
			else
			{
				flash.message = "Error updating account, ${urc.displayName ?: urc.userId}";
				log.debug( "message: ${flash.message}");
				// redirect(controller:'user', action: 'editUser');
				render(view:'adminEditUser', model:[user:user]);
			}
			
		}
		else
		{
			flash.message = "Error updating account, ${urc.displayName ?: urc.userId}";
			log.debug( "message: ${flash.message}");
			render(view:'adminEditUser', model:[user:user]);
		}

	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def disableUser()
	{
		User user = userService.findUserByUuid(  params.id );
		
		userService.disableUser( user );
		
		redirect( controller:'user', action:'manageUsers');
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def enableUser()
	{
		User user = userService.findUserByUuid(  params.id );
		
		userService.enableUser( user );
		
		redirect( controller:'user', action:'manageUsers');
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def deleteUser()
	{
		User user = userService.findUserByUuid(  params.id );
		
		userService.deleteUser( user );
		
		redirect( controller:'user', action:'manageUsers');
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def registerUser( UserRegistrationCommand urc )
    {
		if( grailsApplication.config.enable.self.registration != true )
		{
			redirect( controller:'home', action:'index')
			return;
		}
		
	    if( urc.hasErrors() )
        {
                flash.user = urc;
                redirect( action:"create" );
        }
        else
        {
                def user = new User( urc.properties );
                
				user = userService.createUser( user );
				
				if( user )
                {
                        flash.message = "Welcome Aboard, ${urc.displayName ?: urc.userId}";
                        redirect(controller:'home', action: 'index')
                }
                else
                {
                        // maybe not unique userId?
                        flash.user = urc;
                        redirect( action:"create" );
                }
        }
    }

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def addToFollow()
	{ 
	
		User currentUser = userService.getLoggedInUser();
		
		def targetUser = userService.findUserByUserId( params.userId );
	
		userService.addToFollow( currentUser, targetUser );		
		
		render( "OK" );	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def addToFriends()
	{
        log.info( "addToFriends() called");
        
		User currentUser = userService.getLoggedInUser();
				
        log.info( "currentUser: ${currentUser}" );
        
		def targetUser = userService.findUserByUserId( params.userId );
	
        log.info( "targetUser: ${targetUser}" );
        
		userService.addToFriends( currentUser, targetUser );
		
		render( "OK" );
	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def confirmFriend()
	{
		log.info( "confirmFriend() called" );
		
		User currentUser = userService.getLoggedInUser();
        
		User newFriend = userService.findUserByUserId( params.confirmId )
		
		userService.confirmFriend( currentUser, newFriend );
		
		redirect( controller:'home', action:'index')	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def listFollowers()
	{	
		User currentUser = userService.getLoggedInUser();
	
		List<User> followers = userService.listFollowers( currentUser );
		
		[followers:followers];
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def listFriends()
	{

		User currentUser = userService.getLoggedInUser();
		
		List<User> friends = userService.listFriends( currentUser );
		
		[friends:friends];
	}
		
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def listIFollow()
	{
		User currentUser = userService.getLoggedInUser();
				
		List<User> iFollow = userService.listIFollow( currentUser );
		
		[ifollow: iFollow];
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def create()
	{   
		log.debug( "enable self reg? ");
		log.debug( grailsApplication.config.enable.self.registration);
		
		if( grailsApplication.config.enable.self.registration != true )
		{
			log.debug( "self registration is disabled");
			// if self registration isn't turned on, just bounce to the front-page here
			redirect( controller:'home', action:'index')
		}
		else
		{
			render(view:'create' );
		}
	}

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def list()
	{
		List<User> allusers = userService.findAllUsers();
		
		log.debug( "Found ${allusers.size()} users\n");
		
		[users:allusers];
	}
		
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def viewUserProfile()
	{
		def userId = params.userId;
		def user = null;
		
		if( null != userId )
		{
			// lookup this specific user by params and put in the model for display
			user = userService.findUserByUserId( userId );
		}
		else
		{
			flash.message = "invalid userId";
		}
		
		
		def model = [user:user,sexOptions:sexOptions, years:years, months:months, days:days,
			contactTypes:contactTypes];
		
		
		/* load list of potential predicates for annotations */
		List<AnnotationResource> predicates = new ArrayList<AnnotationResource>();
		AnnotationResource hasSkill = new AnnotationResource( label: "Has Skill", shortName: "hasSkill", qualifiedName: "http://rdfs.org/resume-rdf/cv.rdfs#hasSkill" );
		predicates.add( hasSkill );
		AnnotationResource hasExpertise = new AnnotationResource(  label: "Has Expertise", shortName: "hasExpertise", qualifiedName: "http://schema.fogbeam.com/people#hasExpertise" );
		predicates.add( hasExpertise );
		model << [predicates:predicates];
		
		List<AnnotationResource> possibleObjects = new ArrayList<AnnotationResource>();
		AnnotationResource javaLanguage = new AnnotationResource(  label: "Java (Programming Language)", shortName: "Java", qualifiedName: "http://schema.fogbeam.com/skill#Java" );
		possibleObjects.add( javaLanguage );
		AnnotationResource marketing = new AnnotationResource(  label: "Marketing", shortName: "Marketing", qualifiedName: "http://schema.fogbeam.com/skill#Marketing" );
		possibleObjects.add( marketing);
		AnnotationResource finance = new AnnotationResource(  label: "Finance", shortName: "Finance", qualifiedName: "http://schema.fogbeam.com/skill#Finance" );
		possibleObjects.add( finance );
		AnnotationResource acmeWidgets = new AnnotationResource(  label: "Acme Widgets (Manufacturer)", shortName: "Acme Widgets", qualifiedName: "http://customers.fogbeam.com/Acme_Widgets" );
		possibleObjects.add( acmeWidgets);
		AnnotationResource boxerSteel = new AnnotationResource(  label: "Boxer Steel (Manufacturer)", shortName: "Boxer Steel", qualifiedName: "http://customers.fogbeam.com/Boxer_Steel" );
		possibleObjects.add( boxerSteel);
		AnnotationResource culletBoxes = new AnnotationResource(  label: "Cullet Boxes (Manufacturer)", shortName: "Cullet Boxes", qualifiedName: "http://customers.fogbeam.com/Cullet_Boxes" );
		possibleObjects.add( culletBoxes );
		
		model << [possibleObjects:possibleObjects];
		
		model << [possibleObjects:possibleObjects];
		
		def forJSON = [ [qualifiedName: javaLanguage.qualifiedName, value:javaLanguage.label, tokens: [javaLanguage.shortName]],
						[qualifiedName: marketing.qualifiedName, value:marketing.label, tokens: [marketing.shortName]],
						[qualifiedName: finance.qualifiedName, value:finance.label, tokens: [finance.shortName]],
						[qualifiedName: acmeWidgets.qualifiedName,value:acmeWidgets.label, tokens: [acmeWidgets.shortName]],
						[qualifiedName: boxerSteel.qualifiedName,value:boxerSteel.label, tokens: [boxerSteel.shortName]],
						[qualifiedName: culletBoxes.qualifiedName, value:culletBoxes.label, tokens: [culletBoxes.shortName]]
						];
		
		
		JsonBuilder builder = new JsonBuilder();			
					
		builder( forJSON );
		
		log.debug( "JSON Output: ${builder.toPrettyString()}" );
		
		model.putAt( "predicatesJSON", builder.toPrettyString() );
		
		return model;
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def editProfile()
	{
		User currentUser = userService.getLoggedInUser();
		
		UserProfileCommand upc = new UserProfileCommand(currentUser.profile, months );
		[profileToEdit:upc, sexOptions:sexOptions, years:years, months:months, days:days,
			contactTypes:contactTypes];
	}
	

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfilePrimaryPhone()
    {
		log.debug( "Params: \n ${params}");
		
		String userId = params.id;
		log.debug( "Looking for user by userId: $userId" );
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user" );
			profile = user.profile;
			String newPrimaryPhone = params.primaryPhoneInput;
			if( newPrimaryPhone != null )
			{
				ContactAddress currentPrimaryPhone = profile.primaryPhoneNumber;
				if( currentPrimaryPhone != null )
				{
					currentPrimaryPhone.setPrimaryInType( false );
					currentPrimaryPhone.save(flush:true);
				}
				
				ContactAddress newPrimaryPhoneCA = new ContactAddress();
				newPrimaryPhoneCA.primaryInType = true;
				newPrimaryPhoneCA.address = newPrimaryPhone.trim();
				newPrimaryPhoneCA.profile = profile;
				newPrimaryPhoneCA.serviceType = ContactAddress.PHONE;
				
				
				if( !newPrimaryPhoneCA.save(flush:true) )
				{
					log.error( "Error saving newPrimaryPhoneCA!");
					newPrimaryPhoneCA.error.allErrors.each { log.debug(it);}
				}
				else
				{
					log.debug( "newPrimaryPhoneCA saved OK" );
				}
				
				
				profile.addToContactAddresses( newPrimaryPhoneCA );
				
				if( !profile.save(flush:true) )
				{
					log.error( "Error saving profile..." );
					profile.errors.allErrors.each { log.debug(it); }
				}
				else
				{
					log.debug( "profile saved OK" );
				}
			}
			
		}
				
		render( profile.primaryPhoneNumber.address );
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfilePrimaryEmail() 
    {
		log.debug( "Params: \n ${params}" );

		String userId = params.id;
		log.debug( "Looking for user by userId: $userId");
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user" );
			profile = user.profile;
			String newPrimaryEmail = params.primaryEmailInput;
			if( newPrimaryEmail != null )
			{
				ContactAddress currentPrimaryEmail = profile.primaryEmailAddress;
				if( currentPrimaryEmail != null )
				{
					currentPrimaryEmail.setPrimaryInType( false );
					currentPrimaryEmail.save(flush:true);
				}
				
				ContactAddress newPrimaryEmailCA = new ContactAddress();
				newPrimaryEmailCA.primaryInType = true;
				newPrimaryEmailCA.address = newPrimaryEmail.trim();
				newPrimaryEmailCA.profile = profile;
				newPrimaryEmailCA.serviceType = ContactAddress.EMAIL;
				
				
				if( !newPrimaryEmailCA.save(flush:true) )
				{
					log.error( "Error saving newPrimaryEmailCA!");
					newPrimaryEmailCA.error.allErrors.each { log.debug(it);}
				}
				else
				{
					log.debug( "newPrimaryEmailCA saved OK");
				}
				
				
				profile.addToContactAddresses( newPrimaryEmailCA );
				
				if( !profile.save(flush:true) )
				{
					log.error( "Error saving profile...");
					profile.errors.allErrors.each { log.debug(it); } 
				}
				else
				{
					log.debug( "profile saved OK" );
				}
			}
			
		}
				
		render( profile.primaryEmailAddress.address );
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfilePrimaryInstantMessenger()
    {
		log.debug( "Params: \n ${params}");
		
		String userId = params.id;
		log.debug( "Looking for user by userId: $userId");
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user" );
			profile = user.profile;
			String newPrimaryInstantMessenger = params.primaryInstantMessengerInput;
			if( newPrimaryInstantMessenger != null )
			{
				ContactAddress currentPrimaryInstantMessenger = profile.primaryInstantMessenger;
				if( currentPrimaryInstantMessenger != null )
				{
					currentPrimaryInstantMessenger.setPrimaryInType( false );
					currentPrimaryInstantMessenger.save(flush:true);
				}
				
				ContactAddress newPrimaryInstantMessengerCA = new ContactAddress();
				newPrimaryInstantMessengerCA.primaryInType = true;
				newPrimaryInstantMessengerCA.address = newPrimaryInstantMessenger.trim();
				newPrimaryInstantMessengerCA.profile = profile;
				newPrimaryInstantMessengerCA.serviceType = ContactAddress.JABBER_IM;
				
				
				if( !newPrimaryInstantMessengerCA.save(flush:true) )
				{
					log.error( "Error saving newPrimaryInstantMessengerCA!");
					newPrimaryInstantMessengerCA.error.allErrors.each { log.debug(it);}
				}
				else
				{
					log.debug( "newPrimaryInstantMessengerCA saved OK");
				}
				
				
				profile.addToContactAddresses( newPrimaryInstantMessengerCA );
				
				if( !profile.save(flush:true) )
				{
					log.error( "Error saving profile...");
					profile.errors.allErrors.each { log.debug(it); } 
				}
				else
				{
					log.debug( "profile saved OK");
				}
			}
			
			render( profile.primaryInstantMessenger.address );
		}
		else
		{
			render(status: 503, text: 'User not found')
		}
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveEmploymentHistoryEntry()
	{
		String userId = params.id;
		log.debug( "Looking for user by userId: $userId");
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user" );
			profile = user.profile;
		
			HistoricalEmployer emp1 = new HistoricalEmployer( companyName: params.companyName,
				monthTo: params.monthTo,
				monthFrom: params.monthFrom,
				yearTo: params.yearTo,
				yearFrom: params.yearFrom,
				title: params.title,
				description: "description" );
			
			if( !emp1.save(flush:true) )
			{
				log.error( "Saving new HistoricalEmployer Record failed");
				emp1.errors.allErrors.each { log.debug(it) };
			}

			profile.addToEmploymentHistory( emp1 );
			profile.save(flush:true);
			
			render( "OK" );
		}
		else
		{
			// TODO: return failure code and put message in flash scope
		}
			
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveEducationHistoryEntry()
	{
		log.debug( "Params:\n${params}");
				
		String userId = params.id;
		log.debug( "Looking for user by userId: $userId");
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user" );
			profile = user.profile;
		
		
			EducationalExperience newEducationalExperience =
			new EducationalExperience( institutionName: params.institutionName,
									monthFrom: params.monthFrom,
									yearFrom: params.yearFrom,
									monthTo: params.monthTo,
									yearTo: params.yearTo,
									courseOfStudy: params.major,
									description: "description" );

			if( !newEducationalExperience.save(flush:true) )
			{
				log.error( "Saving new EducationalExperience Record failed");
				newEducationalExperience.errors.allErrors.each { log.debug(it) };
			}
			else
			{
				log.debug( "newEducationalExperience saved");
			}

		
			profile.addToEducationHistory( newEducationalExperience );
			log.debug( "added newEducationalExperience to profile");
			profile.save(flush:true);
		
			render( "OK" );
		}
		else
		{
			// TODO: return failure code and put message in flash scope
		}	
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfileLocation()
    {
		log.debug( "Params: \n ${params}");

		String userId = params.id;
		log.debug( "Looking for user by userId: $userId");
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user");
			profile = user.profile;
			String newLocation = params.locationInput;
			if( newLocation != null )
			{
				profile.location = newLocation.trim();
				profile.save(flush:true);
			}
			
			render( profile.location );
		}
		else
		{
			render(status: 503, text: 'User not found')
		}

	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfileDotPlan()
    {
		log.debug( "Params: \n ${params}");
		
		String userId = params.id;
		log.debug( "Looking for user by userId: $userId");
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user" );
			profile = user.profile;
			String newDotPlan = params.dotPlanInput;
			if( newDotPlan != null )
			{
				profile.dotPlan = newDotPlan.trim();
				profile.save(flush:true);
			}
			
			render( profile.dotPlan );
		}
		else
		{
			render(status: 503, text: 'User not found')
		}
	}
	
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfileTitle()
	{
		log.debug( "params:\n ${params}" );
		log.debug("Saving Title!");
		
		String userId = params.id;
		log.debug( "Looking for user by userId: $userId");
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user");
			profile = user.profile;
			String newTitle = params.titleInput;
			if( newTitle != null )
			{
				profile.title = newTitle.trim();
				profile.save(flush:true);
			}
			
			render( profile.title );
		}
		else
		{
			render(status: 503, text: 'User not found')
		}
		
	}
	
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfileSummary()
	{
		log.debug( "params:\n ${params}" );
		
		// TODO: update summary and return... 
		// summaryInput:Phil is a rad dude, id:prhodes
		log.debug("Saving Summary!");
		
		String userId = params.id;
		log.debug("Looking for user by userId: $userId");
		
		User user = userService.findUserByUserId( userId );
		Profile profile = null;
		if( user != null )
		{
			log.debug( "found user");
			profile = user.profile;
			String newSummary = params.summaryInput;
			if( newSummary != null )
			{
				profile.summary = newSummary.trim();
				profile.save(flush:true);
			}
			
			render( profile.summary );	
		}
		else
		{
			render(status: 503, text: 'User not found')
		}
		
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfileAvatarPic()
	{
		log.debug( "Saving Avatar Image!");
		
		String uuid = params.userUuid;
		log.debug( "Looking for user by uuid: $uuid");
		
		User user = userService.findUserByUuid( uuid );
		Profile profile = user.profile;
		
		if(request instanceof MultipartHttpServletRequest)
		{
			log.debug( "is multipart");
			MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;
		  	CommonsMultipartFile f = (CommonsMultipartFile) mpr.getFile("your_photo");
		  	/* def f = request.getFile('myFile')*/
		  	if( f != null && !f.empty) 
			{
				log.debug( "uploading photo file...");
				
				  // f.transferTo( new File("/tmp/myfile.png") );
		  	
				  /* copy image to a known location for user profile pictures, and
				   * resize to thumbnails, etc. as appropriate
				   */
				  
				  // use quoddy.home variable here
				  String quoddyHome = System.getProperty( "quoddy.home" );
				  
				  File profilePicFile = new File("${quoddyHome}/profilepics/${user.userId}/${user.userId}_profile.jpg");
				  if( !profilePicFile.exists() )
				  {
					  	File parentDir = profilePicFile.getParentFile();
						if( !parentDir.exists() )
						{
							parentDir.mkdirs();
						}
						profilePicFile.createNewFile();  
				  }

				  f.transferTo( profilePicFile  );
				  
				  def convert = ["/usr/bin/convert","/opt/local/bin/convert"].find( { new File(it as String).exists() });
				  File thumbnail = new File( profilePicFile.getParentFile(), FilenameUtils.getBaseName(profilePicFile.getName()) + "_thumbnail48x48.jpg" );
				  ProcessBuilder pb = new ProcessBuilder()
						  .command(convert, profilePicFile.getName(), "-thumbnail", "48x48!", thumbnail.getName()).directory(profilePicFile.getParentFile());
				  
				  int result = pb.start().waitFor()
				  
				  if( result != 0 ){
					throw new RuntimeException("thumbnail generation failured, return code:" + result);
				  }
			  }
			  else
			  {
				log.error( "ERROR: did not find file in upload!");
				
			  }
			  
			  
			  render( "OK" );
			  
		}
		else
		{
			log.debug( "not multipart" );
		}
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def saveProfile( UserProfileCommand upc )
	{	
		String uuid = upc.userUuid;
		log.debug( "Looking for user by uuid: $uuid" );
		User user = userService.findUserByUuid( uuid );
		Profile profile = user.profile;
		
		profile.summary = upc.summary;
		if( upc.birthMonth )
		{
			profile.birthMonth = Integer.parseInt( upc.birthMonth );
		}
		if( upc.birthDayOfMonth )
		{
			profile.birthDayOfMonth = Integer.parseInt( upc.birthDayOfMonth );
		}
		if( upc.birthYear )
		{
			profile.birthYear = Integer.parseInt( upc.birthYear );
		}
		
		if( upc.sex )
		{
			profile.sex = Integer.parseInt( upc.sex );
		}
				
		profile.location = upc.location;
		profile.hometown = upc.hometown;
		
		Set paramsNames = params.keySet();
		paramsNames.each { 
			
			if( it.startsWith( "employment[" ) && it.endsWith( "]" ))
			{
				def emp1v = params.get( it );
				
				// is there an ID? Is it valid?  If so, update existing record for profile
				String histEmpIdStr = emp1v.historicalEmploymentId;
				Integer histEmpId = 0;
				if( histEmpIdStr )
				{
					histEmpId = Integer.parseInt(histEmpIdStr);
				}
				
				if( histEmpId > 0 )
				{
					// TODO: use a service for this?
					String monthTo = null;
					String monthFrom = null;
					String yearTo = null;
					String yearFrom = null;
					
					if( emp1v.monthTo && !emp1v.monthTo.isEmpty() )
					{
						monthTo = emp1v.monthTo;
					}

					if( emp1v.monthFrom && !emp1v.monthFrom.isEmpty() )
					{
						monthFrom = emp1v.monthFrom;
					}
										
					if( emp1v.yearTo && !emp1v.yearTo.isEmpty() )
					{
						yearTo = emp1v.yearTo;
					}

					if( emp1v.yearFrom && !emp1v.yearFrom.isEmpty() )
					{
						yearFrom = emp1v.yearFrom;
					}
					
					
					HistoricalEmployer existingHistEmp = HistoricalEmployer.findById( histEmpId );
					existingHistEmp.companyName = emp1v.companyName;
					existingHistEmp.monthTo = monthTo;
					existingHistEmp.monthFrom = monthFrom;
					existingHistEmp.yearTo = yearTo;
					existingHistEmp.yearFrom = yearFrom;
					existingHistEmp.title = emp1v.title;
					existingHistEmp.description = emp1v.description;
					
					if( !existingHistEmp.save(flush:true) )
					{
						log.error( "updating histemp record failed!" );	
					}
					
					
				}
				// else, create new record and attach to profile
				else
				{
					String monthTo = null;
					String monthFrom = null;
					String yearTo = null;
					String yearFrom = null;
					
					if( emp1v.monthTo && !emp1v.monthTo.isEmpty() )
					{
						monthTo = emp1v.monthTo;
					}

					if( emp1v.monthFrom && !emp1v.monthFrom.isEmpty() )
					{
						monthFrom = emp1v.monthFrom;
					}
										
					if( emp1v.yearTo && !emp1v.yearTo.isEmpty() )
					{
						yearTo = emp1v.yearTo;
					}

					if( emp1v.yearFrom && !emp1v.yearFrom.isEmpty() )
					{
						yearFrom = emp1v.yearFrom;
					}
										
					HistoricalEmployer emp1 = new HistoricalEmployer( companyName: emp1v.companyName,
																		monthTo: monthTo,
																		monthFrom: monthFrom,
																		yearTo: yearTo,
																		yearFrom: yearFrom,
																		title: emp1v.title,
																		description: emp1v.description );
					if( !emp1.save(flush:true) )
					{
						log.error( "Saving new HistoricalEmployer Record failed");
						emp1.errors.allErrors.each { log.debug( it ) };
					}
					
					profile.addToEmploymentHistory( emp1 );
				}
			}
			else if( it.startsWith( "contactAddress[" ) && it.endsWith( "]" ))
			{
				def contactAddress = params.get( it );
				
				log.debug( "contactAddress: ${contactAddress}" );
				
				// is there an ID? Is it valid?  If so, update existing record for profile
				String contactAddressIdStr = contactAddress.contactAddressId;
				Integer contactAddressId = Integer.parseInt(contactAddressIdStr);
				if( contactAddressId > 0 )
				{
					// TODO: use a service for this?
					ContactAddress existingContactAddress = ContactAddress.findById( contactAddressId );
					if( contactAddress.serviceType != null )
					{
						existingContactAddress.serviceType = Integer.parseInt( contactAddress.serviceType );
						existingContactAddress.address = contactAddress.address;
					}
					
					if( !existingContactAddress.save(flush:true) )
					{
						log.error( "updating contact address record failed!");
					}
					
					
				}
				// else, create new record and attach to profile
				else
				{
					if( contactAddress.serviceType && contactAddress.address )
					{
					
						ContactAddress newContactAddress = new ContactAddress( serviceType: Integer.parseInt( contactAddress.serviceType ),
																			address: contactAddress.address );

						if( !newContactAddress.save(flush:true) )
						{
							log.error( "Saving new ContactAddress Record failed" );
							newContactAddress.errors.allErrors.each { log.debug(it) };
						}
					    else
					    {
							log.debug( "newContactAddress saved" );	
					    }
					
						profile.addToContactAddresses( newContactAddress );
					}
				}
			}
			else if( it.startsWith( "education[" ) && it.endsWith( "]" ))
			{
				
				def educationalHistory = params.get( it );
								
				// is there an ID? Is it valid?  If so, update existing record for profile
				String educationalExperienceIdStr = educationalHistory.educationalExperienceId;
				Integer educationalExperienceId = Integer.parseInt(educationalExperienceIdStr);
				if( educationalExperienceId > 0 )
				{
					// TODO: use a service for this?
					EducationalExperience existingEducationalExperience = EducationalExperience.findById( educationalExperienceId );
	
					existingEducationalExperience.institutionName = educationalHistory.institutionName;
					existingEducationalExperience.monthFrom = ( educationalHistory.monthFrom != null &&
																!educationalHistory.monthFrom.isEmpty() ) ? educationalHistory.monthFrom: null;
					
					existingEducationalExperience.yearFrom = ( educationalHistory.yearFrom != null &&
																!educationalHistory.yearFrom.isEmpty() ) ? educationalHistory.yearFrom: null;
					
					existingEducationalExperience.monthTo = ( educationalHistory.monthTo != null &&
																!educationalHistory.monthTo.isEmpty() ) ? educationalHistory.monthTo: null;
					
					existingEducationalExperience.yearTo = ( educationalHistory.yearTo != null &&
																!educationalHistory.yearTo.isEmpty() ) ? educationalHistory.yearTo: null;
					
					existingEducationalExperience.courseOfStudy = ( educationalHistory.major != null &&
																!educationalHistory.major.isEmpty() ) ? educationalHistory.major: null;
					
					existingEducationalExperience.description = ( educationalHistory.description != null &&
																!educationalHistory.description.isEmpty() ) ? educationalHistory.description: null;
					
					
					if( !existingEducationalExperience.save(flush:true) )
					{
						log.error( "updating educational experience record failed!" );
					}
					
					
				}
				// else, create new record and attach to profile
				else
				{
					String monthFrom = ( educationalHistory.monthFrom != null &&
						!educationalHistory.monthFrom.isEmpty() ) ? educationalHistory.monthFrom: null;

					String yearFrom = ( educationalHistory.yearFrom != null &&
						!educationalHistory.yearFrom.isEmpty() ) ? educationalHistory.yearFrom: null;

					String monthTo = ( educationalHistory.monthTo != null &&
						!educationalHistory.monthTo.isEmpty() ) ? educationalHistory.monthTo: null;

					String yearTo = ( educationalHistory.yearTo != null &&
						!educationalHistory.yearTo.isEmpty() ) ? educationalHistory.yearTo: null;

					String courseOfStudy = ( educationalHistory.major != null &&
						!educationalHistory.major.isEmpty() ) ? educationalHistory.major: null;

					String description = ( educationalHistory.description != null &&
						!educationalHistory.description.isEmpty() ) ? educationalHistory.description: null;

					
					EducationalExperience newEducationalExperience = 
							new EducationalExperience( institutionName: educationalHistory.institutionName,
														monthFrom: monthFrom,
														yearFrom: yearFrom,
														monthTo: monthTo,
														yearTo: yearTo,
														courseOfStudy: courseOfStudy,
														description: description );

					if( !newEducationalExperience.save(flush:true) )
					{
						log.error( "Saving new EducationalExperience Record failed" );
						newEducationalExperience.errors.allErrors.each { log.debug( it ) };
					}
					else
					{
						log.debug( "newEducationalExperience saved" );
					}
					
					
					profile.addToEducationHistory( newEducationalExperience );
					
				}
			}
		};

		// upc.interests
		log.debug( "Interests: " + upc.interests );
		String[] interestsLines = upc.interests.split("\n" );
		for( String interestLine : interestsLines )
        {
			// TODO: deal with duplicates
			
			if( interestLine.contains("," ))
			{
				// TODO: deal with comma separated values
			}
			else
			{
				
				Interest interest = Interest.findByName( interestLine );
				if( !interest )
				{
					interest = new Interest( name: interestLine );
					if( !interest.save(flush:true) )
					{
						throw new RuntimeException( "FAIL" );
					}
						
				}
				
				profile.addToInterests( interest );
			}
		}
		
        
		// upc.skills
		log.debug( "Skills: " + upc.skills );
		String[] skillsLines = upc.skills.split("\n" );
		for( String skillsLine : skillsLines )
		{
			if( skillsLine.contains("," ))
			{
				// TODO: deal with comma separted values
			}
			else
			{
				
				
				Skill skill = Skill.findByName( skillsLine );
				if( !skill )
				{
					skill = new Skill( name: skillsLine );
					if( !skill.save(flush:true) )
					{
						throw new RuntimeException( "FAIL" );
					}
						
				}
				
				profile.addToSkills( skill );
			}
		}
		
		
		// upc.groupsOrgs
		log.debug( "GroupsOrgs: " + upc.groupsOrgs );
		String[] groupsOrgsLines = upc.groupsOrgs.split("\n" );
		for( String groupsOrgLine : groupsOrgsLines )
		{
			if( groupsOrgLine.contains("," ))
			{
				// TODO: deal with comma separted values
			}
			else
			{
				OrganizationAssociation org = OrganizationAssociation.findByName( groupsOrgLine );
				if( !org )
				{
					org = new OrganizationAssociation( name: groupsOrgLine );
					if( !org.save(flush:true) )
					{
						throw new RuntimeException( "FAIL" );
					}
						
				}
				
				profile.addToOrganizations( org );
			}
		}
		
		try
		{
			profileService.updateProfile( profile );
		}
		catch( Exception e )
		{
			return[profileToEdit:upc ];		
		}
		
		// reload user to get changes?
		user = userService.findUserByUuid( uuid );
		
		// userHome/index/prhodes
		redirect(controller:"userHome",action:"index", params:[id:user.userId]);
	}
	
    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def editAccount()
	{
		User user = userService.getLoggedInUser();
		
		[user:user];
	}	

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def listOpenFriendRequests()
    {
        log.info( "listOpenFriendRequests() called" );
        
		List<FriendRequest> openFriendRequests = new ArrayList<FriendRequest>();

		User user = userService.getLoggedInUser();
		
        log.info( "User: " + user );
        		
		List<FriendRequest> temp = userService.listOpenFriendRequests( user );
		openFriendRequests.addAll( temp );
		
        log.debug( "found ${openFriendRequests.size()} open friend requests" );
        	
		[openFriendRequests:openFriendRequests];		
	}	

	
	// TODO: refactor this to put the logic in the jenaService and make a reusable service we can
	// use from multiple places...
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
    def addAnnotation()
	{
		log.info( "addAnnotation()" );
		
		// add an annotation, possibly about a skill, or maybe a reference to
		// a Customer or Account or Product or other entity, to the targeted
		// user...
		
		String userId = params.userId;
		log.info( "adding annotation for User: ${userId}");
		User user = userService.findUserByUserId( userId );
		
		String annotationPredicate = params.annotationPredicate;
		log.debug( "annotationPredicate: ${annotationPredicate}");
		
		String annotationObject = params.annotationObject;
		log.debug( "annotationObject: ${annotationObject}");
		
		String annotationObjectQN = params.annotationObjectQN;
		log.debug( "annotationObjectQN: ${annotationObjectQN}");

		jenaService.addUserAnnotation( user, annotationPredicate, annotationObjectQN)		
		
		render( "OK" );
	}
		
	
}

class UserRegistrationCommand
{
	String uuid;
	String userId;
	String password;
	String passwordRepeat;

	byte[] photo;
	// String fullName;
	String firstName;
	String lastName;
	String displayName;
	String bio;
	String homepage;
	String email;
	String timezone;
	String country;
	String jabberAddress;

	static constraints = {
		userId( size: 3..20)
		password( size:6..8, blank:false, validator : {password, urc -> return password != urc.userId } );
		passwordRepeat( nullable:false, validator : {password2, urc -> return password2 == urc.password } );

		
		// fullName( nullable:true );
		bio( nullable:true, maxSize:1000 );
		homepage( url:true, nullable:true);
		email(email:true, nullable:true);
		photo( nullable:true);
		country( nullable:true);
		timezone( nullable:true);
		jabberAddress( email:true, nullable:true);

	}
}

class SexOption
{
	int id;
	String text;	
}

class ContactTypeOption
{
	int id;
	String text;	
}

class Month
{
	int id;
	String text;	
}

class Day
{
	int id;
	String text;	
}

class Year
{
	int id;
	String text;	
}