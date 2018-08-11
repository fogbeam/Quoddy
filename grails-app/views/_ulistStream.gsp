<g:each in="${activities}" var="activity">

	<div class="aseWrapper">
		
		<div class="aseAvatarBlock">
			<img src="${createLink(controller:'profilePic',action:'thumbnail',id:activity.userActor.userId)}" />
		</div>
		<div class="aseTitleBar">
			<a href="${createLink(controller:'user', action:'viewUser', params:[userId:activity.userActor.userId])}">${activity.userActor.fullName}</a>
		</div>
		<div class="activityStreamEntry ulistStreamActivity"> 
			${activity.content}
		</div>
		<div class="aseClear" >
		</div>
		<div class="aseFooter" >
			<g:formatDate date="${activity.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"/>
		</div>
	</div>
</g:each>
<div class="activityStreamFooter" style="clear:both;">
	<sec:ifLoggedIn>
		<center><a href="#" id="loadMoreLink">Get More Events</a></center>
	</sec:ifLoggedIn>
</div>