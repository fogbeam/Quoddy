package com.fogbeam.poe;

import com.fogbeam.poe.profile.Profile 

class User {

	public User()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}
	
    static constraints = {
        userId( size:3..20, unique:true )
        password( size:6..8 )
        homepage( url:true, nullable:true )
        validator: {passwd, user -> 
                return passwd != user.userId 
            }
        profile( nullable: true )
        currentStatus(nullable:true)
        dateCreated()
    }

    
    String uuid;
    String userId;
    String password;
    String homepage;
    String fullName;
    String bio;
    String email;
    Date dateCreated;
    Profile profile;
    StatusUpdate currentStatus;
    
    static mapping = {
    	table 'uzer'
    }

    // static hasMany = [savedEntries : Entry, hiddenEntries: Entry];
    static hasMany = [friends: User, oldStatusUpdates:StatusUpdate]
    
    // static mappedBy = [savedEntries : "savers", hiddenEntries:"hiders" ];

    public void setUuid( String uuid ){
    	
    	// never overwrite existing uuid value with NULL
    	if( uuid != null )
    	{
    		this.uuid = uuid;
    	}
    }
}
