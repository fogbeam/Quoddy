grails.plugin.springsecurity.cas.key = 'cas01.example.org'
grails.plugin.springsecurity.cas.loginUri = '/login'
grails.plugin.springsecurity.cas.serviceUrl = 'http://localhost:8080/login/cas'
grails.plugin.springsecurity.cas.serverUrlPrefix = 'https://sso.fogbeam.com/cas'

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'org.fogbeam.example.springsecurity.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'org.fogbeam.example.springsecurity.UserRole'
grails.plugin.springsecurity.authority.className = 'org.fogbeam.example.springsecurity.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/localLogin/**',  access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

