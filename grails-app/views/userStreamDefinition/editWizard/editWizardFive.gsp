<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
	
		<h3>Select groups to include in this stream.</h3>
        <g:form controller="userStreamDefinition" action="createWizard" method="POST">
			<g:select optionKey="uuid" optionValue="name" 
        		name="userGroups" 
        		from="${groups}"
        		value="${selectedGroups}" 
        		multiple="true" />
			<g:submitButton name="stage6" value="Next" />
		</g:form>
		
	</body>
</html>