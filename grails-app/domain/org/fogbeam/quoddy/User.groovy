package org.fogbeam.quoddy;

import org.fogbeam.quoddy.profile.Profile 

class User {

	public User()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
    static constraints = {
        userId( size:3..20, unique:true )
        homepage( url:true, nullable:true )
        validator: {passwd, user -> 
                return passwd != user.userId 
            }
        profile( nullable: true )
        currentStatus(nullable:true)
		email(nullable:true)
		dateCreated()
    }

    String uuid;
    String userId;
    Date dateCreated;
    Profile profile;
    StatusUpdate currentStatus;	
	
	/* stuff objects of this class "carry around" but aren't persisted as part of the object. 
	 * This stuff is pulled in from an external source, like, say, LDAP. */
	String password;
	String homepage;
	String firstName;
	String lastName;
	String displayName;
	// String fullName;
	String bio;
	String email;
	static transients = [ "password", "homepage", "displayName", "bio",  ]
	
	
    static mapping = {
    	table 'uzer'
		currentStatus lazy:false; // eagerly fetch the currentStatus
    }
	
    // static hasMany = [savedEntries : Entry, hiddenEntries: Entry];
    static hasMany = [oldStatusUpdates:StatusUpdate]
    
    // static mappedBy = [savedEntries : "savers", hiddenEntries:"hiders" ];

    public void setUuid( String uuid ){
    	
    	// never overwrite existing uuid value with NULL
    	if( uuid != null )
    	{
    		this.uuid = uuid;
    	}
    }

	public String getFullName()
	{
		return firstName + " " + lastName;	
	}	

	public void setFullName( String fullName )
	{}	
}
