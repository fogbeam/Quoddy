<html>
	
	<head>
		<title>Quoddy: Display LIST</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>		
		<p />
        <shiro:authenticated>
        
			<g:if test="${activities != null}">

				<div id="activityStream">
				
					<g:render template="/ulistStream" />
				
				</div>
			</g:if>
		</shiro:authenticated>
	</body>
</html>