<div style="padding-top:30px;">
     <g:form controller="search" action="doSearch" method="GET">
          <input name="queryString" type="text" />
          <input type="submit" value="Search" />
     </g:form>
</div>
<div style="padding-top:25px;">
	Stuff you might want to do here:
	<br />
	<ul>
		<li><a href="/quoddy2/user/create">Register</a></li>
		<li><a href="/quoddy2/login">Login</a></li>
		<g:if test="${session.user != null}">
			<li><a href="/quoddy2/login/logout">Logout</a></li>
			<li><a href="/quoddy2/user/editAccount">Edit Account Info</a></li>
			<li><a href="/quoddy2/user/editProfile">Edit Profile</a></li>
			<li><a href="/quoddy2/status/listUpdates">List My Updates</a></li>
			<li><a href="/quoddy2/user/list">List All Users</a></li>
			<li><a href="/quoddy2/user/listFriends">List Friends</a></li>
			<li><a href="/quoddy2/user/listFollowers">List Followers</a></li>
			<li><a href="/quoddy2/user/listIFollow">List People I Follow</a></li>
			<li><a href="/quoddy2/search">Search People</a></li>
			<li><a href="/quoddy2/search/searchFriends">Search Friends</a></li>
			<li><a href="/quoddy2/search/searchIFollow">Search People I Follow</a></li>
			<li><a href="/quoddy2/user/listOpenFriendRequests">List Pending Friend Requests</a></li>
		</g:if>			
	</ul>
</div>
