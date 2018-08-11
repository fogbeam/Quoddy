package org.fogbeam.quoddy

import grails.plugin.springsecurity.annotation.Secured

class OmniCommandController
{
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def submitCommand() 
    {
		String queryString = params.queryString;
		
		if( queryString.startsWith("@"))
		{
			Scanner s = new Scanner( queryString);
			String command = s.next();
			
			switch( command )
			{
				case "@finger":
				
					String userId = s.next();
					User user = User.findByUserId( userId );
					
				
					render( view:"finger", model:[user:user]);
					break;
				default:
					flash.message = "Invalid command";
					break;
			}
			
		}	
	}
}
