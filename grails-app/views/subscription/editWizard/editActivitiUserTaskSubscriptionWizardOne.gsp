<html>
	
	<head>
		<title>Quoddy: Create Event Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<div class="hero-unit span6">	
		<h2>Edit Activiti UserTask Subscription</h2>
		<g:form controller="subscription" action="editWizard" method="POST">
			<label for="subscriptionName">Name</label> <g:textField name="subscriptionName" 
																	value="${subscriptionToEdit.name}"/>
			<label for="subscriptionDescription">Description</label> <g:textField name="subscriptionDescription" 
																	value="${subscriptionToEdit.description}"/>
			
			<label for="activitiServer">Activiti Server</label> <g:textField name="activitiServer" 
																	value="${subscriptionToEdit.activitiServer}"/>
			<label for="candidateGroup">Candidate Group</label> <g:textField name="candidateGroup" 
																	value="${subscriptionToEdit.candidateGroup}"/>
			<label for="assignee">Assignee</label> <g:textField name="assignee" 
																	value="${subscriptionToEdit.assignee}"/>
			<g:submitButton name="finish" class="btn btn-large" value="Save" />
		</g:form>
		</div>
	</body>
</html>