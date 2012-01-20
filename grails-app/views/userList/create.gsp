<html>
	
	<head>
		<title>Quoddy: Create LIST</title>
		<meta name="layout" content="main" />
	    <nav:resources />		
	</head>
	
	<body>
	
		<h3>Create LIST</h3>
		<p />
		<g:form controller="userList" action="save" method="POST">
			<label for="listName">Name:</label> <g:textField name="listName" value=""/>
			<br />
			<g:submitButton name="Create" />
		</g:form>
	</body>
</html>