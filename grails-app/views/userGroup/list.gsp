<html>
	
	<head>
		<title>Quoddy: List GROUPS</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>List GROUPS</h3>
		<p />
		<ul style="margin-left:25px;margin-top:40px;">
			<g:each var="group" in="${allGroups}">
				<li><g:link controller="userGroup" action="display" params="[groupId:group.id]" >${group.name}</g:link> </li>
			</g:each>
		</ul>	
	</body>
</html>