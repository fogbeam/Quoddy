<html>
	
	<head>
		<title>Quoddy: Display SUBSCRIPTION</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<h3>Display SUBSCRIPTION</h3>
		<div id="bodyContent">	
		<g:if test="${session.user != null}">
			<g:if test="${activities != null}">
					<div id="activityStream">
						<g:render template="/activityStream" />
					</div>
				</g:if>
        	</g:if>
		</div>
	</body>
	
</html>