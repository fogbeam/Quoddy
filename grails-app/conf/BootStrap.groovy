import grails.util.Environment

import org.fogbeam.quoddy.AccountRole
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.profile.Profile
import org.fogbeam.quoddy.stream.EventType
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.constants.EventTypeNames

class BootStrap {

	def ldapTemplate;
	def userService;
	def siteConfigService;
	def searchService;
	def environment;
	
	def init = { servletContext ->
     
		
		
		 switch( Environment.current  )
	     {
	         case Environment.DEVELOPMENT:
			 	 createRoles();
	             createSomeUsers();
				 createShareTargets();
				 createEventTypes();
	             break;
	         case Environment.PRODUCTION:
	             println "No special configuration required";
				 createRoles();
				 createSomeUsers();
				 createShareTargets();
				 createEventTypes();
				 break;
	     }     
     
	     
	     searchService.initializeGeneralIndex();
		 searchService.initializePersonIndex();		 
		 
     }
     
     def destroy = {
    		 
    	// nothing, yet...
    	
     }

	 void createEventTypes()
	 {
	 	
		 // create these in a loop off the EventTypes enum
		 
		 for( EventTypeNames eventTypeName : EventTypeNames.values() )
		 {
		 	EventType et = EventType.findByName( eventTypeName.name );
			if( et == null )
			{
				et = new EventType( name: eventTypeName.name );
				et.save();
			}
		 }
		
	 }
	 
	 void createShareTargets()
	 {
		 ShareTarget streamPublicTarget = ShareTarget.findByName( ShareTarget.STREAM_PUBLIC );
		 if( !streamPublicTarget ) {
			 println "Creating new ${ShareTarget.STREAM_PUBLIC} ShareTarget";
			 streamPublicTarget = new ShareTarget();
			 streamPublicTarget.name = ShareTarget.STREAM_PUBLIC;
			 streamPublicTarget.save();
		 }
		 else 
		 {
		 	println "Found existing ${ShareTarget.STREAM_PUBLIC} ShareTarget"; 
		 }
	 }
	 
	 
	 void createRoles()
	 {
		 
		 println "Creating roles...";
		 AccountRole userRole = userService.findAccountRoleByName( "user" );
		 if( userRole != null )
		 {
			 println "Existing AccountRole user found";
		 }
		 else
		 {
			 println "No existing AccountRole user found, so creating now...";
			 
			userRole = new AccountRole( name: "user" );
			userRole.addToPermissions( "activityStream:*" );
			userRole.addToPermissions( "comment:*" );
			userRole.addToPermissions( "home:*" );
			userRole.addToPermissions( "login:*" );
			userRole.addToPermissions( "openSocial:*" );
			userRole.addToPermissions( "profilePic:*" );
			userRole.addToPermissions( "search:*" );
			userRole.addToPermissions( "status:*" );
			userRole.addToPermissions( "subscription:*" );
			userRole.addToPermissions( "tag:*" );
			userRole.addToPermissions( "user:*" );
			userRole.addToPermissions( "userGroup:*" );
			userRole.addToPermissions( "userHome:*" );
			userRole.addToPermissions( "userList:*" );
			userRole.addToPermissions( "userStream:*" );
			
			userRole = userService.createAccountRole( userRole ); 
			
			if( !userRole )
			{
				println "Error creating userRole";
			}

		 }
		 
		 AccountRole adminRole = userService.findAccountRoleByName( "admin" );
		 if( adminRole != null )
		 {
			 println "Existing AccountRole admin found";
		 }
		 else
		 {
			 println "No existing AccountRole admin found, so creating now...";
			 
			 adminRole = new AccountRole( name: "admin" );
			 
			 adminRole.addToPermissions( "admin:*" );
			 adminRole.addToPermissions( "calendar:*" );
			 adminRole.addToPermissions( "special:*" );
			 adminRole.addToPermissions( "dummy:*" );
			 adminRole.addToPermissions( "reports:*" );
			 adminRole.addToPermissions( "schedule:*" );
			 adminRole.addToPermissions( "siteConfigEntry:*" );
			 adminRole.addToPermissions( "userSettings:*" );
			 adminRole.addToPermissions( "importUser:*" );
			 adminRole.addToPermissions( "installer:*" );
			 
			 adminRole = userService.createAccountRole( adminRole );
		 
			 if( !adminRole )
			 {
				 println "Error creating adminRole";
			 }
			 
		 }

		 
		 
	 }
	 
