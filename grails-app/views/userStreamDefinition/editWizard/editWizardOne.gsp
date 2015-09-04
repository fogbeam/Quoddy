<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div id="bodyContent" class="span9">
			<h3>Edit User Stream</h3>
			<p />
			<g:form controller="userStreamDefinition" action="editWizard" method="POST">
				<g:hiddenField name="streamId" value="${streamToEdit.id}" />
				<label for="streamName">Name:</label> <g:textField name="streamName" value="${streamToEdit.name}"/>
				<br />
				<label for="streamDescription">Description:</label> <g:textField name="streamDescription" value="${streamToEdit.description}"/>
				<br />
				
				<div class="well" style="width:50%;">
					<label for="includeEverything">Include Everything: </label> <g:checkBox name="includeEverything" value="${streamToEdit.includeEverything}" />
					Warning: When this option is checked, it will override any and all other settings applied to this UserStream.  "Include Everything"
					really means "include everything".  
				</div>
				
				<br />
				<g:submitButton name="stage2" value="Next" />
			</g:form>
		</div>
	</body>
</html>