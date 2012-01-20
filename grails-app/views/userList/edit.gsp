<html>
	
	<head>
		<title>Quoddy: Edit LIST</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Edit LIST</h3>
		<p />
		<g:form controller="userList" action="update" method="POST">
			<g:hiddenField name="listId" value="${listToEdit.id}" />
			<label for="listName">Name:</label> <g:textField name="listName" value="${listToEdit.name}"/>
			<br />
			<g:submitButton name="Save" />
		</g:form>
	</body>
</html>