     void createSomeUsers()
     {
		 println "Creating some users!";
     
		 AccountRole userRole = userService.findAccountRoleByName( "user" );
		 
		 if( userRole == null )
		 {
			 println "did not locate user role!";
		 }
		 
		 
		 AccountRole adminRole = userService.findAccountRoleByName( "admin" );
		 if( adminRole == null )
		 {
			 println "did not locate admin role!";
		 }

		 
		 boolean prhodesFound = false;
 
		 User userPrhodes= userService.findUserByUserId( "prhodes" );

		 if( userPrhodes != null )
		 {
			  println "Found existing prhodes user!";

		 }
		 else
	 	 {	
			  println "Could not find prhodes";
			  println "Creating new prhodes user";
			  userPrhodes = new User();
			  userPrhodes.uuid = "abc123";
			  userPrhodes.displayName = "Phillip Rhodes";
			  userPrhodes.firstName = "Phillip";
			  userPrhodes.lastName = "Rhodes";
			  userPrhodes.email = "motley.crue.fan@gmail.com";
			  userPrhodes.userId = "prhodes";
			  userPrhodes.password = "secret";
			  userPrhodes.bio = "bio";
			  
			  Profile profilePrhodes = new Profile();
			  // profile.userUuid = "abc123";
			  profilePrhodes.setOwner( userPrhodes );
			  userPrhodes.profile = profilePrhodes;
			  
			  userPrhodes.addToRoles( userRole );
			  userPrhodes.addToRoles( adminRole );
			  
			  
			  userService.createUser( userPrhodes );
			 
		  }

		  
		  User userSarah = userService.findUserByUserId( "sarah" );
  
		   if( userSarah != null )
		   {
				println "Found existing sarah user!";
  
		   }
		   else
			{
				println "Could not find sarah";
				println "Creating new sarah user";
				userSarah = new User();
				userSarah.uuid = "abc124";
				userSarah.displayName = "Sarah Kahn";
				userSarah.firstName = "Sarah";
				userSarah.lastName = "Kahn";
				userSarah.email = "snkahn@gmail.com";
				userSarah.userId = "sarah";
				userSarah.password = "secret";
				userSarah.bio = "bio";
				
				Profile profileSarah = new Profile();
				// profile.userUuid = "abc123";
				profileSarah.setOwner( userSarah );
				userSarah.profile = profileSarah;

				userSarah.addToRoles( userRole );
				userSarah.addToRoles( adminRole );
  
								
				userService.createUser( userSarah );
			   
			}
		  
		  
			User userEric = userService.findUserByUserId( "eric" );
			
			if( userEric != null )
			{
				  println "Found existing eric user!";
		
			}
			else
			{
				  println "Could not find eric";
				  println "Creating new eric user";
				  userEric = new User();
				  userEric.uuid = "abc125";
				  userEric.displayName = "Eric Stone";
				  userEric.firstName = "Eric";
				  userEric.lastName = "Stone";
				  userEric.email = "emstone@gmail.com";
				  userEric.userId = "eric";
				  userEric.password = "secret";
				  userEric.bio = "bio";
				  
				  Profile profileEric = new Profile();
				  // profile.userUuid = "abc123";
				  profileEric.setOwner( userEric );
				  userEric.profile = profileEric;
				  
				  userEric.addToRoles( userRole );
				  userEric.addToRoles( adminRole );
	
				  userService.createUser( userEric );
				 
			}
		  		  
		  for( int i = 0; i < 20; i++ )
		  {
			  if( userService.findUserByUserId( "testuser${i}" ) == null )
			  {
				  println "Fresh Database, creating TESTUSER ${i} user";
				  User testUser = new User(
								  userId: "testuser${i}",
								firstName: "Test",
								lastName: "User${i}",
								email: "testuser${i}@example.com",
								bio:"stuff",
								displayName: "Test User${i}" );
				  
					testUser.password = "secret";
					Profile profile = new Profile();
					// profile.userUuid = testUser.uuid;
					profile.setOwner( testUser );
					testUser.profile = profile;
							
					testUser.addToRoles( userRole );
					
					println "about to create user: ${testUser.toString()}";
					userService.createUser( testUser );
			  }
			  else
			  {
				  println "Existing TESTUSER ${i} user, skipping...";
			  }
		  }
		  
	 }
} 