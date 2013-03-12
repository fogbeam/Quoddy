<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img src="${createLink(controller:'profilePic',action:'thumbnail',id:item.owner.userId)}" />
	</div>
	<div class="aseTitleBar"> <!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
		<a href="${createLink(controller:'user', action:'viewUser', params:[userId:item.owner.userId])}">${item.owner.fullName}</a>
	</div>
	<div class="activityStreamEntry"> 
		${item.content}
	</div>
	<div class="aseClear" >
	</div>
	<div class="aseFooter" >
		<g:formatDate date="${item.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"/>
	</div>
	<div class="aseClear" />
	<div class="aseFooter">
		<label>+1</label>
		<label>Share</label>
	</div>
	<div class="aseClear" />
	<div class="aseFooter">
		<div class="commentBoxWrapper" style="background-color:#F8F8F8;">
		
			<!--  render comments on the Event here -->
			<g:each in="${item.comments}" var="comment">
				<div>
					<span class="commentUserName">${comment.creator.fullName}</span>&nbsp;&nbsp;
					<span class="commentDateTime">${comment.dateCreated}</span>
					<br />
					<span class="commentText">${comment.text}</span>
					<br />
				</div>
			</g:each>
		
			<form name="addCommentForm" id="addCommentForm" class="addCommentForm">
				<input name="addCommentTextInput" id="addCommentTextInput" class="addCommentTextInput" type="textbox" value="Add a Comment" ></input>				<br />
				<input name="eventId" type="hidden" value="${item.id}" />
				<input name="submitCommentBtn" id="submitCommentBtn" class="submitCommentBtn" style="display:none;" type="submit" value="Submit" />
				<input name="cancelCommentBtn" id="cancelCommentBtn" class="cancelCommentBtn" style="display:none;" type="submit" value="Cancel" />
			</form>
		</div>
	</div>
</div>