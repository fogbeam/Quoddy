<html>
	
	<head>
		<title>People I Follow</title>
	</head>
	
	<body>
		<p />
		<h2>People I Follow</h2>
		<p />
		
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   </g:if>

		<ul>
			<g:each in="${ifollow}" var="followee">
			
				<!-- display discrete entries here -->
				<li>
					<g:link controller="user" action="viewUser" params="[userId:followee.userId]">${followee.fullName}</g:link>
				</li>
			</g:each>
		</ul>
	</body>
	
</html>