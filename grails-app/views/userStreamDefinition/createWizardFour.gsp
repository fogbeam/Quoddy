<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	</head>
	
	<body>
	  <div class="jumbotron span6">	
		<h2>Select user lists to include in this stream.</h2>
        <g:form controller="userStreamDefinition" action="createWizardFive" method="POST">
			<g:select optionKey="uuid" optionValue="name" 
        		name="userLists" 
        		from="${userLists}"
        		value="${selectedUserLists}" 
        		multiple="true" />
			<g:submitButton name="stage5" value="Next" class="btn" />
		</g:form>
	</div>
	</body>
</html>