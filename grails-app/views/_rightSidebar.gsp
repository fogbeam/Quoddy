<div style="padding-top:25px;">
	Stuff you might want to do here:
	<br />
	<ul>
		<g:if test="${session.enable_self_registration == true}">
				<!-- /user/create -->
			<li><a href="${createLink(controller:'user', action:'create')}">Register</a></li>
		</g:if>
		
		<li><a href="${createLink(controller:'login') }">Login</a></li>
		<g:if test="${session.user != null}">
			<li><a href="${createLink(controller:'login', action:'logout')}">Logout</a></li>
			<li><a href="${createLink(controller:'user', action:'editAccount')}">Edit Account Info</a></li>
			<li><a href="${createLink(controller:'user', action:'editProfile')}">Edit Profile</a></li>
			<li><a href="${createLink(controller:'status', action:'listUpdates')}">List My Updates</a></li>
			<li><a href="${createLink(controller:'user', action:'list')}">List All Users</a></li>
			<li><a href="${createLink(controller:'user', action:'listFriends')}">List Friends</a></li>
			<li><a href="${createLink(controller:'user', action:'listFollowers')}">List Followers</a></li>
			<li><a href="${createLink(controller:'user', action:'listIFollow')}">List People I Follow</a></li>
			<li><a href="${createLink(controller:'search', action:'searchPeople')}">Search People</a></li>
			<li><a href="${createLink(controller:'search', action:'searchFriends')}">Search Friends</a></li>
			<li><a href="${createLink(controller:'search', action:'searchIFollow')}">Search People I Follow</a></li>
			<li><a href="${createLink(controller:'user', action:'listOpenFriendRequests')}">List Pending Friend Requests</a></li>
		</g:if>			
	</ul>
</div>