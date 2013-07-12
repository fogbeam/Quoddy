package org.fogbeam.security

import org.apache.shiro.authc.AccountException
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.authc.SimpleAccount
import org.apache.shiro.authc.UnknownAccountException
import org.fogbeam.quoddy.AccountRole
import org.fogbeam.quoddy.User

class QuoddyLoginRealm 
{
    static authTokenClass = org.apache.shiro.authc.UsernamePasswordToken
	
	def loginService;
	def userService;
	
    def credentialMatcher
    def shiroPermissionResolver

    def authenticate(authToken) {
		
        log.info "Attempting to authenticate ${authToken.username} in DB realm..."
        def username = authToken.username

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.")
        }

        // Get the user with the given username. If the user is not
        // found, then they don't have an account and we throw an
        // exception.
        
		User user = userService.findUserByUserId( username );
		//  ShiroUser.findByUsername(username)
        if (!user) {
            throw new UnknownAccountException("No account found for user [${username}]")
        }

        log.info "Found user '${user.userId}' in DB"

        // Now check the user's password against the hashed value stored
        // in the database.
        user = loginService.doUserLogin( username, authToken.password as String );
        if(!user ) 
		{
            log.info "Invalid password (DB realm)"
            throw new IncorrectCredentialsException("Invalid password for user '${username}'")
        }
		
		def account = new SimpleAccount(user, "", "QuoddyLoginRealm");
		
        return account;
    }

    def hasRole(principal, roleName) 
	{
		User user = userService.findUserByUserId( principal.userId );
		
		AccountRole role = user.roles.find { it.name.equals( roleName ) };
		
		if( role )
		{
			return true;
		}
		else 
		{
			return false;
		}
    }

    def hasAllRoles(principal, roles) 
	{
		
		User user = userService.findUserByUserId( principal.userId );

        return user.roles.size() == roles.size()
		
    }

    def isPermitted(principal, requiredPermission) 
	{
        
		// Does the user have the given permission directly associated
        // with himself?
        //
        // First find all the permissions that the user has that match
        // the required permission's type and project code.
        
		User user = userService.findUserByUserId( principal.userId );
        Set<String>permissions = user.permissions;

        // Try each of the permissions found and see whether any of
        // them confer the required permission.
        
		def retval = permissions?.find { permString ->
            // Create a real permission instance from the database
            // permission.
            def perm = shiroPermissionResolver.resolvePermission(permString)

            // Now check whether this permission implies the required
            // one.
            if (perm.implies(requiredPermission)) 
			{
                // User has the permission!
                return true
            }
            else 
			{
                return false
            }
        }

        if (retval != null) 
		{
            // Found a matching permission!
            return true
        }

        // If not, does he gain it through a role?
        //
        // Get the permissions from the roles that the user does have.
        List<String> results = new ArrayList<String>();
		Set<AccountRole> roles = user.roles;
		for( AccountRole role : roles )
		{
			results.addAll( role.permissions );
		}

        // There may be some duplicate entries in the results, but
        // at this stage it is not worth trying to remove them. Now,
        // create a real permission from each result and check it
        // against the required one.
        retval = results.find { permString ->
            // Create a real permission instance from the database
            // permission.
            def perm = shiroPermissionResolver.resolvePermission(permString)

            // Now check whether this permission implies the required
            // one.
            if (perm.implies(requiredPermission)) {
                // User has the permission!
                return true
            }
            else {
				return false
            }
        }

        if (retval != null) 
		{
            // Found a matching permission!
            return true
        }
        else 
		{
            return false
        }
		
    }
}