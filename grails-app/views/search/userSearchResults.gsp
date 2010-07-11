<html>
	
	<head>
		<title>User Search Results Go Here</title>
	</head>
	
	<body>
		<p />
		<ul>
			<g:each in="${allUsers}" var="user">
			
				<!-- display discrete entries here -->
				<li>
					<g:link controller="user" action="viewUser" params="[userId:user.userId]">${user.fullName}</g:link>
				</li>
			</g:each>
		</ul>
	</body>
	
</html>