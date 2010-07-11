<html>
	
	<head>
		<title></title>
	</head>
	
	<body>
		<p />
		
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   </g:if>

		<g:if test="${user != null}">
			<ul>
				<g:each in="${user.friends}" var="friend">
				
					<!-- display discrete entries here -->
					<li>
						<g:link controller="user" action="viewUser" params="[userId:friend.userId]">${friend.fullName}</g:link>
					</li>
				</g:each>
			</ul>
		</g:if>
	</body>
	
</html>