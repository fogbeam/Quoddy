package org.fogbeam.quoddy

class UserListController
{
	def userService;
	def userStreamService;
	def userListService;
	def userGroupService;
	
	def index =
	{
		User user = null;
		
		def userLists = new ArrayList<UserList>();
		
		if( session.user != null )
		{
			user = userService.findUserByUserId( session.user.userId );
		
		
			def tempUserLists = userListService.getListsForUser( user );
			userLists.addAll( tempUserLists );
			
			[user:user, userLists:userLists ];
	  
			
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
			UserList listToCreate = new UserList();
		
			listToCreate.name = params.listName;
			listToCreate.description = params.listDescription;
			listToCreate.owner = user;
			
			if( !listToCreate.save() )
			{
				println( "Saving UserList FAILED");
				listToCreate.errors.allErrors.each { println it };
			}
		
			redirect(controller:"userList", action:"index");
		}
		else
		{
			// not logged in, deal with this...	
		}
	}
	
	def edit =
	{
		def listId = params.id;
		println "Editing UserList with id: ${listId}";
		UserList listToEdit = null;
		
		listToEdit = UserList.findById( listId );
		
		
		[listToEdit:listToEdit];	
	}
	
	def update = 
	{
		println "update using params: ${params}"
		def listId = params.listId;
		UserList listToEdit = null;
		
		listToEdit = UserList.findById( listId );
		
		listToEdit.name = params.listName;
		listToEdit.description = params.listDescription;
		
		if( !listToEdit.save() )
		{
			println( "Saving UserList FAILED");
			listToEdit.errors.allErrors.each { println it };
		}
		
		
		
		redirect(controller:"userList", action:"index");
	}
}
