<html>
	
	<head>
		<title>Quoddy: List Friends</title>
        <meta name="layout" content="main"/>
        <nav:resources />
	</head>
	
	<body>
		<p />
		<h2>My Friends</h2>
		<p />
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   </g:if>

		<ul>
			<g:each in="${friends}" var="friend">
			
				<!-- display discrete entries here -->
				<li>
					<g:link controller="user" action="viewUser" params="[userId:friend.userId]">${friend.fullName}</g:link>
				</li>
			</g:each>
		</ul>
	</body>
	
</html>