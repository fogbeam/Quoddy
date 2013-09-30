package org.fogbeam.quoddy;

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.grails.ConfigUtils
import org.apache.shiro.web.util.WebUtils

class LoginController 
{

	
	// def loginService;
	
     def index = { }
    
	 /* NOTE: 2013-07-11 - switching to using Apache Shiro for Authentication / Authorization, so this
	  * all changes now.  Instead of calling our loginService directly, we hand off processing to
	  * Shiro, which will invoke a Realm instance.  
	  */
    def login = 
	{
    	
		def authToken = new UsernamePasswordToken(params.username, params.password as String)
		
		// Support for "remember me"
		if (params.rememberMe) 
		{
			authToken.rememberMe = true
		}
		
		// If a controller redirected to this page, redirect back
		// to it. Otherwise redirect to the root URI.
		def targetUri = params.targetUri ?: "/"
		
		// Handle requests saved by Shiro filters.
		def savedRequest = WebUtils.getSavedRequest(request)
		if (savedRequest) 
		{
			targetUri = savedRequest.requestURI - request.contextPath
			if (savedRequest.queryString) targetUri = targetUri + '?' + savedRequest.queryString
		}
				
		try
		{
			// Perform the actual login. An AuthenticationException
			// will be thrown if the username is unrecognised or the
			// password is incorrect.
			SecurityUtils.subject.login(authToken)

			
			session.user = SecurityUtils.subject.principal;
			
			log.info "Redirecting to '${targetUri}'."
			redirect(uri: targetUri)
		}
		catch (AuthenticationException ex)
		{
			// Authentication failed, so display the appropriate message
			// on the login page.
			log.info "Authentication failure for user '${params.username}'."
			flash.message = message(code: "login.failed")

			// Keep the username and "remember me" setting so that the
			// user doesn't have to enter them again.
			def m = [ username: params.username ]
			if (params.rememberMe) {
				m["rememberMe"] = true
			}

			// Remember the target URI too.
			if (params.targetUri) {
				m["targetUri"] = params.targetUri
			}

			// Now redirect back to the login page.
		   redirect(controller: "login", action: "index", params: m)
		}
				
    }
    
    def logout = 
	{
    	session.user = null;
		
		// Log the user out of the application.
		def principal = SecurityUtils.subject?.principal
		SecurityUtils.subject?.logout()
		// For now, redirect back to the home page.
		if (ConfigUtils.getCasEnable() && ConfigUtils.isFromCas(principal)) 
		{
			redirect(uri:ConfigUtils.getLogoutUrl())
		}
		else 
		{
			redirect(uri: "/")
		}
		
		ConfigUtils.removePrincipal(principal)
		
    }
}
