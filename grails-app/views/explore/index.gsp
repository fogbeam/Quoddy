<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="main" />       	
    </head>
	<body>
		<div id="bodyContent">	
		<h3>Content</h3>
			<g:form  controller="explore" action="enrich" style="margin-top:50px;margin-left:270px;">
				<g:textArea name="content" style="width:400px;height:300px;"></g:textArea>
				<br />
				<g:submitButton id="enrichButton" name="enrichButton" value="Submit Normal"/> 
				<g:submitButton id="enrichButton" name="enrichButton" value="Enrich"/>
			</g:form>
		</div>
	</body>
</html>