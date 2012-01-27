<html>
	
	<head>
		<title>Quoddy: The page for managing LISTS</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<p />
		<h3>The page for managing LISTS</h3>
		<g:link controller="userList" action="create" style="float:right;color:orange;margin-right:200px;margin-bottom:10px;">Create New List</g:link>
		<p />
		<ul style="margin-left:25px;margin-top:40px;">
			<g:each var="list" in="${userLists}">
				<li><g:link controller="userList" action="editWizard" event="start" params="[listId:list.id]" >${list.name}</g:link> </li>
			</g:each>
		</ul>		
	</body>
	
</html>