<html>
	
	<head>
		<title>Quoddy: Create Subscription</title>
		<meta name="layout" content="main" />	
	</head>
	
	<body>
	<div class="jumbotron span6">	
		<h2>Create Subscription</h2>
		<g:form controller="subscription" action="createWizardTwo" method="POST">

			<g:radio name="subscriptionType" value="activitiUserTask" /> <span>Activiti User Task Subscription</span>
			<br />		
			<g:radio name="subscriptionType" value="businessEvent"/> <span>Business Event Subscription</span>
			<br />
			<g:radio name="subscriptionType" value="calendarFeed" /> <span>Calendar Feed Subscription</span>
			<br />
			<g:radio name="subscriptionType" value="rssFeed" /> <span>RSS Feed Subscription</span>
			<br />
			<g:submitButton name="stage2" class="btn btn-large" value="Next" />
		</g:form>
		</div>
	</body>
</html>