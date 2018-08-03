<html>
	
	<head>
		<title>Quoddy: Create Event Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<div class="jumbotron span6">	
		<h2>Create Activiti UserTask Subscription</h2>
		<g:form controller="subscription" action="createWizard" method="POST">
			<label for="subscriptionName">Name</label> <g:textField name="subscriptionName" value=""/>
			<label for="subscriptionDescription">Description</label> <g:textField name="subscriptionDescription" value=""/>
			
			<label for="activitiServer">Activiti Server</label> <g:textField name="activitiServer" value=""/>
			<label for="candidateGroup">Candidate Group</label> <g:textField name="candidateGroup" value=""/>
			<label for="assignee">Assignee</label> <g:textField name="assignee" value=""/>
			<g:submitButton name="stage2" class="btn btn-large" value="Save" />
		</g:form>
		</div>
	</body>
</html>