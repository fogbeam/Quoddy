<html>
	
	<head>
		<title>Quoddy: List People I Follow</title>
        <meta name="layout" content="main"/>
	</head>
	
	<body>
		<div class="jumbotron span6">
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
					<g:link controller="user" action="viewUser" params="[userId:followee.userId]">${followee.fullName}</g:link>  -- <span><g:link controller="user" action="unFollow" params="[unfollowId:followee.userId]">unfollow</g:link></span>
				</li>
			</g:each>
		</ul>
	</body>
	
</html>