grails.plugin.springsecurity.cas.key = 'cas01.example.org'
grails.plugin.springsecurity.cas.loginUri = '/login'
grails.plugin.springsecurity.cas.serviceUrl = 'http://localhost:8080/login/cas'
grails.plugin.springsecurity.cas.serverUrlPrefix = 'https://sso.fogbeam.com/cas'


grails.plugin.springsecurity.auth.loginFormUrl="/localLogin/index"
grails.plugin.springsecurity.failureHandler.defaultFailureUrl="/localLogin/index?login_error=1"

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.password.hash.iterations = 10
grails.plugin.springsecurity.userLookup.userDomainClassName = 'org.fogbeam.quoddy.User'
grails.plugin.springsecurity.userLookup.usernamePropertyName = "userId"
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'org.fogbeam.quoddy.UserAccountRoleMapping'
grails.plugin.springsecurity.authority.className = 'org.fogbeam.quoddy.AccountRole'
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
    [pattern: '/api/**',         filters: 'none'],
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

grails.plugin.springsecurity.logout.handlerNames =[ 'rememberMeServices', 'securityContextLogoutHandler', 'logoutEventListener']

spring.autoconfigure.exclude = "org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration"

server.session.timeout = 600

oauth.server.introspect.url="https://localhost:8443/cas/oidc/introspect"
oauth.client.id = "test_client"

