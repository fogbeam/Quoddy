<html>
	
	<head>
		<title>Quoddy: Advanced Search</title>
		<meta name="layout" content="main" />
	     <nav:resources />
	</head>
	
	<body>
		<h2>Quoddy: Advanced Search</h2>
		<p>
			<g:form controller="search" action="doEverythingSearch" method="GET" >	
			<label for="searchQuery">Search:</label><g:textField name="queryString" value="" />
			<br />
			<g:submitButton name="Search" />
			</g:form>
		</p>
		
	</body>
	
</html>