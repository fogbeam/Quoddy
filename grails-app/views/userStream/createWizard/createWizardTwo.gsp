<html>
	
	<head>
		<title>Quoddy: Create User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	  <div class="hero-unit span6">	
		<h2>Select the event types you would like in this stream.</h2>
        <g:form controller="userStream" action="createWizard" method="POST">
        	<g:select optionKey="id" optionValue="name" 
        		name="eventTypes" 
        		from="${eventTypes}"
        		value="${selectedEventTypes}" 
        		multiple="true" />
        	<g:submitButton name="stage3" value="Next" class="btn" />
		</g:form>
		</div>
	</body>
</html>