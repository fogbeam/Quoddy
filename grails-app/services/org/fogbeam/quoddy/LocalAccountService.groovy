package org.fogbeam.quoddy

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import sun.misc.BASE64Encoder;

class LocalAccountService 
{
	public LocalAccount findAccountByUserId( final String userId )
	{
		LocalAccount account = LocalAccount.findByUsername( userId );
		
		return account;
	}
	
	public void createUser( final User user )
	{
		LocalAccount account = new LocalAccount();
		account.username = user.userId;
		account.password = digestMd5( user.password );
			
		if( !account.save() )
		{
			println( "Saving LocalAccount FAILED");
			account.errors.allErrors.each { println it };
		}


		FriendCollection friendCollection = new FriendCollection( ownerUuid: user.uuid );
		friendCollection.save();
		IFollowCollection iFollowCollection = new IFollowCollection( ownerUuid: user.uuid );
		iFollowCollection.save();
		FriendRequestCollection friendRequestCollection = new FriendRequestCollection( ownerUuid: user.uuid );
		friendRequestCollection.save();
		
	}
	
	public User updateUser( final User user )
	{

		LocalAccount account = LocalAccount.findByUsername( userId );
		account.password = digestMd5( user.password );
		
		if( !account.save() )
		{
			println( "Updating LocalAccount FAILED");
			account.errors.allErrors.each { println it };
		}
	}
	
	private static String digestMd5(final String password)
	{
		String base64;
		try
		{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes());
			base64 = new BASE64Encoder().encode(digest.digest());
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	  
		return "{MD5}" + base64;
	}
	
}
