package org.fogbeam.quoddy

import org.springframework.security.core.GrantedAuthority

import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@GrailsCompileStatic
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class AccountRole implements GrantedAuthority
{
	private static final long serialVersionUID = 1

	String authority

	static constraints = 
	{
		authority blank: false, unique: true
	}

	static mapping = 
	{
		cache true
	}
}
