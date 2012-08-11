<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
	  <div class="hero-unit span6">	
		<h2>Select user lists to include in this stream.</h2>
        <g:form controller="userStream" action="createWizard" method="POST">
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