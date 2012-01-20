<html>
	
	<head>
		<title>Quoddy: The page for managing GROUPS</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<p />
		<h3>The page for managing GROUPS</h3>
		<g:link controller="userGroup" action="create" style="float:right;color:orange;margin-right:200px;margin-bottom:10px;">Create New Group</g:link>
		<p />
		<ul style="margin-left:25px;margin-top:40px;">
			<g:each var="group" in="${userGroups}">
				<li><g:link controller="userGroup" action="edit" id="${group.id}" >${group.name}</g:link> </li>
			</g:each>
		</ul>		
	</body>		
	</body>
	
</html>