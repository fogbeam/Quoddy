<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
	
		<h3>Select groups to include in this stream.</h3>
        <g:form controller="userStream" action="createWizard" method="POST">
			<!-- TODO: add usergroup list-->
			<g:submitButton name="stage6" value="Next" />
		</g:form>
		
	</body>
</html>