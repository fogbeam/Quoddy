<html>
	
	<head>
		<title>Quoddy: People Search Results</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div class="span8">
			<p />
			<ul style="margin-left:315px;">
				<g:each in="${searchResults}" var="searchResult">
					<g:if test="${searchResult.object.hasProperty('streamObject') && searchResult.object.streamObject != null}">
						<g:render template="${searchResult.object.streamObject.templateName}" var="item" bean="${searchResult.object}" /> 
					</g:if>
					<g:else>
						<g:render template="${searchResult.object.templateName}" var="item" bean="${searchResult.object}" />
					</g:else>
					
				</g:each>
			</ul>
		</div>
	</body>
	
</html>