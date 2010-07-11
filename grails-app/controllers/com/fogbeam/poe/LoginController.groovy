package com.fogbeam.poe;

class LoginController {

	def userService;
	
    def index = { }
    
    def login = {
    	
    	def userId = params.username;
    	def password = params.password;
    	
    	def user = userService.findUserByUserIdAndPassword( userId, password );
    	if( user )
    	{
    		session.user = user;
    		redirect( controller:'home', action:'index')
    	}
    	else
    	{
    		flash.message = "Login Failed";
    		redirect( action:'index');
    	}
    }
    
    def logout = {
    	session.user = null;
    	redirect( uri:'/');
    }
}
