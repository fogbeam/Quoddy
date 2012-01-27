<html>
	
	<head>
		<title>Quoddy: Create STREAM</title>
		<meta name="layout" content="main" />
	    <nav:resources />		
	</head>
	
	<body>
	
		<h3>Create STREAM</h3>
		<p />
		<g:form controller="userStream" action="save" method="POST">
			<label for="streamName">Name:</label> <g:textField name="streamName" value=""/>
			<br />
			<g:submitButton name="Create" />
		</g:form>
	</body>
</html>