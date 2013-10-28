<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img
			src="${createLink(controller:'profilePic',action:'thumbnail',id:item.owner.userId)}" />
	</div>
	<div class="aseTitleBar">
	
		<span class="aseTitleBarUserLink"> <a
			href="${createLink(controller:'activityStream', action:'viewUserStream', params:[userId:item.owner.userId])}">
				${item.owner.fullName}
		</a> 
		<span class="aseTitleBarPermalink"> <a href="#"
			title="${formatDate(date:item.dateCreated)}"><g:formatDate
					date="${item.dateCreated}" type="datetime" style="SHORT"
					timeStyle="SHORT" /></a>
		</span>
	</div>	
	
	
	<div class="activityStreamEntry remoteActivityStreamEntry"> 
		<p>
		    ${item.name}
		</p>
		<p>
			${item.content}
		</p>
		<hr />
	</div>
	<!-- begin aseClear -->
	<div class="aseClear"></div>
	<!--  end aseClear -->
	
	<!-- begin aseFooter -->
	<div class="aseFooter">
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
	<!--  end aseFooter -->
	
	<!-- begin aseClear -->
	<div class="aseClear" ></div>
	<!-- end aseClear -->
	
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