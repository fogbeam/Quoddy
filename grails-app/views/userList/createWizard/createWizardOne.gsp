<html>
	
	<head>
		<title>Quoddy: Create LIST</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Create LIST</h3>
		<p />
		<g:form controller="userList" action="createWizard" method="POST">
			<label for="listName">Name:</label> <g:textField name="listName" value=""/>
			<br />
			<label for="listDescription">Description:</label> <g:textField name="listDescription" value=""/>
			<br />
			<g:submitButton name="stage2" value="Next" />
		</g:form>
	</body>
</html>