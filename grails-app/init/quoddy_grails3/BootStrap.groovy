package quoddy_grails3

import org.fogbeam.quoddy.AccountRole
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.UserAccountRoleMapping
import org.fogbeam.quoddy.profile.Profile
import org.fogbeam.quoddy.stream.EventType
import org.fogbeam.quoddy.stream.ShareTarget
import org.fogbeam.quoddy.stream.constants.EventTypes

import grails.util.Environment


class BootStrap 
{
	def ldapTemplate;
	def userService;
	def siteConfigService;
	def searchService;
	def environment;
	def grailsApplication;
	
    def init = 
	{ servletContext ->
    
		switch( Environment.current  )
		{
			case Environment.DEVELOPMENT:
				 

			   println "quoddy.hostName: ${grailsApplication.config.quoddy.hostName}"
			   // println "fooHost: ${grailsApplication.config.fooHost}"
			   
				createRoles();
				createSomeUsers();
				createSystemUser();
				createShareTargets();
				createEventTypes();
				break;
			case Environment.PRODUCTION:
				println "No special configuration required";
				createRoles();
				// createSomeUsers();
				createSystemUser();
				createShareTargets();
				createEventTypes();
				break;
		}
	
		
		searchService.initializeGeneralIndex();
		searchService.initializePersonIndex();

		
	}
    
	def destroy = 
	{
	}
	
