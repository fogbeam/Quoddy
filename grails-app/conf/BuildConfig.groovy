grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits( "global" ) {
        // uncomment to disable ehcache
		excludes 'slf4j-api', 'slf4j-log4j12', 'jcl-over-slf4j', 'jul-to-slf4j'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {        
	
	grailsCentral()
//         grailsPlugins()
        grailsHome()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
	mavenRepo "http://maven.restlet.org/"
	mavenRepo "https://repo.grails.org/grails/plugins"
    }
    
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
		compile "org.grails:grails-webflow:$grailsVersion"
		compile "antlr:antlr:2.7.7"
		compile "commons-logging:commons-logging:1.1.1"

		compile "org.grails:grails-webflow:$grailsVersion"

	}    
   plugins {

	  compile ':webflow:2.0.0', {
     	  	  exclude 'grails-webflow'
   		  }
	   
	   runtime( ":shiro:1.1.4" ){
		exclude 'quartz';   
	   }

	   runtime( ":jaxrs:0.8" )
	   {
//                exclude group:"org.restlet.gae", name:'org.restlet.ext.json'
//               exclude group:"org.restlet.gae", name:'org.restlet.ext.servlet'               
//	       exclude group:"org.restlet.gae", name:'org.restlet'
           }

   	}
}