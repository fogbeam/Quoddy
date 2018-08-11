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
		<g:if test="${session.enable_self_registration == true}">
			<li><a href="/user/create">Register</a></li>
		</g:if>
		
		<li><a href="/login">Login</a></li>
		<sec:ifLoggedIn>
			<li><a href="/localLogin/logout">Logout</a></li>
			<li><a href="/user/editAccount">Edit Account Info</a></li>
			<li><a href="/user/editProfile">Edit Profile</a></li>
			<li><a href="/status/listUpdates">List My Updates</a></li>
			<li><a href="/user/list">List All Users</a></li>
			<li><a href="/user/listFriends">List Friends</a></li>
			<li><a href="/user/listFollowers">List Followers</a></li>
			<li><a href="/user/listIFollow">List People I Follow</a></li>
			<li><a href="/search/searchPeople">Search People</a></li>
			<li><a href="/search/searchFriends">Search Friends</a></li>
			<li><a href="/search/searchIFollow">Search People I Follow</a></li>
			<li><a href="/user/listOpenFriendRequests">List Pending Friend Requests</a></li>
		</sec:ifLoggedIn>			
	</ul>
</div>
