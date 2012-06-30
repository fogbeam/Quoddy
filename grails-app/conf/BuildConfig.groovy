grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"

//grails.plugin.location.jms= "../grails-jms"


//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits( "global" ) {
        // uncomment to disable ehcache
        excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {        
				// If we can use a local version, do it
        mavenLocal()

				// Sonatype snapshots and releases, which are fresher than mavenCentral
				mavenRepo 'https://oss.sonatype.org/content/repositories/snapshots/'
				mavenRepo 'https://oss.sonatype.org/content/repositories/releases/'

				// These have the most meta-data
				ebr()
        mavenCentral()
        grailsPlugins()

				// Fall back (less preferable: little meta-data)
				grailsCentral()
        grailsHome()

				mavenRepo 'http://repo.smokejumperit.com'

				// Random other places we can scour
/*
				['releases','thirdparty-releases','thirdparty-uploads','deprecated','snapshots'].each { dir ->	
					mavenRepo "http://repository.jboss.org/nexus/content/repositories/$dir"
				}
				mavenRepo 'http://repository.jboss.org/nexus/content/groups/public-jboss/'
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
*/
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // runtime 'mysql:mysql-connector-java:5.1.5'

			// Cannot find a maven repo for Exist 

			compile 'com.google.guava:guava:12.0'
			compile 'commons-io:commons-io:2.3'
			//compile 'org.jsoup:jsoup:1.6.3' // Replaces HTMLParser and NekoHTML
			compile 'com.fasterxml.jackson.core:jackson-core:2.0.2'
			compile 'com.fasterxml.jackson.core:jackson-databind:2.0.2'
			//compile 'net.sf.trove4j:trove4j:3.0.1'
			compile 'joda-time:joda-time:2.1'
			compile 'org.springframework.ldap:spring-ldap:1.3.1.RELEASE'
			compile 'org.springframework.ldap:spring-ldap-core:1.3.1.RELEASE'
			compile 'org.apache.httpcomponents:httpclient:4.2'
			compile 'org.mnode.ical4j:ical4j:1.0.3'
			compile 'org.apache.lucene:lucene-core:3.6.0'
			compile 'excalibur-cli:excalibur-cli:1.0'
			compile 'jgroups:jgroups-all:2.4.1'
			runtime 'postgresql:postgresql:9.1-901.jdbc4'
    }

		plugins {
			compile ':jaxrs:0.6'
			compile ':rest-client-builder:1.0.2'
			compile ':code-coverage:1.2.5'
			compile('Fogbeam-Labs:jms:1.3') {
				changing = true
			}
			compile ':jquery:1.7.2'
			compile ':navigation:1.3.2'
			compile ':quartz:1.0-RC2'
			compile ':webflow:1.3.8'
		}

}
