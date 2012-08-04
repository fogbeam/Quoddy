<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
	
		<h3>Select the event types you would like in this stream.</h3>
        <g:form controller="userStream" action="createWizard" method="POST">
        	<g:select optionKey="id" optionValue="name" 
        		name="eventTypes" 
        		from="${eventTypes}"
        		value="${selectedEventTypes}" 
        		multiple="true" />
			<g:submitButton name="stage3" value="Next" />
		</g:form>
		
	</body>
</html>