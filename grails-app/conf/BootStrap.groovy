import grails.util.Environment;
import com.fogbeam.poe.User;

class BootStrap {

     def init = { servletContext ->
     
	     switch( Environment.current )
	     {
	         case Environment.DEVELOPMENT:
	             createSomeUsers();
	             break;
	         case Environment.PRODUCTION:
	             println "No special configuration required";
	             break;
	     }     
     
	     
	     
	     // getClass().classLoader.rootLoader.URLs.each { println it };
	     
	     
     }
     
     def destroy = {
    		 
    	// nothing, yet...
    	
     }

     void createSomeUsers()
     {
         if( !User.findByUserId( "prhodes" ))
         {
             println "Fresh Database, creating PRHODES user";
             def user = new User( userId: "prhodes", password: "secret",
                     fullName: "Phillip Rhodes", email: "prhodes@example.com", bio:"" );
             
             if( !user.save() )
             {
                 println( "Saving PRHODES user failed!");
             }
             
         }
         else
         {
             println "Existing PRHODES user, skipping...";
         }     	 
     
         for( int i = 0; i < 20; i++ )
         {
             if( !User.findByUserId( "testuser${i}" ))
             {
                 println "Fresh Database, creating TESTUSER ${i} user";
                 def user = new User( userId: "testuser${i}", password: "secret",
                         fullName: "Test User ${i}", email: "testuser${i}@example.com", bio:"" );
                 
                 if( !user.save() )
                 {
                     println( "Saving TESTUSER ${i} user failed!");
                     user.errors.allErrors.each { println it };
                     
                 
                 
                 }
                 
             }
             else
             {
                 println "Existing TESTUSER ${i} user, skipping...";
             }         	 
         }
     }


} 