	void createEventTypes()
	{
		
		// create these in a loop off the EventTypes enum
		
		for( EventTypes eventTypeEnum : EventTypes.values() )
		{
			EventType et = EventType.findByName( eventTypeEnum.name );
		   if( et == null )
		   {
			   println "Creating DB entry for EventType: ${eventTypeEnum.name}";
			   et = new EventType( name: eventTypeEnum.name, scope: eventTypeEnum.scope );
			   et.save();
		   }
		   else
		   {
			   println "EventType: ${eventTypeEnum.name} already exists in DB, skipping";
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
		
		AccountRole userRole = null;
		AccountRole.withTransaction
		{
			
			userRole = AccountRole.findByAuthority( "ROLE_USER" );
			if( !userRole )
			{
				userRole = new AccountRole(authority: 'ROLE_USER');
			
				if( !userRole.save(flush:true) )
				{
					userRole.errors.allErrors.each { println it };
				}
				else
				{
					println "userRole created!";
				}
			}
			else
			{
				println "userRole already exists!";
			}
		}
		
		
		AccountRole adminRole = null;
		AccountRole.withTransaction
		{
			adminRole = AccountRole.findByAuthority( "ROLE_ADMIN" );
			if( !adminRole )
			{
				adminRole = new AccountRole( authority: 'ROLE_ADMIN');
				if( !adminRole.save(flush:true) )
				{
					adminRole.errors.allErrors.each { println it };
				}
				else
				{
					println "adminRole created!";
				}
			}
			else
			{
				println "adminRole already exists!";
			}
		}
	}

	
	void createSystemUser()
	{
		AccountRole userRole = userService.findAccountRoleByAuthority( "ROLE_USER" );

		if( userRole == null )
		{
				println "did not locate user role!";
		}

		AccountRole adminRole = userService.findAccountRoleByAuthority( "ROLE_ADMIN" );
		if( adminRole == null )
		{
				println "did not locate admin role!";
		}

		User ghostUser = userService.findUserByUserId( "SYS_ghost_user" );
	   
		if( ghostUser != null )
		{
			println "Found existing SYS_ghost_user!";
 		}
		else
		{
			println "Could not find SYS_ghost_user";
			println "Creating new SYS_ghost_user user";
			ghostUser = new User();
			ghostUser.uuid = "abc126";
			ghostUser.displayName = "Ghost User";
			ghostUser.firstName = "System";
			ghostUser.lastName = "Ghost User";
			ghostUser.email = "SYS_ghost_user@example.com";
			ghostUser.userId = "SYS_ghost_user";
			ghostUser.password = "secret";
			ghostUser.bio = "bio";
			 
			Profile profileGhost = new Profile();
			 
			profileGhost.setOwner( ghostUser );
			ghostUser.profile = profileGhost;
			 
			ghostUser = userService.createUser( ghostUser );
			
			UserAccountRoleMapping adminUser_UserRole = null;
			UserAccountRoleMapping.withSession
			{
				adminUser_UserRole = UserAccountRoleMapping.findByUserAndRole( ghostUser, userRole );
				if( !adminUser_UserRole )
				{
					adminUser_UserRole = new UserAccountRoleMapping( ghostUser, userRole );
					if( ! adminUser_UserRole.save( flush: true ) )
					{
						adminUser_UserRole.errors.allErrors.each { println it };
                        throw new RuntimeException( "Failed to create adminUser_UserRole" );
					}
					else
					{
						println "adminUser_UserRole created!";
					}
				}
				else
				{
					 println "adminUser_UserRole already exists!";
				}
			}

			UserAccountRoleMapping adminUser_AdminRole = null;
			UserAccountRoleMapping.withSession
			{
				adminUser_AdminRole = UserAccountRoleMapping.findByUserAndRole( ghostUser, adminRole );
				if( !adminUser_AdminRole )
				{
					adminUser_AdminRole = new UserAccountRoleMapping( ghostUser, adminRole );
					if( ! adminUser_AdminRole.save( flush: true ) )
					{
						adminUser_AdminRole.errors.allErrors.each { println it };
                        throw new RuntimeException( "Failed to create adminUser_AdminRole" );
					}
					else
					{
						println "adminUser_AdminRole created!";
					}
				}
				else
				{
					println "adminUser_AdminRole already exists!";
				}
			}
	   	}
	}

	void createSomeUsers()
	{
		println "Creating some users!";
	
		AccountRole userRole = userService.findAccountRoleByAuthority( "ROLE_USER" );

		if( userRole == null )
		{
				println "did not locate user role!";
		}

		AccountRole adminRole = userService.findAccountRoleByAuthority( "ROLE_ADMIN" );
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
			 
			userPrhodes = userService.createUser( userPrhodes );
			
			// userPrhodes.addToRoles( userRole );
			UserAccountRoleMapping prhodesUser_UserRole = null;
			UserAccountRoleMapping.withSession
			{
				prhodesUser_UserRole = UserAccountRoleMapping.findByUserAndRole( userPrhodes, userRole );
				if( !prhodesUser_UserRole )
				{
					prhodesUser_UserRole = new UserAccountRoleMapping( userPrhodes, userRole );
					if( !prhodesUser_UserRole.save( flush: true ) )
					{
						prhodesUser_UserRole.errors.allErrors.each { println it };
						throw new RuntimeException( "Failed to create prhodesUser_UserRole" );
					}
					else
					{
						println "prhodesUser_UserRole created!";
					}
				}
				else
				{
					println "prhodesUser_UserRole already exists!";
				}
			}
 
			 
			// userPrhodes.addToRoles( adminRole );
			UserAccountRoleMapping prhodesUser_AdminRole = null;
			UserAccountRoleMapping.withSession
			{
				 prhodesUser_AdminRole = UserAccountRoleMapping.findByUserAndRole( userPrhodes, adminRole );
				 if( !prhodesUser_AdminRole )
				 {
					 prhodesUser_AdminRole = new UserAccountRoleMapping( userPrhodes, adminRole );
					 if( ! prhodesUser_AdminRole.save( flush: true ) )
					 {
						prhodesUser_AdminRole.errors.allErrors.each { println it };
					 	throw new RuntimeException( "Failed to create prhodesUser_AdminRole" );
					 }
					 else
					 {
						 println "prhodesUser_AdminRole created!";
					 }
				 }
				 else
				 {
					 println "prhodesUser_AdminRole already exists!";
				 }
			 }
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

			 userSarah = userService.createUser( userSarah );
			 			   
			 UserAccountRoleMapping sarahUser_UserRole = null;
			 UserAccountRoleMapping.withSession
			 {
				 sarahUser_UserRole = UserAccountRoleMapping.findByUserAndRole( userSarah, userRole );
				 if( !sarahUser_UserRole )
				 {
					 sarahUser_UserRole = new UserAccountRoleMapping( userSarah, userRole );
					 if( ! sarahUser_UserRole.save( flush: true ) )
					 {
						 sarahUser_UserRole.errors.allErrors.each { println it };
						 throw new RuntimeException( "Failed to create sarahUser_UserRole" );
					 }
					 else
					 {
						 println "sarahUser_UserRole created!";
					 }
				 }
				 else
				 {
					 println "sarahUser_UserRole already exists!";
				 }
			 }
   
			   
			 UserAccountRoleMapping sarahUser_AdminRole = null;
			 UserAccountRoleMapping.withSession
			 {
				 sarahUser_AdminRole = UserAccountRoleMapping.findByUserAndRole( userSarah, adminRole );
				 if( !sarahUser_AdminRole )
				 {
					 sarahUser_AdminRole = new UserAccountRoleMapping( userSarah, adminRole );
					 if( ! sarahUser_AdminRole.save( flush: true ) )
					 {
						 sarahUser_AdminRole.errors.allErrors.each { println it };
					 	 throw new RuntimeException( "Failed to create sarahUser_AdminRole" );
					 }
					 else
					 {
						 println "sarahUser_AdminRole created!";
					 }
				 }
				 else
				 {
					 println "sarahUser_AdminRole already exists!";
				 }
			 }
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
				  testUser.uuid = "test_user_${i}";
				  Profile profile = new Profile();
				  // profile.userUuid = testUser.uuid;
				  profile.setOwner( testUser );
				  testUser.profile = profile;
				
				  println "about to create user: ${testUser.toString()}";
				  testUser = userService.createUser( testUser );

				  		   
				  UserAccountRoleMapping testUser_UserRole = null;
				  UserAccountRoleMapping.withSession
				  {
					  testUser_UserRole = UserAccountRoleMapping.findByUserAndRole( testUser, userRole );
					  if( !testUser_UserRole )
					  {
						  testUser_UserRole = new UserAccountRoleMapping( testUser, userRole );
						  if( ! testUser_UserRole.save( flush: true ) )
						  {
							  testUser_UserRole.errors.allErrors.each { println it };
                              throw new RuntimeException( "Failed to create testUser_UserRole" );
						  }
						  else
						  {
							  println "testUser_UserRole created!";
						  }
					  }
					  else
					  {
						  println "testUser_UserRole already exists!";
					  }
				  }				   
			 }
			 else
			 {
				 println "Existing TESTUSER ${i} user, skipping...";
			 }
		 }
	}
}
