<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="main" />
    </head>
	<body>
		<div id="bodyContent">	
			FOO
			<p />
			<ul>
				<g:each in="${events}" var="event">
					<li>${event.name} - ${event.class}</li>
				</g:each>
			</ul>
		</div>
	</body>
</html>