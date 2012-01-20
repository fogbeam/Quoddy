package org.fogbeam.quoddy

class UserStreamController
{
	def userService;
	def userStreamService;
	def userListService;
	def userGroupService;
	
	def index =
	{
		User user = null;
		
		def systemDefinedStreams = new ArrayList<UserStream>();
		def userDefinedStreams = new ArrayList<UserStream>();
		
		if( session.user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
		
		
			def tempSysStreams = userStreamService.getSystemDefinedStreamsForUser( user );
			systemDefinedStreams.addAll( tempSysStreams );
			def tempUserStreams = userStreamService.getUserDefinedStreamsForUser( user );
			userDefinedStreams.addAll( tempUserStreams );
			
			[user:user, sysDefinedStreams:systemDefinedStreams,
			  userDefinedStreams:userDefinedStreams];
	  
			
		}
		else
		{
			// TODO: not logged in, deal with this...	
		}
	}
	
	def create = 
	{
		[];	
	}
	
	def save = 
	{
		
		// TODO: implement this...
		println "save using params: ${params}"
		if( session.user != null )
		{
			def user = userService.findUserByUserId( session.user.userId );
			UserStream streamToCreate = new UserStream();
		
			streamToCreate.name = params.streamName;
			streamToCreate.owner = user;
			streamToCreate.definedBy = UserStream.DEFINED_USER;
			
			
			streamToCreate.save();
		
			redirect(controller:"userStream", action:"index");
		}
		else
		{
			// not logged in, deal with this...	
		}
	}
	
	def edit =
	{
		def streamId = params.id;
		println "Editing UserStream with id: ${streamId}";
		UserStream streamToEdit = null;
		
		streamToEdit = UserStream.findById( streamId );
		
		
		[streamToEdit:streamToEdit];	
	}
	
	def update = 
	{
		
		// TODO: implement this...
		println "update using params: ${params}"
		def streamId = params.streamId;
		UserStream streamToEdit = null;
		
		streamToEdit = UserStream.findById( streamId );
		
		streamToEdit.name = params.streamName;
		streamToEdit.save();
		
		redirect(controller:"userStream", action:"index");
	}
}
