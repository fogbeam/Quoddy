<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="main" />
    </head>
	<body>
		<div id="bodyContent">	
		
		<h3>Update GROUP</h3>
		<p />
		<g:form controller="calendar" action="updateFeed" method="POST">
			<g:hiddenField name="calFeedId" value="${calFeedToEdit.id}" />
			<label for="calFeedName">Name:</label> <g:textField name="calFeedName" value="${calFeedToEdit.name}"/>
			<br />
			<label for="calFeedUrl">URL:</label> <g:textField name="calFeedUrl" value="${calFeedToEdit.url}"/>
			<br />
			<g:submitButton name="Update" />
		</g:form>
		
		</div>
	</body>
</html>