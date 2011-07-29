<g:each in="${activities}" var="activity">

	<div class="aseWrapper">
		
		<div class="aseAvatarBlock">
			<img src="${createLink(controller:'profilePic',action:'thumbnail',id:activity.creator.userId)}" />
		</div>
		<div class="aseTitleBar"> <!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
			<a href="${createLink(controller:'user', action:'viewUser', params:[userId:activity.creator.userId])}">${activity.creator.fullName}</a>
		</div>
		<div class="activityStreamEntry"> 
			${activity.text}
		</div>
		<div class="aseClear" >
		</div>
		<div class="aseFooter" >
			${activity.dateCreated}
		</div>
	</div>

</g:each>
<div class="activityStreamFooter" style="clear:both;">
	<g:if test="${session.user}">
		<center><a href="#" id="loadMoreLink">Get More Events</a></center>
	</g:if>
</div>