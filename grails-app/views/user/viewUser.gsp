<html>
	
	<head>
		<title>View User</title>
	</head>
	
	<body>
		<h1>View A User Here</h1>
		<p />
		
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   </g:if>

		<g:if test="${user != null}">
			
			<dl>
				<dt>Name:</dt>
				<dd>${user.fullName}</dd>
				
				<dt>Bio:</dt>
				<dd>${user.bio}</dd>
				
				<dt>Email:</dt>
				<dd>${user.email}</dd>
				
				<dt>Homepage:</dt>
				<dd>${user.homepage}</dd>
			
			</dl>

			<!-- Note to Self: this should probably be an async call instead of requiring a full view refresh -->
			<g:link controller="user" action="addToFriends" params="[userId:user.userId]">add to friends</g:link>
						
		</g:if>
		

	</body>
	
</html>