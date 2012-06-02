grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits( "global" ) {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {        
				// If we can use a local version, do it
        mavenLocal()

				// These have the most meta-data
				ebr()
        mavenCentral()
        grailsPlugins()

				// Fall back (less preferable: little meta-data)
				grailsCentral()
        grailsHome()

				// Random other places we can scour
				mavenRepo 'http://repo.springsource.org/snapshot'
				mavenRepo 'http://repo.springsource.org/release'
				mavenRepo 'http://maven.springsource.org/snapshot'
				mavenRepo 'http://maven.springsource.org/release'

				mavenRepo 'http://s3.amazonaws.com/maven.springframework.org/milestone'
				mavenRepo 'http://s3.amazonaws.com/maven.springframework.org/snapshot'
		
				['releases','thirdparty-releases','thirdparty-uploads','deprecated','snapshots'].each { dir ->	
					mavenRepo "http://repository.jboss.org/nexus/content/repositories/$dir"
				}
				mavenRepo 'http://repository.jboss.org/nexus/content/groups/public-jboss/'
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
				mavenRepo 'http://repository.opencastproject.org/nexus/content/repositories/com.springsource.repository.bundles.release/'
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
			compile 'com.google.guava:guava:12.0'
			//compile 'org.jsoup:jsoup:1.6.3' // Replaces HTMLParser and NekoHTML
			compile 'com.fasterxml.jackson.core:jackson-core:2.0.2'
			compile 'net.sf.trove4j:trove4j:3.0.1'
			compile 'joda-time:joda-time:2.1'
			compile 'org.springframework.ldap:spring-ldap:1.3.1.RELEASE'
			compile 'org.apache.httpcomponents:httpclient:4.2'
    }

		plugins {
			compile ':code-coverage:1.2.5'
			compile ':jms:1.2' // TODO: Upgrade to JMS 1.2.0.1
			compile ':jquery:1.7.1'
			compile ':navigation:1.3.2'
			compile ':quartz:1.0-RC2'
			compile ':webflow:1.3.7'
		}

}
