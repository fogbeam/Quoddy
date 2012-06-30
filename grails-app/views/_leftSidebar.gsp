<div class="">
    <g:if test="${session.user}">  <!-- /profilePic/thumbnail?userId=${session.user.userId} -->
      <img style="float:left;" src="${createLink(controller:'profilePic',action:'thumbnail',id:session.user.userId)}" />
      <h3><a href="${createLink(controller:'status', action:'listUpdates')}">${session.user.fullName}</a></h3>
    </g:if>
		<p>Manage Connections:</p>
    <p><a class="btn btn-info selected" href="${createLink(controller:'user', action:'listFriends')}">All Friends</a></p>
		<p><a class="btn btn-primary" href="${createLink(controller:'user', action:'listFollowers')}">All Followers</a></p>
		<p><a class="btn btn-success" href="${createLink(controller:'user', action:'listIFollow')}">People I follow</a></p>
		<p><a class="btn btn-inverse" href="${createLink(controller:'userGroup', action:'list')}">All Groups</a></p>
		<p><a class="btn" href="${createLink(controller:'user', action:'list')}">All Users</a></p>
</div>   