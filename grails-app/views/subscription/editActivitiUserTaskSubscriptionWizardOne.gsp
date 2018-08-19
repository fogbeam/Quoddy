<html>
	
	<head>
		<title>Quoddy: Create Event Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<div class="jumbotron span6">	
		<h2>Edit Activiti UserTask Subscription</h2>
		<g:form controller="subscription" action="editActivitiUserTaskSubscriptionWizardFinish" method="POST">
			<label for="subscriptionName">Name</label> <g:textField name="subscriptionName" 
																	value="${subscriptionToEdit.name}"/>
		    <br />
			<label for="subscriptionDescription">Description</label> <g:textField name="subscriptionDescription" 
																	value="${subscriptionToEdit.description}"/>
			<br />
			<label for="activitiServer">Activiti Server</label> <g:textField name="activitiServer" 
																	value="${subscriptionToEdit.activitiServer}"/>
            <br />
			<label for="candidateGroup">Candidate Group</label> <g:textField name="candidateGroup" 
																	value="${subscriptionToEdit.candidateGroup}"/>
			<br />														
			<label for="assignee">Assignee</label> <g:textField name="assignee" 
																	value="${subscriptionToEdit.assignee}"/>
			<br />														
			<g:submitButton name="finish" class="btn btn-large" value="Save" />
		</g:form>
		</div>
	</body>
</html>