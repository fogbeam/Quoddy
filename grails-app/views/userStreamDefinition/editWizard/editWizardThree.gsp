<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
		<div id="bodyContent" class="span9">
			<h3>Select users to include in this stream.</h3>
	        <g:form controller="userStreamDefinition" action="editWizard" method="POST">
				<g:select optionKey="uuid" optionValue="fullName" 
	        		name="users" 
	        		from="${users}"
	        		value="${selectedUsers}" 
	        		multiple="true" />
				<g:submitButton name="stage4" value="Next" />
			</g:form>
		</div>
	</body>
</html>