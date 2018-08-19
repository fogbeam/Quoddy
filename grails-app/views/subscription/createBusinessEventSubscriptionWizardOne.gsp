<html>
	
	<head>
		<title>Quoddy: Create Event Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<div class="jumbotron span6">	
		<h2>Create Event Subscription</h2>
		<g:form controller="subscription" action="createBusinessEventSubscriptionWizardFinish" method="POST">
			<label for="subscriptionName">Name</label> <g:textField name="subscriptionName" value=""/>
			<br />
			<label for="subscriptionDescription">Description</label> <g:textField name="subscriptionDescription" value=""/>
			<br />
			<label for="xQueryExpression">Query:</label> <g:textField name="xQueryExpression" value=""/>
            <br />
			<g:submitButton name="stage2" class="btn btn-large" value="Next" />
		</g:form>
		</div>
	</body>
</html>