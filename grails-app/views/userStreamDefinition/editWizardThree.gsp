<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
		<div id="bodyContent" class="span9">
			<h3>Select users to include in this stream.</h3>
	        <g:form controller="userStreamDefinition" action="editWizardFour" method="POST">
				
				<label for="includeSelf" style="display:inline;">Include Myself: </label><g:checkBox name="includeSelf" value="${streamToEdit.includeSelf}"/>
				<p />
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