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
		<div class="commentBoxWrapper">
			<form>
				<input id="addCommentTextInput" class="addCommentTextInput" type="textbox" value="Add a Comment" ></input>
				<br />
				<input id="submitCommentBtn" class="submitCommentBtn" style="display:none;" type="submit" value="Submit" />
				<input id="cancelCommentBtn" class="cancelCommentBtn" style="display:none;" type="submit" value="Cancel" />
			</form>
		</div>
	</div>
</div>