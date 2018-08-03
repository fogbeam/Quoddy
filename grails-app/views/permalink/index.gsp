<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="main" />
    </head>
	<body>
		<div id="bodyContent" class="span8">
			<g:render template="${item.templateName}" var="item" bean="${item}" />
		</div>
	</body>
</html>