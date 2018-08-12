<html>
	
	<head>
		<title>Quoddy: Display Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div id="bodyContent" class="span8">	
		<sec:ifLoggedIn>
			<g:if test="${activities != null}">
					<div id="activityStream">
						<g:render template="/activityStream" />
					</div>
				</g:if>
        	</sec:ifLoggedIn>
		</div>
	</body>
	
</html>