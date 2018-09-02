<html>
	
	<head>
		<title>Quoddy: Edit LIST</title>
		<meta name="layout" content="main" />	
	</head>
	
	<body>

<div class="hero span6">
		<h2>Edit LIST</h2>
		<p />
		<g:form controller="userList" action="editWizardTwo" method="POST">
			<g:hiddenField name="listId" value="${listToEdit.id}" />
			<label for="listName">Name:</label> <g:textField name="listName" value="${listToEdit.name}"/>
			<br />
			<label for="listDescription">Description:</label> <g:textField name="listDescription" value="${listToEdit.description}"/>
			<br />
			<g:submitButton name="stage2" value="Next" />
		</g:form>
    </div>
	</body>
</html>