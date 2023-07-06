<html>
	
	<head>
		<title>Quoddy: Open Friend Requests</title>
        <meta name="layout" content="main"/>
	</head>
	
	<body>
	<div class="jumbotron span6">     
		<h2>Friend Requests</h2>
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   	</g:if>
		
		<p />
		<span>Pending friend request from:</span>
		<p />
		<ul>
			<g:each in="${openFriendRequests}" var="friendRequest">
			
				<!-- display discrete entries here -->
				<li>
					<span>${friendRequest.unconfirmedFriend.fullName}</span>: 
					<span><g:link controller="user" action="confirmFriend" params="[confirmId:friendRequest.unconfirmedFriend.userId]">confirm</g:link></span>
					<span> <g:link controller="user" action="deleteFriendRequest" params="[confirmId:friendRequest.unconfirmedFriend.userId]">delete</g:link></span>
				</li>
			</g:each>
		</ul>
		</div>
	</body>

</html>