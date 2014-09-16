<div class="aseWrapper">
	

	<div class="aseTitleBar">

		<div class="aseAvatarBlock">
			<img height="48" width="48" src="${createLinkTo(dir:'images', file:'task_report_hot.png')}"/>
		</div>
	
		<span class="aseTitleBarUserLink" >
		${item.streamObject.owningSubscription.name}

		</span> 
		
		<span class="aseTitleBarPermalink">
		<g:link controller="permalink" action="index" params="${[uuid:item.uuid]}" title="${formatDate(date:item.dateCreated)}">
				<g:formatDate date="${item.dateCreated}" type="datetime" style="SHORT" timeStyle="SHORT" />
		</g:link>
		</span>
		
		<div class="commentButtonBar">
		
		    <!-- if super-secret fogbeam-dev mode is on, render the "delete" button -->
        	<g:if test="CH.config.fogbeam.devmode">
        		<span class="xButton" id="xButton.${item.uuid}" name="xButton.${item.uuid}" >
        			<a href="#" class="btn">X</a>
      			</span>	
        	</g:if>
		
      		<span class="plusOneButton" id="plusOneButton.${item.uuid}" name="plusOneButton.${item.uuid}" >
        		<a href="#" class="btn">+1</a>
      		</span>
      		<span class="shareButton" id="shareButton.${item.uuid}" name="shareButton.${item.uuid}" >
        		<a href="#" class="btn">Share</a>
      		</span>
      		<span class="showHideCommentsButton">
        		
      			<!-- change the initial state of this button
      			depending on whether or not there are any comments yet. 
      			If there are no comments, render it with text "no comments"
      			(and maybe make the button inactive)?
      			-->
      			
      			<g:if test="${item.streamObject.comments.size() > 0}">
        			<a href="#" class="btn">Hide Comments</a>
      			</g:if>
      			<g:else>
      				<a href="#" class="btn">No Comments</a>
      			</g:else>


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
			<li>Assignee: <g:if test="${item.streamObject.assignee != null && !item.streamObject.assignee.equals("null")}">${item.streamObject.assignee}</g:if></li>
			<li>Due Date: <g:if test="${item.streamObject.dueDate != null && !item.streamObject.assignee.equals("null")}">${item.streamObject.dueDate}</g:if></li>
			<li>Priority: <g:if test="${item.streamObject.priority != null && !item.streamObject.assignee.equals("null") }">${item.streamObject.priority}</g:if></li>
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