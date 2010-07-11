<html>
	<head>
		<title>Welcome to Project Poe</title>
	</head>
	<body>
		<h1>Welcome to Project Poe</h1>
		
		<p />
		
		Stuff you might want to do here:
		<br />
		<ul>
			<li><a href="/poe1/user/create">Register</a></li>
			<li><a href="/poe1/login">Login</a></li>
			<g:if test="${session.user != null}">
				<li><a href="/poe1/login/logout">Logout</a></li>
				<li><a href="#">Edit My Profile</a></li>
				<li><a href="/poe1/user/list">List All Users</a></li>
				<li><a href="/poe1/search">Search People</a></li>
				<!-- <li><a href="/poe1/user/addToFriends">Add Friend</a></li> -->
				<li><a href="/poe1/user/listFriends">List Friends</a></li>
				<li><a href="/poe1/search/searchFriends">Search Friends</a></li>
			</g:if>			
		</ul>
		<p />
		
		<g:if test="${session.user != null}">
			<g:form controller="status" action="updateStatus" >
				<input type="text" name="statusText" />
				<input type="submit" value="Update Status" />
			</g:form>
			<br />
			
			<dl>
				<dt>Status:</dt>
				
				<g:if test="${user != null}">
					<dd><div class="myStatus">${user.currentStatus.text}</div></dd>
				</g:if>
			</dl>
			<hr />
			<h2>Activity Stream</h2>
			<p />
			<g:if test="${activities != null}">
			
				<div id="activityStream">
					<dl>
						<g:each in="${activities}" var="activity">
							<dd><span class="activityStreamEntry">${activity.text}</span></dd>
						</g:each>
					</dl>
				</div>
			
			</g:if>
		</g:if>
	</body>
</html>