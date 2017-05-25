// import org.springframework.ldap.core.support.LdapContextSource;
// import org.springframework.ldap.core.LdapTemplate;

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH;

// Place your Spring DSL code here
beans = {
 
	contextSource(org.springframework.ldap.core.support.LdapContextSource){
		url="ldap://localhost:10389"
		base=""
		userDn="uid=admin,ou=system"
		password="secret"
	}   
	
	// the LDAP server we use if we're using LDAP as the backing store for
	// accounts
	ldapTemplate(org.springframework.ldap.core.LdapTemplate, ref("contextSource"))
	
	// for looking up LDAP users in "import" mode.  NOTE: this whole deal needs
	// a lot of reworking & rethinking to deal with all the different potential
	// configurations... is LDAP a "read only" service being used only as an authSource?
	// Or do we create accounts in LDAP?  Is there one LDAP server or two (or more)?
	// etc...  for now we're assuming the simple case just to get stuff up and running, but
	// this could get complicated.
	ldapPersonService(org.fogbeam.quoddy.LdapPersonService){
		ldapTemplate = ref("ldapTemplate")
	}
	
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
		userListService = ref( "userListService");
		userGroupService = ref( "userGroupService" );
	}
	
	jmsConnectionFactory(org.springframework.jndi.JndiObjectFactoryBean) {
		jndiName="ConnectionFactory"
		jndiEnvironment=["java.naming.factory.initial":"org.jnp.interfaces.NamingContextFactory",
"java.naming.provider.url":"ip-10-0-0-61.ec2.internal:1099"]
		}
	
	jenaTemplate( org.fogbeam.quoddy.spring.factorybean.JenaTemplateFactoryBean)
	{
		tdbDirectory = System.getProperty("quoddy.home") + "/jenastore/triples";
	}
	
	// select the EmailService implementation based on parameter
	switch( CH.config.emailservice.backend )
	{
		case 'direct_smtp':
		        println "direct_smtp"
			emailService(org.fogbeam.quoddy.email.DirectSmtpEmailService);
			break;
		case 'gmail_api':
		        println "gmail_api"
			emailService(org.fogbeam.quoddy.email.GMailApiEmailService)
			break;
		case 'amazon_ses':
		        println "amazon_ses"
			emailService(org.fogbeam.quoddy.email.AmazonSesEmailService)
			break;
		default:
                        println "default (direct_smtp)"
			emailService(org.fogbeam.quoddy.email.DirectSmtpEmailService);
			break;
	}
	
	
}