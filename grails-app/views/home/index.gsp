<html>
	<head>
		<title>Welcome to Quoddy</title>
	</head>
	<body>
		<h1>Welcome to Project Quoddy</h1>
		
		<p />
             <g:hasErrors>
                 <div class="errors">
                    <g:renderErrors bean="${flash.user}" as="list" />
                 </div>
             </g:hasErrors>		
		<p />
		Stuff you might want to do here:
		<br />
		<ul>
			<li><a href="/quoddy2/user/create">Register</a></li>
			<li><a href="/quoddy2/login">Login</a></li>
			<g:if test="${session.user != null}">
				<li><a href="/quoddy2/login/logout">Logout</a></li>
				<li><a href="/quoddy2/user/editAccount">Edit Account Info</a></li>
				<li><a href="/quoddy2/user/editProfile">Edit Profile</a></li>
				<li><a href="/quoddy2/user/list">List All Users</a></li>
				<li><a href="/quoddy2/user/listFriends">List Friends</a></li>
				<li><a href="/quoddy2/user/listIFollow">List People I Follow</a></li>
				<li><a href="/quoddy2/search">Search People</a></li>
				<li><a href="/quoddy2/search/searchFriends">Search Friends</a></li>
				<li><a href="/quoddy2/search/searchIFollow">Search People I Follow</a></li>
				<li><a href="/quoddy2/user/listOpenFriendRequests">List Pending Friend Requests</a></li>
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
					<dd><div class="myStatus">${user?.currentStatus?.text}</div></dd>
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