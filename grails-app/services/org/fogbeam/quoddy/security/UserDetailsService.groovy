package org.fogbeam.quoddy.security

import org.fogbeam.quoddy.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import grails.transaction.Transactional

@Transactional
class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{
    def userService;
    
    @Override
    public UserDetails loadUserByUsername( String username )
            throws UsernameNotFoundException
    {
        println "username: " + username;
        
        
        User ourUser = null;
        User.withNewSession
        {
            ourUser = userService.findUserByUserId( username );
        }
        
        println "returning user: " + ourUser;
        
        return ourUser;
    }
}
