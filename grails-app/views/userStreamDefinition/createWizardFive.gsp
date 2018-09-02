<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	</head>
	
	<body>
  <div class="jumbotron span6">	
		<h2>Select groups to include in this stream.</h2>
        <g:form controller="userStreamDefinition" action="createWizardSix" method="POST">
			<g:select optionKey="uuid" optionValue="name" 
        		name="userGroups" 
        		from="${groups}"
        		value="${selectedGroups}" 
        		multiple="true" />
			<g:submitButton name="stage6" value="Next" class="btn" />
		</g:form>
	</div>	
	</body>
</html>