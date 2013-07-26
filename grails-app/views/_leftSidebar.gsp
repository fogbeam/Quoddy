<%@ page import="org.apache.shiro.SecurityUtils" %>

<div class="hero-unit">
	<shiro:authenticated>
		<img style="float: left;"
			src="${createLink(controller:'profilePic',action:'thumbnail',id:SecurityUtils.subject.principal.userId)}" />
		<h3>
			<a href="${createLink(controller:'status', action:'listUpdates')}">
				<shiro:principal property="fullName"/>
			</a>
		</h3>
	</shiro:authenticated>
	<p>
		<a class="btn btn-info selected"
			href="${createLink(controller:'user', action:'listFriends')}">All
			Friends</a>
	</p>
	<p>
		<a class="btn btn-primary"
			href="${createLink(controller:'user', action:'listFollowers')}">All
			Followers</a>
	</p>
	<p>
		<a class="btn btn-success"
			href="${createLink(controller:'user', action:'listIFollow')}">People
			I follow</a>
	</p>
	<p>
		<a class="btn btn-inverse"
			href="${createLink(controller:'userGroup', action:'list')}">All
			Groups</a>
	</p>
	<p>
		<a class="btn" href="${createLink(controller:'user', action:'list')}">All
			Users</a>
	</p>
</div>