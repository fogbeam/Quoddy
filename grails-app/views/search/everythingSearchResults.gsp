<html>
	
	<head>
		<title>Quoddy: People Search Results</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<p />
		Results:
		<p />
		<ul style="margin-left:315px;">
			<g:each in="${searchResults}" var="searchResult">
			
				<!-- <li>AAA: ${searchResult.uuid} - ${searchResult.docType}</li> -->
				<g:render template="${searchResult.object.templateName}" var="item" bean="${searchResult.object}" />
								
			</g:each>
		</ul>
		
	</body>
	
</html>