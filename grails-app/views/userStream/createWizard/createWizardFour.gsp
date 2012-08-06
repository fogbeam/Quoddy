<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
	
		<h3>Select user lists to include in this stream.</h3>
        <g:form controller="userStream" action="createWizard" method="POST">
			<g:select optionKey="uuid" optionValue="name" 
        		name="userLists" 
        		from="${userLists}"
        		value="${selectedUserLists}" 
        		multiple="true" />
			<g:submitButton name="stage5" value="Next" />
		</g:form>
		
	</body>
</html>