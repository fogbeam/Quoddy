<html>
	
	<head>
		<title>Quoddy: Edit STREAM</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Edit STREAM</h3>
		<p />
		<g:form controller="userStream" action="update" method="POST">
			<g:hiddenField name="streamId" value="${streamToEdit.id}" />
			<label for="streamName">Name:</label> <g:textField name="streamName" value="${streamToEdit.name}"/>
			<br />
			<g:submitButton name="Save" />
		</g:form>
	</body>
</html>