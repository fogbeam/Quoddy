<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
	
		<h3>Select subscriptions to include in this stream.</h3>
        <g:form controller="userStream" action="createWizard" method="POST">
			<!-- TODO: add subscription list-->
			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
		
	</body>
</html>