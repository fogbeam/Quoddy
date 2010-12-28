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
		<li><a href="/user/create">Register</a></li>
		<li><a href="/login">Login</a></li>
		<g:if test="${session.user != null}">
			<li><a href="/login/logout">Logout</a></li>
			<li><a href="/user/editAccount">Edit Account Info</a></li>
			<li><a href="/user/editProfile">Edit Profile</a></li>
			<li><a href="/status/listUpdates">List My Updates</a></li>
			<li><a href="/user/list">List All Users</a></li>
			<li><a href="/user/listFriends">List Friends</a></li>
			<li><a href="/user/listFollowers">List Followers</a></li>
			<li><a href="/user/listIFollow">List People I Follow</a></li>
			<li><a href="/search">Search People</a></li>
			<li><a href="/search/searchFriends">Search Friends</a></li>
			<li><a href="/search/searchIFollow">Search People I Follow</a></li>
			<li><a href="/user/listOpenFriendRequests">List Pending Friend Requests</a></li>
		</g:if>			
	</ul>
</div>
