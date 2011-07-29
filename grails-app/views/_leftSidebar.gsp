<div style="padding-top:25px;">
	
	<div style="margin-left:130px;">
		<g:if test="${session.user}">  <!-- /profilePic/thumbnail?userId=${session.user.userId} -->
			<img style="float:left;" src="${createLink(controller:'profilePic',action:'thumbnail',id:session.user.userId)}" />
			<div style="font-weight:bold;margin-left:60px;padding-top:11px;">${session.user.fullName}</div>
		</g:if>
	</div>
	<div style="border-top: 3px solid #E2ECFC; margin-left:130px; height: 200px; clear:both;margin-top:45px;">
		<g:if test="${session.user}">
			Streams
		</g:if>
	</div>
	<div style="border-top: 3px solid #E2ECFC; margin-left:130px; height: 200px;">
		<g:if test="${session.user}">
			Groups
		</g:if>
	</div>
</div>
 				