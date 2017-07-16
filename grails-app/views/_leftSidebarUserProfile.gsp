<%@ page import="org.apache.shiro.SecurityUtils" %>

<div class="jumbotron">
	<shiro:authenticated>
		<img style="float: left;"
			src="${createLink(controller:'profilePic',action:'thumbnail',id:user.userId)}" />
		<h3>
			<a href="#">${user.fullName}</a>
		</h3>
	</shiro:authenticated>
	<p>
		<a class="btn btn-info selected"
			href="${createLink(controller:'user', action:'listFriends', id:user.id)}">Friends</a>
	</p>
	<p>
		<a class="btn btn-primary"
			href="${createLink(controller:'user', action:'listFollowers', id:user.id)}">Followers</a>
	</p>
	<p>
		<a class="btn btn-success"
			href="${createLink(controller:'user', action:'listIFollow')}">Followed Users</a>
	</p>
</div>