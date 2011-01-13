<html>
	
	<head>
		<title>Quoddy: List Users</title>
		<meta name="layout" content="main"/>
        <nav:resources />
	</head>
	
	<body>
		<p />
		
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   </g:if>

		<ul>
			<g:each in="${users}" var="aUser">
			
				<!-- display discrete entries here -->
				<li>
					<g:link controller="user" action="viewUser" params="[userId:aUser.userId]"> ${aUser.fullName} </g:link>
				</li>
			</g:each>
		</ul>
	</body>
	
</html>