<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="main" />
    </head>
	<body>
		<div id="bodyContent">	
		
		<h3>Create GROUP</h3>
		<p />
		<g:form controller="calendar" action="saveFeed" method="POST">
			<label for="calFeedName">Name:</label> <g:textField name="calFeedName" value=""/>
			<br />
			<label for="calFeedUrl">URL:</label> <g:textField name="calFeedUrl" value=""/>
			<br />
			<g:submitButton name="Create" />
		</g:form>
		
		</div>
	</body>
</html>