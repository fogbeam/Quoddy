<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img src="${createLinkTo(dir:'images', file:'flavour-icons/ical.png')}"/>
	</div>

<!-- begin aseTitleBar -->
>·<div class="aseTitleBar">
>·>·<!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
>·>·<span class="aseTitleBarUserLink"> <a href="${createLink(controller:'activityStream', action:'viewUserStream', params:[userId:item.owner.userId])}">
>·>·>·>·${item.owner.fullName}
>·>·</a>
>·>·</span> <span class="aseTitleBarPermalink"> <a href="#" title="${formatDate(date:item.dateCreated)}"><g:formatDate date="${item.dateCreated}" type="datetime" style="SHORT" timeStyle="SHORT" /></a>
  </span>
    <div class="commentButtonBar">
      <span class="plusOneButton" id="plusOneButton.${item.uuid}" name="plusOneButton.${item.uuid}" >
        <a href="#" class="btn">+1</a>
      </span>
      <span class="shareButton" id="shareButton.${item.uuid}" name="shareButton.${item.uuid}" >
        <a href="#" class="btn">Share</a>
      </span>
      <span class="showHideCommentsButton">
        <a href="#" class="btn">Show Comments</a>
      </span>
    </div>
  </div>
<!--  end aseTitleBar -->



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
        <label>Add a comment</label>
				<input name="addCommentTextInput" id="addCommentTextInput"
					class="addCommentTextInput" type="textbox" value="Add a Comment"></input>
				<input name="eventId" type="hidden" value="${item.id}" /> <input
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