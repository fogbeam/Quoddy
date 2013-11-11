<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img height="48" width="48" src="${createLinkTo(dir:'images', file:'task_report_hot.png')}"/>
	</div>
	<div class="aseTitleBar">
	
		<span class="aseTitleBarUserLink" >
		${item.streamObject.owningSubscription.name}

		</span> 
		<span class="aseTitleBarPermalink"> <a href="#"
			title="${formatDate(date:item.dateCreated)}"><g:formatDate
					date="${item.dateCreated}" type="datetime" style="SHORT"
					timeStyle="SHORT" /></a>
		</span>
	</div>	
	
	
	<div class="activityStreamEntry"> 
		<font color="red">ACTIVITI USER TASK</font>
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
	</div>
	<!-- begin aseClear -->
	<div class="aseClear"></div>
	<!--  end aseClear -->
	
	<!-- begin aseFooter -->
	<div class="aseFooter">
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