<html>
	
	<head>
		<title>Quoddy: Create Calendar Feed Subscription</title>
		<meta name="layout" content="main" />	
	</head>
	
	<body>
	<div class="jumbotron span6">	
		<h2>Create Calendar Feed Subscription</h2>
		<g:form controller="subscription" action="createCalendarFeedSubscriptionWizardFinish" method="POST">
			<label for="calFeedName">Name:</label> <g:textField name="calFeedName" value=""/>
			<br />
			<label for="calFeedUrl">URL:</label> <g:textField name="calFeedUrl" value=""/>
			<br />
			<g:submitButton name="stage2" class="btn btn-large" value="Save" />
		</g:form>
		</div>
	</body>
</html>