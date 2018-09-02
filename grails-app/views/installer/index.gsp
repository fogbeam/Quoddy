<html>
    <head>
		<title>Quoddy: Installer</title>
       	<meta name="layout" content="main" />
    </head>
	<body>
		<h1>Quoddy: Installer</h1>
		
		<p />
             <g:hasErrors>
                 <div class="errors">
                    <g:renderErrors bean="${flash.user}" as="list" />
                 </div>
             </g:hasErrors>		
		<p />
		
		TODO: A form would go here, leading into a wizard for doing initial system setup...
		See: SystemSettings.groovy, etc.
		
	</body>
</html>