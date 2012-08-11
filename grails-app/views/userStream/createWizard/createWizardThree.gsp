<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <nav:resources />	
	</head>
	
	<body>
	  <div class="hero-unit span6">	
		<h2>Select users to include in this stream.</h2>
        <g:form controller="userStream" action="createWizard" method="POST">
			<g:select optionKey="uuid" optionValue="fullName" 
        		name="users" 
        		from="${users}"
        		value="${selectedUsers}" 
        		multiple="true" />
			<g:submitButton name="stage4" value="Next" class="btn" />
		</g:form>
		</div>
	</body>
</html>