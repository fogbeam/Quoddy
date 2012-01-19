<html>
	
	<head>
		<title>Quoddy: Post ActivityStream as JSON</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<p />
		<g:form controller="activityStream" action="index" method="POST">
			<g:textArea name="activityJson" style="height:400px;width:600px;" />
			<br />
			<input type="submit" name="submitJson" value="SubmitJson" />
		</g:form>
	</body>
	
</html>