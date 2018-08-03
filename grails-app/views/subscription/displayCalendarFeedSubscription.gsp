<html>
	
	<head>
		<title>Quoddy: Display Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div id="bodyContent" class="span8">	
		<shiro:authenticated>
			<g:if test="${activities != null}">
					<div id="activityStream">
						<g:render template="/activityStream" />
					</div>
				</g:if>
        	</shiro:authenticated>
		</div>
	</body>
	
</html>