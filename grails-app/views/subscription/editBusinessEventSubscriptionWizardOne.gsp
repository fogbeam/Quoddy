<html>
	
	<head>
		<title>Quoddy: Edit Event Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Edit Event Subscription</h3>
		<p />
		<g:form controller="subscription" action="editBusinessEventSubscriptionWizardFinish" method="POST">
			<g:hiddenField name="subscriptionId" value="${subscriptionToEdit.id}" />
			<label for="subscriptionName">Name:</label> <g:textField name="subscriptionName" value="${subscriptionToEdit.name}"/>
			<br />
			<label for="subscriptionDescription">Description:</label> <g:textField name="subscriptionDescription" value="${subscriptionToEdit.description}"/>
			<br />
			<label for="xQueryExpression">Query:</label> <g:textField name="xQueryExpression" value="${subscriptionToEdit.xQueryExpression}"/>
            <br />
			<g:submitButton name="stage2" value="Save" />
		</g:form>
	</body>
</html>