<html>
	
	<head>
		<title>Quoddy: Create Event Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<div class="hero-unit span6">	
		<h2>Create Event Subscription</h2>
		<g:form controller="subscription" action="createWizard" method="POST">
			<label for="subscriptionName">Name</label> <g:textField name="subscriptionName" value=""/>
			<label for="subscriptionDescription">Description</label> <g:textField name="subscriptionDescription" value=""/>
			<g:submitButton name="stage2" class="btn btn-large" value="Next" />
		</g:form>
		</div>
	</body>
</html>