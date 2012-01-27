<html>
	
	<head>
		<title>Quoddy: Edit LIST</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Edit LIST</h3>
		<p />
		<g:form controller="userList" action="editWizard" method="POST">
			<g:hiddenField name="listId" value="${listToEdit.id}" />
			<label for="listName">Name:</label> <g:textField name="listName" value="${listToEdit.name}"/>
			<br />
			<label for="listDescription">Description:</label> <g:textField name="listDescription" value="${listToEdit.description}"/>
			<br />
			<g:submitButton name="stage2" value="Next" />
		</g:form>
	</body>
</html>