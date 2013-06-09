<html>
	
	<head>
		<title>Quoddy: List SUBSCRIPTIONS</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>List SUBSCRIPTIONS</h3>
		<p />
		<ul style="margin-left:25px;margin-top:40px;">
			<g:each var="subscription" in="${allSubscriptions}">
				<li><g:link controller="subscription" action="display" params="[subscriptionId:subscription.id]" >${subscription.name}</g:link> </li>
			</g:each>
		</ul>	
	</body>
</html>