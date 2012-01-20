<html>
	
	<head>
		<title>Quoddy: Edit GROUP</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Edit GROUP</h3>
		<p />
		<g:form controller="userGroup" action="update" method="POST">
			<g:hiddenField name="groupId" value="${groupToEdit.id}" />
			<label for="groupName">Name:</label> <g:textField name="groupName" value="${groupToEdit.name}"/>
			<br />
			<g:submitButton name="Save" />
		</g:form>
	</body>
</html>