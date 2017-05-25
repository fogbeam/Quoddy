
dataSource {
	pooled = true
	// driverClassName = "org.hsqldb.jdbcDriver"
	driverClassName = "org.postgresql.Driver"
	// username = "sa"
	username = "postgres"
	password = "database"
	// password = ""
	logSql=false
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {

			dbCreate = "update" // one of 'create', 'create-drop','update'
						//create-drop to drop/rebuild all tables
						//update to persist data
			// url = "jdbc:postgresql:quoddy2";
			// url = "jdbc:postgresql:quoddy_prhodes";
			url = "jdbc:postgresql:quoddy";
		}
	}
	test {
		dataSource {

			dbCreate = "update"
			url = "jdbc:hsqldb:mem:quoddy_test"
		}
	}
	production {
		dataSource {

			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:postgresql:quoddy"

		}
	}
}
