import org.fogbeam.quoddy.User 
import org.fogbeam.quoddy.profile.Profile;
import grails.util.Environment;

class BootStrap {

	def ldapTemplate;
	def userService;
	
	def init = { servletContext ->
     
		
		
		 switch( Environment.current )
	     {
	         case Environment.DEVELOPMENT:
	             createSomeUsers();
	             break;
	         case Environment.PRODUCTION:
	             println "No special configuration required";
				 createSomeUsers();
				 break;
	     }     
     
	     
	     
	     // getClass().classLoader.rootLoader.URLs.each { println it };
	     
     }
     
     def destroy = {
    		 
    	// nothing, yet...
    	
     }

     void createSomeUsers()
     {
	 	println "Creating some users!";
     
		 boolean prhodesFound = false;
 
		 User user = userService.findUserByUserId( "prhodes" );

		 if( user != null )
		 {
			  println "Found existing prhodes user!";

		 }
		 else
	 	 {	
			  println "Could not find prhodes";
			  println "Creating new prhodes user";
			  User prhodes = new User();
			  prhodes.uuid = "abc123";
			  prhodes.displayName = "Phillip Rhodes";
			  prhodes.firstName = "Phillip";
			  prhodes.lastName = "Rhodes";
			  prhodes.email = "motley.crue.fan@gmail.com";
			  prhodes.userId = "prhodes";
			  prhodes.password = "secret";
			  prhodes.bio = "bio";
			  
			  Profile profile = new Profile();
			  profile.userUuid = "abc123";
			  prhodes.profile = profile;
			  
			  userService.createUser( prhodes );
			 
			  println "bound user prhodes into LDAP"; 
		  }
		  
		  for( int i = 0; i < 20; i++ )
		  {
			  if( userService.findUserByUserId( "testuser${i}" ) == null )
			  {
				  println "Fresh Database, creating TESTUSER ${i} user";
				  def testUser = new User(
								  userId: "testuser${i}",
								password: "secret",
								firstName: "Test",
								lastName: "User${i}",
								email: "testuser${i}@example.com",
								bio:"stuff",
								displayName: "Test User${i}" );
				  
					Profile profile = new Profile();
					profile.userUuid = testUser.uuid;
					testUser.profile = profile;
							
					userService.createUser( testUser );
			  }
			  else
			  {
				  println "Existing TESTUSER ${i} user, skipping...";
			  }
		  }
		  
	 }
} 