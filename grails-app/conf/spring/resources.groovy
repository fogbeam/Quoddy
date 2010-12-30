import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH;

// Place your Spring DSL code here
beans = {
 
	contextSource(org.springframework.ldap.core.support.LdapContextSource){
		url="ldap://localhost:10389"
		base=""
		userDn="uid=admin,ou=system"
		password="secret"
	}   

	ldapTemplate(org.springframework.ldap.core.LdapTemplate, ref("contextSource"))

	switch( CH.config.friends.backingStore )
	{
		case "ldap":
			friendService(org.fogbeam.quoddy.LdapFriendService) {
				ldapTemplate = ref("ldapTemplate")
			}
			break;
			
		case "localdb":
			friendService(org.fogbeam.quoddy.LocalFriendService)
			break;
		
		default:
		
			// ???
			break;
	}
	
	switch( CH.config.groups.backingStore )
	{
		case "ldap":
			groupService(org.fogbeam.quoddy.LdapGroupService){
				ldapTemplate = ref("ldapTemplate")
			}
			break;
			
		case "localdb":
			groupService(org.fogbeam.quoddy.LocalGroupService)
			break;
		
		default:
		
			// ???
			break;
	}

	// define userService and toggle the accountService we pass in, based on the
	// value of 'created.accounts.backingStore'	
	switch( CH.config.created.accounts.backingStore )
	{
		case "ldap":
			accountService(org.fogbeam.quoddy.LdapPersonService){
				ldapTemplate = ref("ldapTemplate")
			}
			break;
			
		case "localdb":
			accountService(org.fogbeam.quoddy.LocalAccountService)
			break;
			
		default:
		
			// ???
			break;
	}
	
	userService(org.fogbeam.quoddy.UserService)
	{
		accountService = ref("accountService" )
		friendService = ref( "friendService" )
		groupService = ref( "groupService" )
	}
	
	
}