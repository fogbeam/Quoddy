<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img height="48" width="48" src="${createLinkTo(dir:'images', file:'task_report_hot.png')}"/>
	</div>
	<div class="aseTitleBar">
	
		<span class="aseTitleBarUserLink" >
		${item.streamObject.owningSubscription.name}

		</span> 
		
		<span class="aseTitleBarPermalink">
		<g:link controller="permalink" action="index" params="${[uuid:item.uuid]}" title="${formatDate(date:item.dateCreated)}">
				<g:formatDate date="${item.dateCreated}" type="datetime" style="SHORT" timeStyle="SHORT" />
		</g:link>
		</span>
		
		<div class="commentButtonBar">
      		<span class="plusOneButton" id="plusOneButton.${item.uuid}" name="plusOneButton.${item.uuid}" >
        		<a href="#" class="btn">+1</a>
      		</span>
      		<span class="shareButton" id="shareButton.${item.uuid}" name="shareButton.${item.uuid}" >
        		<a href="#" class="btn">Share</a>
      		</span>
      		<span class="showHideCommentsButton">
        		<a href="#" class="btn">Hide Comments</a>
      		</span>
    	</div>
		
	</div>	
	<!-- end aseTitleBar -->
	
	<div class="activityStreamEntry activitiUserTask"> 
		<h5>Activiti User Task</h5>
		<p>
		    ${item.streamObject.name}
			<br />
			${item.streamObject.description}
		</p>
		<ul>
			<li>Assignee: ${item.streamObject.assignee}</li>
			<li>Due Date: ${item.streamObject.dueDate}</li>
			<li>Priority: ${item.streamObject.priority}</li>
		</ul>
		<div class="activitiBPMButtons">
			<div class="btn-group">
				<button id="activityClaimBtn.${item.streamObject.uuid}" type="button" class="btn btn-info btn-small">Claim</button>
				<button id="activityCompleteBtn.${item.streamObject.uuid}" type="button" class="btn btn-info btn-small">Complete</button>
				<button id="activityTransferBtn.${item.streamObject.uuid}" type="button" class="btn btn-info btn-small">Transfer</button>
				<button id="activitySuspendBtn.${item.streamObject.uuid}" type="button" class="btn btn-info btn-small">Suspend</button>
				<button id="activityRefreshBtn.${item.streamObject.uuid}" type="button" class="btn btn-info btn-small">Refresh</button>
			</div>
		</div>
	</div>
	
	<!-- end activityStreamEntry -->
	
	<!-- begin aseClear -->
	<div class="aseClear"></div>
	<!--  end aseClear -->
	
	
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
			
			<form name="addCommentForm" id="addCommentForm" class="addCommentForm">
        		
        		<!-- <label>Add a comment</label> -->
				<input name="addCommentTextInput" id="addCommentTextInput"
					class="addCommentTextInput" type="textbox" value="Add a Comment"></input>
				<br />
				<input name="eventId" type="hidden" value="${item.id}" /> <input
					name="submitCommentBtn" id="submitCommentBtn"
					class="btn submitCommentBtn" style="display: none;" type="submit"
					value="Submit" /> <input name="cancelCommentBtn"
					id="cancelCommentBtn" class="btn cancelCommentBtn"
					style="display: none;" type="submit" value="Cancel" />
			</form>
			<!--  end addCommentForm -->
			
		</div>
		<!--  end commentBoxWrapper -->
	</div>
	
	<!--  end aseFooter -->
</div>