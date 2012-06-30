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
	
	<div class="hero-unit">
		<g:if test="${session.user}">
		<h3>Streams</h3>
			<g:link controller="userStream" action="index" class="btn manageStreams"><i class="icon-cog"></i></g:link>
			<p>What are streams? Add some instructions.</p>
			<ul>
				<g:each var="stream" in="${sysDefinedStreams}">
					<li>
						<g:link controller="home" action="index" params="[streamId:stream.id]">${stream.name}</g:link>
					</li>
				</g:each>
				<g:each var="stream" in="${userDefinedStreams}">
					<li>
						<g:link controller="home" action="index" params="[streamId:stream.id]" >${stream.name}</g:link>
					</li>
				</g:each> 
		  	</ul>
		</g:if>
		<g:if test="${session.user}">
		<h3>Lists</h3>
			<g:link controller="userList" action="index" class="btn manageStreams"><i class="icon-cog"></i></g:link>
			<ul>
				<g:each var="list" in="${userLists}">
					<li>
						<g:link controller="userList" action="display" params="[listId:list.id]" >${list.name}</g:link>
					</li>
				</g:each>
		  	</ul>			
		</g:if>
		<g:if test="${session.user}">
		<h3>Groups</h3>
			<g:link controller="userGroup" action="index" class="manageStreams btn"><i class="icon-cog"></i></g:link>
			<ul>
				<g:each var="group" in="${userGroups}">
					<li>
						<g:link controller="userGroup" action="display" params="[groupId:group.id]" >${group.name}</g:link>
					</li>
				</g:each>
		  	</ul>				
		</g:if>
		<g:if test="${session.user}">
		<h3>Subscriptions</h3>
			<g:link controller="eventSubscription" action="index" class="btn manageStreams"><i class="icon-cog"></i></g:link>
			<ul style="margin-left:25px;margin-top:15px;">
				<g:each var="subscription" in="${eventSubscriptions}">
					<li>
						<g:link controller="eventSubscription" action="display" params="[subscriptionId:subscription.id]" >${subscription.name}</g:link>
					</li>
				</g:each>
		  	</ul>				
		</g:if>
	</div>	
</div>
