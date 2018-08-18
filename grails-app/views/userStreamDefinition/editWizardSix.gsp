<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
		<div id="bodyContent" class="span9">
			<h3>Select subscriptions to include in this stream.</h3>
	        <g:form controller="userStreamDefinition" action="editWizardFinish" method="POST">
				<g:select optionKey="uuid" optionValue="name" 
	        		name="eventSubscriptions" 
	        		from="${eventSubscriptions}"
	        		value="${selectedEventSubscriptions}" 
	        		multiple="true" />
				<g:submitButton name="finishWizard" value="Finish" />
			</g:form>
		</div>
	</body>
</html>