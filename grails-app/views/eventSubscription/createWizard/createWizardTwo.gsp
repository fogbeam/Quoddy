<html>
	
	<head>
		<title>Quoddy: Create Event Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Create Event Subscription</h3>
		<p />
		<g:form controller="eventSubscription" action="createWizard" method="POST">
			<label for="xQueryExpression">Name:</label> <g:textField name="xQueryExpression" value=""/>
			<br />
			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
	</body>
</html>