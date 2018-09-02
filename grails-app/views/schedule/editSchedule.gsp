<html>
    <head>
        <title>Welcome to Grails</title>
        <meta name="layout" content="admin" />
    </head>
    <body>
        <div id="bodyContent" style="height:600px;margin-left:100px;">
            <h1 style="margin-top:7px;margin-bottom:8px;font-size:18pt;font-weight:bold;">Manage Job Schedule</h1>
            <table border="1" width="90%">
            	<tr>
            		<th>Name:</th>
            		<th>Group:</th>
            		<!-- <th>Full Name:</th> -->
            		<th>Job Name:</th>
            		<!-- <th>Full Job Name:</th> -->
            		<th>Previous Fire Time:</th>
					<!-- <th>Last Result:</th> -->
            		<th>Next Fire Time:</th>
            		<th>Times Triggered:</th>
            		<th>Repeat Interval:</th>
            		<th>Repeat Count</th>
            		<th colspan="3">&nbsp;</th>
            	</tr>
            	<g:each in="${existingTriggers}" var="trigger">
            		<tr style="padding-top:15px;">
            		<td>${trigger.name}</td>
            		<td>${trigger.group}</td>
            		<!-- <td>${trigger.fullName}</td> -->
            		<td>${trigger.jobName}</td>
            		<!-- <td>${trigger.fullJobName}</td> -->
            		<td>${trigger.previousFireTime}</td>
            		<!-- <td>${trigger.jobDataMap?.getBooleanValue("result")}</td> -->
            		<td>${trigger.nextFireTime}</td>
            		<td>${trigger.timesTriggered}</td>
            		<td>${trigger.repeatInterval}</td>
            		<td>${trigger.repeatCount}</td>
            		<td><g:link controller="schedule" action="editTrigger" params="[jobName:jobName,jobGroup:jobGroup, triggerName:trigger.name, triggerGroup:trigger.group, fullName:trigger.fullName, fullJobName:trigger.fullJobName]">edit trigger</g:link></td>
            		<td><g:link controller="schedule" action="deleteTrigger" params="[jobName:jobName,jobGroup:jobGroup, triggerName:trigger.name, triggerGroup:trigger.group, fullName:trigger.fullName, fullJobName:trigger.fullJobName]">delete trigger</g:link></td>
            		<td><g:link controller="schedule" action="executeJobNow" params="[jobName:jobName,jobGroup:jobGroup, triggerName:trigger.name, triggerGroup:trigger.group, fullName:trigger.fullName, fullJobName:trigger.fullJobName]">execute now</g:link></td>
            		</tr>
            	</g:each> 
            </table>
			<div style="margin-top:8px;">  <!-- params="[jobName:jobName,jobGroup:jobGroup]" -->
				<g:link controller="schedule" params='[jobId:"${jobFullName}"]' action="createTrigger">add new trigger</g:link>
			</div>
        </div>
    </body>
</html>
