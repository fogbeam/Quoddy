package org.fogbeam.quoddy;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fogbeam.quoddy.profile.Profile;
import org.fogbeam.quoddy.stream.ActivityStreamItem;
import org.fogbeam.quoddy.stream.StatusUpdate;
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@XmlRootElement
@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
class User implements Serializable, UserDetails
{

	public User()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
		this.disabled = false;
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
		displayName( nullable:true)
		bio( nullable:true)
		dateCreated()
    }
	
	
	public String toString()
	{
		return "id: ${id}, uuid: ${uuid}, userId: ${userId}, password: ${password}, firstName: ${firstName}, lastName: ${lastName}, homepage: ${homepage}, disabled: ${disabled}";
	}
	
    // UserAccountRoleMapping roleMapping;
	
	@XmlElement
    String uuid;
	@XmlElement
	String userId;
	@XmlElement
    Date dateCreated;
    @XmlElement
	boolean disabled;
	Profile profile;
    StatusUpdate currentStatus;	
	
	/* stuff objects of this class "carry around" but aren't persisted as part of the object. 
	 * This stuff is pulled in from an external source, like, say, LDAP. */
	String password = "notused"; // normal users login using CAS and don't have a password stored locally
	@XmlElement
	String homepage;
	@XmlElement
	String firstName;
	@XmlElement
	String lastName;
	@XmlElement
	String displayName;
	// String fullName;  // see below
	String bio;
	@XmlElement
	String email;
	@XmlElement
    boolean accountExpired;
    @XmlElement
    boolean accountLocked;
    @XmlElement
    boolean passwordExpired;
    
	static transients = [ "templateName", "fullName" ]
	
    static mapping = {
    	table 'uzer'
		currentStatus lazy:false, cascade:'delete'; // eagerly fetch the currentStatus
    	roles lazy: false;		  // eagerly fetch roles
		permissions lazy:false, cascade:'delete';   // eagerly fetch permissions
		streams cascade: 'delete';
	}
	
	static fetchMode = [roles: 'eager', permissions:'eager'];
	
    static hasMany = [oldStatusUpdates:StatusUpdate, roles: AccountRole, permissions: String, streams:UserStreamDefinition];
	static mappedBy = [oldStatusUpdates:'creator']

    
    Set<GrantedAuthority> getAuthorities()
    {
        Set<GrantedAuthority> authorities = null;
        UserAccountRoleMapping.withNewSession
        {
            authorities = (UserAccountRoleMapping.findAllByUser(this) as List<UserAccountRoleMapping>)*.role as Set<GrantedAuthority>
        }
        
        return authorities;
    }

    
    boolean getEnabled()
    {
        return !disabled;
    }
       
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
	
	public ActivityStreamItem getMostRecentUpdate()
	{
		List<ActivityStreamItem> currentUpdates = ActivityStreamItem.executeQuery("select asi from ActivityStreamItem as asi where asi.streamObject = :streamObject", [streamObject:currentStatus]);
		ActivityStreamItem mostRecent = null;
		if( currentUpdates != null )
		{
			mostRecent = currentUpdates.get(0);
		}
		
		
		return mostRecent;
	}
	
	public String getTemplateName()
	{
		return "/renderUser";
	}


    @Override
    public String getUsername()
    {
        return userId;
    }


    @Override
    public boolean isAccountNonExpired()
    {
        return !accountExpired;
    }


    @Override
    public boolean isAccountNonLocked()
    {
        return !accountLocked;
    }


    @Override
    public boolean isCredentialsNonExpired()
    {
        return !passwordExpired;
    }


    @Override
    public boolean isEnabled()
    {
        return !disabled;
    }
	
}
