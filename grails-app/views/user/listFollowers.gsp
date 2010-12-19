<html>
	
	<head>
		<title>Quoddy: List Followers</title>
        <meta name="layout" content="main"/>
        <nav:resources />
	</head>
	
	<body>
		<p />
		<h2>My Followers</h2>
		<p />
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   </g:if>

		<ul>
			<g:each in="${followers}" var="follower">
			
				<!-- display discrete entries here -->
				<li>
					<g:link controller="user" action="viewUser" params="[userId:follower.userId]">${follower.fullName}</g:link>
				</li>
			</g:each>
		</ul>
	</body>
	
</html>