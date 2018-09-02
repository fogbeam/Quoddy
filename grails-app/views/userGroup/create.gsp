<html>
	
	<head>
		<title>Quoddy: Create GROUP</title>
		<meta name="layout" content="main" />	
	</head>
	
	<body>
	  <div class="jumbotron span6">	
		<h2>Create Group</h2>
		<p />
		<g:form controller="userGroup" action="save" method="POST">
			<label for="groupName">Name:</label> <g:textField name="groupName" value=""/>
			<br />
			<label for="groupDescription">Description:</label> <g:textField name="groupDescription" value=""/>
			<br />
			<g:submitButton name="Create" class="btn" />
		</g:form>
	</div>
	</body>
</html>