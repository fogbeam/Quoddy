<%@ page import="org.apache.shiro.SecurityUtils" %>

<div class="jumbotron" style="padding-top:10px; padding-left:20px;padding-right:10px;max-width:180px;">

	<img src="${createLink(controller:'profilePic',action:'thumbnail',id:SecurityUtils.subject.principal.userId)}" />
	<p />
	<a href="${createLink(controller:'user', action:'viewUser', params:[userId:SecurityUtils.subject.principal.userId])}">
		<shiro:principal property="fullName"/>
	</a>
	<p>
		<a style="min-width:120px;" class="btn btn-info selected" href="${createLink(controller:'user', action:'listFriends')}">All Friends</a>
	</p>
	<p>
		<a style="min-width:120px;" class="btn btn-primary" href="${createLink(controller:'user', action:'listFollowers')}">All Followers</a>
	</p>
	<p>
		<a style="min-width:120px;" class="btn btn-success" href="${createLink(controller:'user', action:'listIFollow')}">People I follow</a>
	</p>
	<p>
		<a style="min-width:120px;" class="btn btn-warning" href="${createLink(controller:'userGroup', action:'list')}">All Groups</a>
	</p>
	<p>
		<a style="min-width:120px;" class="btn btn-default" href="${createLink(controller:'user', action:'list')}">All Users</a>
	</p>
	
</div>

