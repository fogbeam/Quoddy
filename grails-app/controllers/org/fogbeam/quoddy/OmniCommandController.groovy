package org.fogbeam.quoddy

class OmniCommandController
{
	def submitCommand = {
	
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
