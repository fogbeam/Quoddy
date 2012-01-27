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
		<div>
			
			<div style="clear:both;float:left;margin-left:100px;margin-top:50px;">
				<span>Groups I'm In</span>
				<ul style="margin-left:25px;margin-top:40px;">
					<g:each var="group" in="${userMembershipGroups}">
						<li><g:link controller="userGroup" action="edit" id="${group.id}" >${group.name}</g:link> </li>
					</g:each>
				</ul>
			</div>	
			
			<div style="clear:both;float:right;margin-right:250px;margin-top:-60px;">
				<span>Groups I Own</span>
				<ul style="margin-left:25px;margin-top:40px;">
					<g:each var="group" in="${userOwnedGroups}">
						<li><g:link controller="userGroup" action="edit" id="${group.id}" >${group.name}</g:link> </li>
					</g:each>
				</ul>
			</div>
			
		</div>	
	</body>
</html>