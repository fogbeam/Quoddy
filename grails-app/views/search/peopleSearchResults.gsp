<html>
	
	<head>
		<title>Quoddy: People Search Results</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div id="bodyContent" class="span8">
			<p />
			<g:each in="${allUsers}" var="searchResult">
				<g:render template="${searchResult.object.templateName}" var="item"
					bean="${searchResult.object}" />
			</g:each>
		</div>
	</body>
	
</html>