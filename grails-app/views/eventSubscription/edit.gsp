<html>
	
	<head>
		<title>Quoddy: Edit SUBSCRIPTION</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Edit SUBSCRIPTION</h3>
		<p />
		<g:form controller="eventSubscription" action="update" method="POST">
			<g:hiddenField name="eventSubscriptionId" value="${subscriptionToEdit.id}" />
			<label for="subscriptionName">Name:</label> <g:textField name="subscriptionName" value="${subscriptionToEdit.name}"/>
			<br />
			<label for="subscriptionDescription">Description:</label> <g:textField name="subscriptionDescription" value="${subscriptionToEdit.description}"/>
			<br/>
			<g:submitButton name="Save" />
		</g:form>
	</body>
</html>