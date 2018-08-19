<html>
	
	<head>
		<title>Quoddy: Create RSS Feed Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<div class="jumbotron span6">	
		<h2>Edit RSS Feed Subscription</h2>
		<g:form controller="subscription" action="editRssFeedSubscriptionWizardFinish" method="POST">
			<label for="subscriptionName">Name:</label> <g:textField name="subscriptionName" value="${subscriptionToEdit.name}"/>
			<br />
			<label for="subscriptionUrl">URL:</label> <g:textField name="subscriptionUrl" value="${subscriptionToEdit.url}"/>
			<br />
			<g:submitButton name="stage2" class="btn btn-large" value="Save" />
		</g:form>
		</div>
	</body>
</html>