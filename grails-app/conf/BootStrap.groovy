import grails.util.Environment

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriter.MaxFieldLength
import org.apache.lucene.store.Directory
import org.apache.lucene.store.NIOFSDirectory
import org.apache.lucene.util.Version
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.profile.Profile
import org.fogbeam.quoddy.stream.EventType;
import org.fogbeam.quoddy.stream.ShareTarget;

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
	             createSomeUsers();
				 createShareTargets();
				 createEventTypes();
	             break;
	         case Environment.PRODUCTION:
	             println "No special configuration required";
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
	 	
		EventType calendarFeedItemType = EventType.findByName( "CalendarFeedItem" );
		if( calendarFeedItemType == null )
		{
			calendarFeedItemType = new EventType( name:"CalendarFeedItem" );
			calendarFeedItemType.save();
		}
		
		EventType activityStreamItemType = EventType.findByName( "ActivityStreamItem" );
		if( activityStreamItemType == null )
		{
			activityStreamItemType = new EventType( name:"ActivityStreamItem" );
			activityStreamItemType.save();
		}
		
		EventType businessEventSubscriptionItemType = EventType.findByName( "BusinessEventSubscriptionItem" );
		if( businessEventSubscriptionItemType == null )
		{
			businessEventSubscriptionItemType = new EventType( name:"BusinessEventSubscriptionItem" );
			businessEventSubscriptionItemType.save();
		}

		
	 	// new types, not used yet
		EventType rssFeedItemType = EventType.findByName( "RssFeedItem" );
		if( calendarFeedItemType == null )
		{
			rssFeedItemType = new EventType( name:"RssFeedItem" );
			rssFeedItemType.save();
		}
		
		EventType questionItemType = EventType.findByName( "Question" );
		if( questionItemType == null )
		{
			questionItemType = new EventType( name:"Question" );
			questionItemType.save();
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
	 
     void createSomeUsers()
     {
	 	println "Creating some users!";
     
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