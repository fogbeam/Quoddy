<html>

	<head>
		<title>Quoddy: Import Users</title>
		<meta name="layout" content="admin_dialog" />
	</head>
	
	<body>
		<div class="bodyContent" style="padding-left:100px;padding-top:40px;">
			<h2>Import Users</h2>
			<p />
			<div>
			<!--  search for goes here... -->
			<g:form controller="importUser" action="importUserSearch">
				<input type="text" name="queryString"/>
				<input type="submit" name="submitSearch" value="Search" />
			</g:form>
			</div>  
		</div>
	</body>

</html>