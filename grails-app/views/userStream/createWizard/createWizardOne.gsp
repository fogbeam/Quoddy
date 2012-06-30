<html>
	
	<head>
		<title>Quoddy: Create User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Create User Stream</h3>
		<p />
		<g:form controller="userStream" action="createWizard" method="POST">
			<label for="streamName">Name:</label> <g:textField name="streamName" value=""/>
			<br />
			<label for="streamDescription">Description:</label> <g:textField name="streamDescription" value=""/>
			<br />
			<g:submitButton name="stage2" value="Next" />
		</g:form>
	</body>
</html>