<html>
	
	<head>
		<title>Quoddy: Create Event Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
  <div class="jumbotron span6">
  <h2>Create Event Subscription</h2>
		<p />
		<g:form controller="subscription" action="createWizard" method="POST">
			<label for="xQueryExpression">Query:</label> <g:textField name="xQueryExpression" value=""/>
			<br />
			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
  </div>
	</body>
</html>