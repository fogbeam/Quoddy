package com.fogbeam.poe;

class UserService {

	public User findUserByUserIdAndPassword( String userId, String password )
	{
		def user = User.findByUserIdAndPassword( userId, password );
		return user;
	}

	public User findUserByUserId( String userId )
	{
		def user = User.findByUserId( userId );
		return user;
	}	
	
	public void updateUser( User user )
	{
		println "about to update user...";
		if( !user.save() )
		{
			println( "Updating user: ${user.userId} FAILED");
			user.errors.allErrors.each { println it };
		}
	}
	
	public List<User> findAllUsers() 
	{
		def allUsers = User.findAll();
		return allUsers;
	}
}
