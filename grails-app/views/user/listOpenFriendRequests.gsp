<html>
	
	<head>
		<title>Friend Requests</title>
	</head>
	
	<body>
		<p />
		<h2>Friend Requests</h2>
		<p />
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   	</g:if>
		
		<p />
		Pending friend request from:
		<p />
		<ul>
			<g:each in="${openFriendRequests}" var="friendRequest">
			
				<!-- display discrete entries here -->
				<li>
					<span>${friendRequest.unconfirmedFriend.fullName}</span>: <span><g:link controller="user" action="confirmFriend" params="[confirmId:friendRequest.unconfirmedFriend.userId]">confirm</g:link>
				</li>
			</g:each>
		</ul>
	</body>

</html>