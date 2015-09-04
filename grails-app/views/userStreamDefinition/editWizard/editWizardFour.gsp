<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
		<div id="bodyContent" class="span9">
			<h3>Select user lists to include in this stream.</h3>
	        <g:form controller="userStreamDefinition" action="createWizard" method="POST">
				<g:select optionKey="uuid" optionValue="name" 
	        		name="userLists" 
	        		from="${userLists}"
	        		value="${selectedUserLists}" 
	        		multiple="true" />
				<g:submitButton name="stage5" value="Next" />
			</g:form>
		</div>	
	</body>
</html>