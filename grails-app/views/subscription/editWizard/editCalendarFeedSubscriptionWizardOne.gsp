<html>
	
	<head>
		<title>Quoddy: Create Calendar Feed Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<div class="hero-unit span6">	
		<h2>Edit Calendar Feed Subscription</h2>
		<g:form controller="subscription" action="editWizard" method="POST">
			<label for="calFeedName">Name:</label> <g:textField name="calFeedName" 
													value="${subscriptionToEdit.name}"/>
			<br />
			<label for="calFeedUrl">URL:</label> <g:textField name="calFeedUrl" 
													value="${subscriptionToEdit.url}"/>
			<br />
			<g:submitButton name="finish" class="btn btn-large" value="Save" />
		</g:form>
		</div>
	</body>
</html>