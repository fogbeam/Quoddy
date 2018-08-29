<html>
    <head>
        <title>Welcome to Grails</title>
        <meta name="layout" content="admin" />
    </head>
    <body>
    	<div id="bodyContent" style="height:600px;margin-left:100px;">
			<ul>
                <g:each var="queueKey" in="${queueKeys}">
                    <li style="padding-top:8px;">${queueKey}</li>
                </g:each>
            </ul>            
        </div>
    </body>
</html>
