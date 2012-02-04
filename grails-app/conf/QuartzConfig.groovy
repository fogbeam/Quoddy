
quartz {
    autoStartup=true
    jdbcStore=true
    waitForJobsToCompleteOnShutdown=true
    exposeSchedulerInRepository = false

    props {
		scheduler.instanceName="mySchedulerInstance"
        scheduler.skipUpdateCheck=true
		threadPool.threadCount=10
		threadPool.class='org.quartz.simpl.SimpleThreadPool'	
		jobStore.class='org.quartz.impl.jdbcjobstore.JobStoreTX'
		jobStore.driverDelegateClass='org.quartz.impl.jdbcjobstore.PostgreSQLDelegate'
		jobStore.tablePrefix='QRTZ_'
		jobStore.dataSource='myDS'
    }
}

environments {
    test {
        quartz {
            autoStartup = false
        }
    }
}
