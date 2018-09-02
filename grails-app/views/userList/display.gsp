<html>
	
	<head>
		<title>Quoddy: Display LIST</title>
		<meta name="layout" content="main" />		
	</head>
	
	<body>		
		<p />
        <sec:ifLoggedIn>
        
			<g:if test="${activities != null}">

				<div id="activityStream">
				
					<g:render template="/ulistStream" />
				
				</div>
			</g:if>
		</sec:ifLoggedIn>
	</body>
</html>