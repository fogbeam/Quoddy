<html>
    <head>
        <title>Welcome to Grails</title>
        <meta name="layout" content="admin" />
    </head>
    <body>
    	<div id="bodyContent" style="height:600px;margin-left:100px;">
            <h1 style="margin-top:7px;font-size:18pt;font-weight:bold;">Schedule Jobs</h1>
            <h2 style="margin-top:10px;font-size:13pt;font-weight:bold;">Available Jobs:</h2>
            <ul>
                <g:each var="job" in="${artefacts}">
                    <li style="padding-top:8px;"><g:link controller="schedule" action="editSchedule" params='[jobId:"${job.fullName}"]'>${job.shortName}</g:link> </li>
                </g:each>
            </ul>            
        </div>
    </body>
</html>
