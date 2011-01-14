package org.fogbeam.quoddy

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH;
import org.fogbeam.quoddy.profile.Profile 

class UserController {

	def userService;
	def profileService;
	def scaffold = true;

	def sexOptions = [new SexOption( id:1, text:"Male" ), new SexOption( id:2, text:"Female" ) ];
	
	def viewUser = 
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
		
		[user:user];
		
	}

    def registerUser = 
	{ UserRegistrationCommand urc ->
    
		if( CH.config.enable.self.registration != true )
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
                // user.profile = new Profile( urc.properties );
                
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

	
	def addToFollow = 
	{
		
		def currentUser = null;
		if( !session.user ) 
		{
			flash.message = "Must be logged in before you can follow people";
		}
		else
		{
			println "follow: ${params.userId}";
		
			currentUser = userService.findUserByUserId( session.user.userId );
			
			def targetUser = userService.findUserByUserId( params.userId );
		
			userService.addToFollow( currentUser, targetUser );
			
		}
		
		render(view:'viewUser', model:[user:currentUser]);
			
	}
	
	def addToFriends = 
	{
		
		def currentUser = null;
		if( !session.user ) 
		{
			flash.message = "Must be logged in before you can add friends";
		}
		else
		{
			println "addToFriends: ${params.userId}";
		
			currentUser = userService.findUserByUserId( session.user.userId );
			
			def targetUser = userService.findUserByUserId( params.userId );
		
			userService.addToFriends( currentUser, targetUser );
			
		}
		
		render(view:'viewUser', model:[user:currentUser]);
	}

	def confirmFriend = 
	{
		println "confirmFriend";
		User currentUser = null;
		if( !session.user )
		{
			flash.message = "Must be logged in before you can list friends";
		}
		else
		{
			currentUser = userService.findUserByUserId( session.user.userId )
		}
	
		User newFriend = userService.findUserByUserId( params.confirmId )
		
		userService.confirmFriend( currentUser, newFriend );
		
		redirect( controller:'home', action:'index')	
	}
	
	def listFollowers =
	{
		def user = null;
		if( !session.user ) 
		{
			flash.message = "Must be logged in before you can list followers";
		}
		else
		{
			user = userService.findUserByUserId( session.user.userId )
		}
	
		List<User> followers = userService.listFollowers( user );
		
		[followers:followers];
	}
	
	def listFriends = 
	{

		def user = null;
		if( !session.user ) 
		{
			flash.message = "Must be logged in before you can list friends";
		}
		else
		{
			user = userService.findUserByUserId( session.user.userId )
		}
	
		List<User> friends = userService.listFriends( user );
		
		[friends:friends];
	}
		
	def listIFollow = 
	{
		
		def user = null;
		if( !session.user ) 
		{
			flash.message = "Must be logged in before you can list friends";
		}
		else
		{
			user = userService.findUserByUserId( session.user.userId )
		}
	
		List<User> iFollow = userService.listIFollow( user );
		
		[ifollow: iFollow];
	}
	
	def create = 
	{   
		println "enable self reg? "
		println CH.config.enable.self.registration;
		
		if( CH.config.enable.self.registration != true )
		{
			println "self registration is disabled";
			// if self registration isn't turned on, just bounce to the front-page here
			redirect( controller:'home', action:'index')
		}
		else
		{
			render(view:'create' );
		}
	}

	def list = 
	{
	
		List<User> allusers = userService.findAllUsers();
		
		println "Found ${allusers.size()} users\n";
		
		[users:allusers];
	}
		
	
	def editProfile = 
	{
		String userId;
		if( session.user )
		{
			userId = session.user.userId;
		}
		else
		{
			// error, must be logged in to do this...	
		}
		
		User user = userService.findUserByUserId( userId );
		UserProfileCommand upc = new UserProfileCommand(user.profile);
		[profileToEdit:upc, sexOptions:sexOptions];
	}
	
	def saveProfile =
	{ 
		UserProfileCommand upc ->
		
		String uuid = upc.userUuid;
		println "Looking for user by uuid: $uuid";
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
			println "sex: ${upc.sex}";
			profile.sex = Integer.parseInt( upc.sex );
		}
				
		println "location: ${upc.location}";
		profile.location = upc.location;
		println "hometown: ${upc.hometown}";
		profile.hometown = upc.hometown;
		
		
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
	
	def editAccount = 
	{
	
		def user = null;
		if( !session.user ) 
		{
			flash.message = "Must be logged in edit your profile!";
		}
		else
		{
			user = userService.findUserByUserId( session.user.userId )
		}
	
		[user:user];
	}	

	def saveAccount = 
	{ UserRegistrationCommand urc ->
	
		User user = new User( urc.properties );
		user = userService.updateUser( user );
		
		if( user )
		{
				flash.message = "Account updated, ${urc.displayName ?: urc.userId}";
				redirect(controller:'home', action: 'index')
		}
		else
		{
			flash.message = "Error updating account, ${urc.displayName ?: urc.userId}";
			render(view:'editAccount', model:[user:user]);
		}
	}
		
	def listOpenFriendRequests = {

		def user = null;
		List<FriendRequest> openFriendRequests = new ArrayList<FriendRequest>();
		if( !session.user )
		{
			flash.message = "Must be logged in to display pending requests!";
		}
		else
		{
			user = userService.findUserByUserId( session.user.userId );
		
			List<FriendRequest> temp = userService.listOpenFriendRequests( user );
			openFriendRequests.addAll( temp );
			
		}
			
		[openFriendRequests:openFriendRequests];		
	}	
	
	
}

class UserProfileCommand
{
	public UserProfileCommand()
	{
		
	}
	
	public UserProfileCommand( Profile profile )
	{
		this.userUuid = profile.owner.uuid;		
		this.sex = profile.sex;
		this.birthDayOfMonth = profile.birthDayOfMonth;
		this.birthMonth = profile.birthMonth;
		this.birthYear = profile.birthYear;
		this.summary = profile.summary;
		this.location = profile.location;
		this.hometown = profile.hometown;
	}
	
	String userUuid;
	String sex;
	String birthYear;
	String birthMonth;
	String birthDayOfMonth;
	String summary;
	String location;
	String hometown;
	String languages;
	String interests;
	String skills;
	String groupsOrgs;
	String employmentHistory;
	String educationHistory;
	String links;
	String contactAddresses;
	String favorites;
	String projects;
			
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