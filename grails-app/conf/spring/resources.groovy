import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.LdapTemplate;

// Place your Spring DSL code here
beans = {
 
	contextSource(org.springframework.ldap.core.support.LdapContextSource){
		url="ldap://localhost:10389"
		base=""
		userDn="uid=admin,ou=system"
		password="secret"
	}   

	ldapTemplate(org.springframework.ldap.core.LdapTemplate, ref("contextSource"))
		
}