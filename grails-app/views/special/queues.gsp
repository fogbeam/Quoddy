<html>
    <head>
        <title>Welcome to Grails</title>
        <meta name="layout" content="admin" />
    </head>
    <body>
    	<div id="bodyContent" style="height:600px;margin-left:100px;">
			<ul>
                <g:each var="entryKeyAndSize" in="${entryKeysAndSizes}">
                    <li style="padding-top:8px;">${entryKeyAndSize[0]} &nbsp; -- &nbsp; ${entryKeyAndSize[1]}</li>
                </g:each>
            </ul>            
        </div>
    </body>
</html>
