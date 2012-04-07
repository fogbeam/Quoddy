<div class="hero-unit">
    <g:if test="${session.user}">  <!-- /profilePic/thumbnail?userId=${session.user.userId} -->
      <img style="float:left;" src="${createLink(controller:'profilePic',action:'thumbnail',id:session.user.userId)}" />
      <h3>${session.user.fullName} <span><a href="${createLink(controller:'status', action:'listUpdates')}">My updates &rarr;</a></span></h3>
    </g:if>
    <p><ul style="margin-left:16px; margin-bottom:24px;"><li>Are there any attributes we'd like to expose here?</li></ul>
		<p>Manage Connections:</p>
    <p><a class="btn btn-info selected" href="${createLink(controller:'user', action:'listFriends')}">All Friends</a></p>
		<p><a class="btn btn-primary" href="${createLink(controller:'user', action:'listFollowers')}">All Followers</a></p>
		<p><a class="btn btn-success" href="${createLink(controller:'user', action:'listIFollow')}">People I follow</a></p>
		<p><a class="btn btn-inverse" href="${createLink(controller:'userGroup', action:'list')}">All Groups</a></p>
		<p><a class="btn" href="${createLink(controller:'user', action:'list')}">All Users</a></p>
</div>   
	
	<div style="border-top: 3px solid #E2ECFC; margin-left:130px; height: 200px; clear:both;margin-top:45px;">
		<g:if test="${session.user}">
			<g:link controller="userStream" action="index" >Streams</g:link>
			<ul style="margin-left:25px;margin-top:15px;">
				<g:each var="stream" in="${sysDefinedStreams}">
					<li>
						<g:link controller="home" action="index" params="[streamId:stream.id]" >${stream.name}</g:link>
					</li>
				</g:each>
				<g:each var="stream" in="${userDefinedStreams}">
					<li>
						<g:link controller="home" action="index" params="[streamId:stream.id]" >${stream.name}</g:link>
					</li>
				</g:each> 
		  	</ul>
		</g:if>
	</div>
	<div style="border-top: 3px solid #E2ECFC; margin-left:130px; height: 200px;">
		<g:if test="${session.user}">
			<g:link controller="userList" action="index" >Lists</g:link>
			<ul style="margin-left:25px;margin-top:15px;">
				<g:each var="list" in="${userLists}">
					<li>
						<g:link controller="userList" action="display" params="[listId:list.id]" >${list.name}</g:link>
					</li>
				</g:each>
		  	</ul>			
		</g:if>
	</div>
	
	<div style="border-top: 3px solid #E2ECFC; margin-left:130px; height: 200px;">
		<g:if test="${session.user}">
			<g:link controller="userGroup" action="index" >Groups</g:link>
			<ul style="margin-left:25px;margin-top:15px;">
				<g:each var="group" in="${userGroups}">
					<li>
						<g:link controller="userGroup" action="display" params="[groupId:group.id]" >${group.name}</g:link>
					</li>
				</g:each>
		  	</ul>				
		</g:if>
	</div>
	
	<div style="border-top: 3px solid #E2ECFC; margin-left:130px; height: 200px;">
		<g:if test="${session.user}">
			<g:link controller="eventSubscription" action="index" >Subscriptions</g:link>
			<ul style="margin-left:25px;margin-top:15px;">
				<g:each var="subscription" in="${eventSubscriptions}">
					<li>
						<g:link controller="eventSubscription" action="display" params="[eventSubscriptionId:subscription.id]" >${subscription.name}</g:link>
					</li>
				</g:each>
		  	</ul>				
		</g:if>
	</div>	
	
</div>
 				