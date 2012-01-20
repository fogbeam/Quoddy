<div style="padding-top:25px;">
	
	<div style="margin-left:130px;">
		<g:if test="${session.user}">  <!-- /profilePic/thumbnail?userId=${session.user.userId} -->
			<img style="float:left;" src="${createLink(controller:'profilePic',action:'thumbnail',id:session.user.userId)}" />
			<div style="font-weight:bold;margin-left:60px;padding-top:11px;">${session.user.fullName}</div>
		</g:if>
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
						<g:link controller="home" action="index" params="[listId:list.id]" >${list.name}</g:link>
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
						<g:link controller="home" action="index" params="[groupId:group.id]" >${group.name}</g:link>
					</li>
				</g:each>
		  	</ul>				
		</g:if>
	</div>
</div>
 				