<html>

<head>
<title>Quoddy: Search Results</title>
<meta name="layout" content="main" />
<nav:resources />
</head>

<body>
	<div id="bodyContent" class="span8">
		<p />
		<g:each in="${searchResults}" var="searchResult">
			<g:if test="${searchResult.object.streamObject != null}">
				<g:render
					template="${searchResult.object.streamObject.templateName}"
					var="item" bean="${searchResult.object}" />
			</g:if>
			<g:else>
				<g:render template="${searchResult.object.templateName}" var="item"
					bean="${searchResult.object}" />
			</g:else>

		</g:each>
	</div>

</body>

</html>