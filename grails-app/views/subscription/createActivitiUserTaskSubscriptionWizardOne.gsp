<html>
	
	<head>
		<title>Quoddy: Create Event Subscription</title>
		<meta name="layout" content="main" />	
	</head>
	
	<body>
	<div class="jumbotron span6">	
		<h2>Create Activiti UserTask Subscription</h2>
		<g:form controller="subscription" action="createActivitiUserTaskSubscriptionWizardFinish" method="POST">
			<label for="subscriptionName">Name</label> <g:textField name="subscriptionName" value=""/>
			<br />
			<label for="subscriptionDescription">Description</label> <g:textField name="subscriptionDescription" value=""/>
			<br />
			<label for="activitiServer">Activiti Server</label> <g:textField name="activitiServer" value=""/>
			<br />
			<label for="candidateGroup">Candidate Group</label> <g:textField name="candidateGroup" value=""/>
			<br />
			<label for="assignee">Assignee</label> <g:textField name="assignee" value=""/>
			<br />
			<g:submitButton name="stage2" class="btn btn-large" value="Save" />
		</g:form>
		</div>
	</body>
</html>