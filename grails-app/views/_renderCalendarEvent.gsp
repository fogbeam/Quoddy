<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img src="${createLinkTo(dir:'images', file:'flavour-icons/ical.png')}"/>
	</div>
	<div class="aseTitleBar"> <!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
		<a href="${createLink(controller:'calendar', action:'display', params:[calendarFeedId:item.owningFeed.id])}">${item.owningFeed.name}</a>
	</div>
	<div class="activityStreamEntry"> 
		<font color="red">CALENDAR EVENT</font>
		<p>
			${item.description}
		</p>
		<ul>
			<li>Start Date: ${item.startDate}</li>
			<li>End Date: ${item.endDate}</li>
			<li><a href="${item.url}">${item.url}</a></li>
		</ul>
	</div>
	<div class="aseClear" >
	</div>
	<div class="aseFooter" >
		<g:formatDate date="${item.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"/>
	</div>
</div>