package org.fogbeam.quoddy

class UserAccountRoleMapping 
{
	User user;
	AccountRole role;

	public UserAccountRoleMapping( User user, AccountRole role )
	{
		this.user = user;
		this.role = role;
	}
}