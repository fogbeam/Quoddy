
<div id="leftSidebar" class="jumbotron" style="padding-top:10px; padding-left:20px;padding-right:10px;max-width:180px;">

	<img id="profilePic" src="${createLink(controller:'profilePic',action:'thumbnail',id:sec.loggedInUserInfo(field: 'userId'))}" />
	<p />
	<a  id="userFullName" href="${createLink(controller:'user', action:'viewUser', params: [userId:sec.loggedInUserInfo(field: 'userId') ] ) }">
		<sec:loggedInUserInfo field='fullName'/>
	</a>
	<p>
		<a id="listAllFriends" style="min-width:120px;" class="btn btn-info selected" href="${createLink(controller:'user', action:'listFriends')}">All Friends</a>
	</p>
	<p>
		<a id="listFollowers" style="min-width:120px;" class="btn btn-primary" href="${createLink(controller:'user', action:'listFollowers')}">All Followers</a>
	</p>
	<p>
		<a id="listFollowing" style="min-width:120px;" class="btn btn-success" href="${createLink(controller:'user', action:'listIFollow')}">People I follow</a>
	</p>
	<p>
		<a id="listGroups" style="min-width:120px;" class="btn btn-warning" href="${createLink(controller:'userGroup', action:'list')}">All Groups</a>
	</p>
	<p>
		<a id="listAllUsers" style="min-width:120px;" class="btn btn-default" href="${createLink(controller:'user', action:'list')}">All Users</a>
	</p>
	
</div>

