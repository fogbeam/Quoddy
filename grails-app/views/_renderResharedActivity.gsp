<!--  begin aseWrapper area -->
<div class="aseWrapper" id="aseWrapper.${item.uuid}">


	
	<!-- begin aseTitleBar -->
	<div class="aseTitleBar">

		<!-- begin aseAvatarBlock -->
		<div class="aseAvatarBlock">
			<img src="${createLink(controller:'profilePic',action:'thumbnail',id:item.owner.userId)}" />
		</div>
		<!-- end aseAvatarBlock -->

		<span class="aseTitleBarPermalink">
			<g:link controller="permalink" action="index" params="${[uuid:item.uuid]}" title="${formatDate(date:item.dateCreated)}">
				<g:formatDate date="${item.dateCreated}" type="datetime" style="SHORT" timeStyle="SHORT" />
			</g:link>
		</span>
		
		<span class="aseTitleBarPermalink"> <a href="#"
			title="${formatDate(date:item.dateCreated)}"><g:formatDate
					date="${item.dateCreated}" type="datetime" style="SHORT"
					timeStyle="SHORT" /></a>
		</span>
		
        <div class="commentButtonBar">
        
            <!-- if super-secret fogbeam-dev mode is on, render the "delete" button -->
        	<g:if test="CH.config.fogbeam.devmode">
        		<span class="xButton" id="xButton.${item.uuid}" name="xButton.${item.uuid}" >
        			<a href="#" class="btn btn-default">X</a>
      			</span>	
        	</g:if>
        
        
      		<span class="plusOneButton" id="plusOneButton.${item.uuid}" name="plusOneButton.${item.uuid}" >
        		<a href="#" class="btn btn-default">+1</a>
      		</span>
      		<span class="shareButton" id="shareButton.${item.uuid}" name="shareButton.${item.uuid}" >
        		<a href="#" class="btn btn-default">Share</a>
      		</span>
      		<span class="showHideCommentsButton">
      			<!-- change the initial state of this button
      			depending on whether or not there are any comments yet. 
      			If there are no comments, render it with text "no comments"
      			(and maybe make the button inactive)?
      			-->
      			
      			<g:if test="${item.streamObject.comments.size() > 0}">
        			<a href="#" class="btn btn-default">Hide Comments</a>
      			</g:if>
      			<g:else>
      				<a href="#" class="btn btn-default">No Comments</a>
      			</g:else>
      		</span>
    	</div>		
		
		
	</div>
	<!--  end aseTitleBar -->
	
	<!-- begin activityStreamEntry -->
	<div class="activityStreamEntry resharedActivityStreamEntry">
		This item was originally shared by: ${item.originalItem.owner.userId}
		<p>
			${item.originalItem.content}
		</p>
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
					bean="${item.originalItem.streamObject.comments}" />
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
<!-- end aseWrapper -->
