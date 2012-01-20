<html>
	
	<head>
		<title>Quoddy: Create GROUP</title>
		<meta name="layout" content="main" />
	    <nav:resources />		
	</head>
	
	<body>
	
		<h3>Create GROUP</h3>
		<p />
		<g:form controller="userGroup" action="save" method="POST">
			<label for="groupName">Name:</label> <g:textField name="groupName" value=""/>
			<br />
			<g:submitButton name="Create" />
		</g:form>
	</body>
</html>