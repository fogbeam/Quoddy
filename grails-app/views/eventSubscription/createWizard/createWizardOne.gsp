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
			<label for="subscriptionName">Name:</label> <g:textField name="subscriptionName" value=""/>
			<br />
			<label for="subscriptionDescription">Description:</label> <g:textField name="subscriptionDescription" value=""/>
			<br />
			<g:submitButton name="stage2" value="Next" />
		</g:form>
	</body>
</html>