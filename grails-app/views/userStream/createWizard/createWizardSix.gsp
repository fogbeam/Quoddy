<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
	
		<h3>Select subscriptions to include in this stream.</h3>
        <g:form controller="userStream" action="createWizard" method="POST">
			<g:select optionKey="uuid" optionValue="name" 
        		name="eventSubscriptions" 
        		from="${eventSubscriptions}"
        		value="${selectedEventSubscriptions}" 
        		multiple="true" />
			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
		
	</body>
</html>