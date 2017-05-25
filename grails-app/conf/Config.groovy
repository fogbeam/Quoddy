import grails.util.Environment;

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

String quoddyHome = System.getProperty( "quoddy.home");
if( quoddyHome == null || quoddyHome.isEmpty() )
{
	quoddyHome = System.getenv( "QUODDY_HOME" );
}

if( quoddyHome == null || quoddyHome.isEmpty() )
{
	quoddyHome = "/opt/fogcutter/quoddy";
}


if(!grails.config.locations || !(grails.config.locations instanceof List)) 
{
	grails.config.locations = []
}

switch( Environment.current  )
{
	case Environment.DEVELOPMENT:
		
		String configLocation = quoddyHome + "/quoddy-dev.properties";
		println "####################\n######################\nadding configLocation: ${configLocation}\n###################";
		grails.config.locations << "file:" + configLocation;
		break;
		
	case Environment.PRODUCTION:
		
		String configLocation = quoddyHome + "/quoddy-production.properties";
		println "####################\n######################\nadding configLocation: ${configLocation}\n###################";
		grails.config.locations << "file:" + configLocation;
		break;
		
	case Environment.TEST:
		String configLocation = quoddyHome + "/quoddy-test.properties";
		println "####################\n######################\nadding configLocation: ${configLocation}\n###################";
		grails.config.locations << "file:" + configLocation;
		break;
		
	default:
		break;
}


String fogbeamDevMode = System.getProperty( "fogbeam.devmode" );
if( fogbeamDevMode != null )
{
	fogbeam.devmode=true;
}
else
{
	fogbeam.devmode=false;
}

// backingStore options: "ldap" or "localdb"
// for now, assume they have to change in tandem: all "ldap" or all "localdb"
// we're not yet - if ever - interested in any really bizarre hybrid scenarios on this
friends.backingStore="localdb";
groups.backingStore="localdb";
enable.self.registration=true;
created.accounts.backingStore="localdb";
// TODO: parameters for "is ldap authentication enabled"
// and "is local account authentication enabled" etc.
 

grails.views.javascript.library="jquery"
grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      // xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      // json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder=false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable fo AppEngine!
grails.logging.jul.usebridge = false
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// set per-environment serverURL stem for creating absolute links
environments {
    production {
		def serverPort = System.getProperty( "server.port");
		if( serverPort == null )
		{
			serverPort = "8080";
		}
        grails.serverURL = "http://localhost:${serverPort}/${appName}"
    }
    development {
		def serverPort = System.getProperty( "server.port");
		if( serverPort == null )
		{
			serverPort = "8080";
		}

		grails.serverURL = "http://localhost:${serverPort}/${appName}"
    }
    test {
		def serverPort = System.getProperty( "server.port");
		if( serverPort == null )
		{
			serverPort = "8080";
		}

        grails.serverURL = "http://localhost:${serverPort}/${appName}"
    }

}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

	// debug  'org.hibernate'
	
	appenders {
            rollingFile name: "myAppender", maxFileSize: 10000000, file: "/opt/fogcutter/quoddy/quoddy.log", threshold: org.apache.log4j.Level.DEBUG
            console name: "stdout", threshold: org.apache.log4j.Level.DEBUG
	      }
	
	root {
           info 'stdout', 'myAppender'
         }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
	       'org.codehaus.groovy.grails.web.pages', //  GSP
	       'org.codehaus.groovy.grails.web.sitemesh', //  layouts
	       'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
	       'org.codehaus.groovy.grails.web.mapping', // URL mapping
	       'org.codehaus.groovy.grails.commons', // core / classloading
	       'org.codehaus.groovy.grails.plugins', // plugins
	       'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
	       'org.springframework',
               'net.sf.ehcache.hibernate',
	       'org.hibernate',
	       'net.sf.ehcache',
               'org.jboss',
               'org.jboss.remoting',
	       'org.quartz'

    warn   'org.mortbay.log',
           'org.hibernate'

    debug  'grails.controllers',
    	   'grails.services',
           'grails.domain'

}

jms {
	adapters {
		standard {
			messageConverter = null
		}
	}
}
     
security.shiro.redirect.uri = "/login/index";
