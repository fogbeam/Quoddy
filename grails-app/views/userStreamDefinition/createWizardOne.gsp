<html>
	
	<head>
		<title>Quoddy: Create User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div class="jumbotron span6">	
		<h2>Create User Stream</h2>
		<p />
		<g:form controller="userStreamDefinition" action="createWizardTwo" method="POST">
			<label for="streamName">Name:</label> <g:textField name="streamName" value=""/>
			<br />
			<label for="streamDescription">Description:</label> <g:textField name="streamDescription" value=""/>
			<br />
			<g:submitButton name="stage2" class="btn" value="Next" />
		</g:form>
	</div>
	</body>
</html>