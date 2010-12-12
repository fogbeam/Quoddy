<html>
	
	<head>
		<title>People I Follow Search Results</title>
	</head>
	
	<body>
		<p />
		Matching People I Follow:
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