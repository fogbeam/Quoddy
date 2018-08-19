<html>
	
	<head>
		<title>Quoddy: The page for managing Event Subscriptions</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>

  <div class="span8 listView">
  <!-- Blank state, should display when there are not any subscriptions
  <div class="jumbotron">
    <h1>Subscriptions</h1>
    <p>Add new event subscriptions and manage existing ones here. <a href="#">What are subscriptions?</a></p>
    <p><a class="btn btn-primary btn-large">Create a New Subscription</a></p>
  </div>
  --> 


  <!-- populated state -->
  <div class="jumbotron">
    <div class="span4">
    <h2>Subscriptions</h2>
    <p>Manage existing event subscriptions and create new ones.</p>
    </div>
    <div class="span4 offset3">
		<g:link controller="subscription" action="createWizardOne" class="btn btn-primary btn-large">Create New Subscription</g:link>
    </div>
  <div class="clear"></div>
  </div>

		<ul>
			<g:each var="subscription" in="${businessEventSubscriptions}">
				<li><g:link controller="subscription" action="editWizard" event="start" params="[subscriptionId:subscription.id,subscriptionType:subscription.class.simpleName]" >${subscription.name}</g:link> </li>
			</g:each>
		</ul>
		
		<ul>
			<g:each var="subscription" in="${calendarFeedSubscriptions}">
				<li><g:link controller="subscription" action="editWizard" event="start" params="[subscriptionId:subscription.id,subscriptionType:subscription.class.simpleName]" >${subscription.name}</g:link> </li>
			</g:each>
		</ul>
		
		<ul>
			<g:each var="subscription" in="${activitiUserTaskSubscriptions}">
				<li><g:link controller="subscription" action="editWizard" event="start" params="[subscriptionId:subscription.id,subscriptionType:subscription.class.simpleName]" >${subscription.name}</g:link> </li>
			</g:each>
		</ul>	
		
		<ul>
			<g:each var="subscription" in="${rssFeedSubscriptions}">
				<li><g:link controller="subscription" action="editWizard" event="start" params="[subscriptionId:subscription.id,subscriptionType:subscription.class.simpleName]" >${subscription.name}</g:link> </li>
			</g:each>
		</ul>		
	</div>
	</body>
	
</html>