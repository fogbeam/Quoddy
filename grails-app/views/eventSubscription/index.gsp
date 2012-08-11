<html>
	
	<head>
		<title>Quoddy: The page for managing Event Subscriptions</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>

  <div class="span8 listView">
  <!-- Blank state, should display when there are not any subscriptions
  <div class="hero-unit">
    <h1>Subscriptions</h1>
    <p>Add new event subscriptions and manage existing ones here. <a href="#">What are subscriptions?</a></p>
    <p><a class="btn btn-primary btn-large">Create a New Subscription</a></p>
  </div>
  --> 


  <!-- populated state -->
  <div class="hero-unit">
    <div class="span4">
    <h2>Subscriptions</h2>
    <p>Manage existing event subscriptions and create new ones.</p>
    </div>
    <div class="span4 offset3">
		<g:link controller="eventSubscription" action="createWizard" class="btn btn-primary btn-large">Create New Subscription</g:link>
    </div>
  <div class="clear"></div>
  </div>

		<ul>
			<g:each var="subscription" in="${eventSubscriptions}">
				<li><g:link controller="eventSubscription" action="editWizard" event="start" params="[subscriptionId:subscription.id]" >${subscription.name}</g:link> </li>
			</g:each>
		</ul>		
	</div>
	</body>
	
</html>