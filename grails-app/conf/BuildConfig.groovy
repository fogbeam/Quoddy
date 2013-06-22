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
        grailsPlugins()
        grailsHome()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
	compile "org.grails:grails-webflow:$grailsVersion"
    }
    
   plugins {
   compile ':webflow:2.0.0', {
     exclude 'grails-webflow'
   }

}
}