<html>

	<head>
		<title>Quoddy: Import Users</title>
		<meta name="layout" content="admin_dialog" />
	</head>
	
	<body>
		Quoddy: Import Users
		<p />
		<div>
		<!--  search for goes here... -->
		<g:form controller="importUser" action="importUserSearch">
			<input type="text" name="queryString"/>
			<input type="submit" name="submitSearch" value="Search" />
		</g:form>
		</div>  
	
	</body>

</html>