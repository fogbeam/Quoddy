<g:each in="${activities}" var="activity">

	<div class="aseWrapper">
		
		<div class="aseAvatarBlock">
			<img src="${createLink(controller:'profilePic',action:'thumbnail',id:activity.owner.userId)}" />
		</div>
		<div class="aseTitleBar"> <!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
			<a href="${createLink(controller:'user', action:'viewUser', params:[userId:activity.owner.userId])}">${activity.owner.fullName}</a>
		</div>
		<div class="activityStreamEntry ugroupStreamActivity"> 
			${activity.content}
		</div>
		<div class="aseClear" >
		</div>
		<div class="aseFooter" >
			<g:formatDate date="${activity.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"/>
		</div>
	</div>
</g:each>