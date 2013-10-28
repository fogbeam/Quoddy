<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img src="${createLinkTo(dir:'images', file:'flavour-icons/ical.png')}"/>
	</div>
	<div class="aseTitleBar"> <!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
		<span class="aseTitleBarUserLink">	
		<a href="${createLink(controller:'calendar', action:'display', params:[calendarFeedId:item.streamObject.owningSubscription.id])}">${item.streamObject.owningSubscription.name}</a>
		</span>
		<span classs="aseTitleBarPermalink">
		<g:formatDate date="${item.streamObject.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"/>
		</span>
	
	
	</div>
	
	
	<div class="activityStreamEntry calendarEntry"> 
		<font color="red">CALENDAR EVENT</font>
		<p>
			${item.streamObject.description}
		</p>
		<ul>
			<li>Start Date: ${item.streamObject.startDate}</li>
			<li>End Date: ${item.streamObject.endDate}</li>
			<li><a href="${item.streamObject.url}">${item.streamObject.url}</a></li>
		</ul>
	</div>
	<!-- end activityStreamEntry -->
	
	<div class="aseClear" >
	</div>
	
	<div class="aseFooter" >
			<span class="plusOneButton" id="plusOneButton.${item.uuid}" name="plusOneButton.${item.uuid}" >
			<a href="#">+1</a>
		</span> 
		<span class="shareButton" id="shareButton.${item.uuid}" name="shareButton.${item.uuid}" >
			<a href="#">Share</a>
		</span>
		<span class="showHideCommentsButton"> 
			<a href="#">Show Comments</a>
		</span>	
	</div>
	
	<div class="aseClear" >
	</div>
	
	<!-- begin aseFooter -->
	<div class="aseFooter">
	
		<!-- begin commentboxWrapper -->
		<div class="commentBoxWrapper">
		
			<!-- begin commentsArea -->
			<div id="commentsArea" class="commentsArea">
				<!--  render comments on the Event here -->
				<g:render template="/renderComments" var="comments"
					bean="${item.streamObject.comments}" />
			</div>
			<!-- end commentsArea -->
			
			<form name="addCommentForm" id="addCommentForm"
				class="addCommentForm">
				<input name="addCommentTextInput" id="addCommentTextInput"
					class="addCommentTextInput" type="textbox" value="Add a Comment"></input>
				<br /> <input name="eventId" type="hidden" value="${item.id}" /> <input
					name="submitCommentBtn" id="submitCommentBtn"
					class="submitCommentBtn" style="display: none;" type="submit"
					value="Submit" /> <input name="cancelCommentBtn"
					id="cancelCommentBtn" class="cancelCommentBtn"
					style="display: none;" type="submit" value="Cancel" />
			</form>
		</div>
		<!--  end commentBoxWrapper -->
	</div>
	<!--  end aseFooter -->
	
</div>