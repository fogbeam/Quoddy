<html>
	
	<head>
		<title>Quoddy: Edit Event Subscription</title>
		<meta name="layout" content="main" />	
	</head>
	
	<body>
	
		<h3>Edit Event Subscription</h3>
		<p />
		<g:form controller="subscription" action="createWizard" method="POST">
			<label for="xQueryExpression">Name:</label> <g:textField name="xQueryExpression" value="${subscriptionToEdit.xQueryExpression}" />
			<br />
			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
	</body>
</html>