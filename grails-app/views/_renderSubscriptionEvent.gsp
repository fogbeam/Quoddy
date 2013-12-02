<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img src="${createLinkTo(dir:'images/', file:'business_event.jpg')}" />
	</div>
	<div class="aseTitleBar">
		
		<span class="aseTitleBarUserLink">
			<a href="${createLink(controller:'eventSubscription', action:'display', params:[subscriptionId:item.streamObject.owningSubscription.id])}">${item.streamObject.owningSubscription.name}</a>
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
	
	<div class="activityStreamEntry businessSubscriptionEvent"> 
		<h2>Business Subscription Event</h2>
		<p>
			<g:if test="${item.streamObject.xmlDoc != null}">
				<g:transform stylesheet="oagis3" source="${item.streamObject.xmlDoc}" factory="org.apache.xalan.processor.TransformerFactoryImpl" />
			</g:if>
			<g:else>
				<h3>Error rendering Subscription Event: Please Contact Your System Administrator</h3>
			</g:else>
		</p>
	</div>
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
<!--  end aseWrapper -->