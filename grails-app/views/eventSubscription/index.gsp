<html>
	
	<head>
		<title>Quoddy: The page for managing Event Subscriptions</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<p />
		<h3>The page for managing Event Subscriptions</h3>
		<g:link controller="eventSubscription" action="createWizard" style="float:right;color:orange;margin-right:200px;margin-bottom:10px;">Create New Subscription</g:link>
		<p />
		<ul style="margin-left:25px;margin-top:40px;">
			<g:each var="subscription" in="${eventSubscriptions}">
				<li><g:link controller="eventSubscription" action="editWizard" event="start" params="[subscriptionId:subscription.id]" >${subscription.name}</g:link> </li>
			</g:each>
		</ul>		
	</body>
	
</html>