<g:each in="${comments}" var="comment">
	<div class="individualCommentBox">
		<span class="commentUserName">
		<a href="${ createLink(controller:'user', action:'viewUser', params:['userId':comment.creator.userId])}">${comment.creator.fullName}</a></span>&nbsp;&nbsp;
		<span class="commentDateTime" alt="${comment.formattedCreateDate}" title="${comment.formattedCreateDate}" >${comment.formattedCreateTime}</span>
		<br />
		<span class="commentText">${comment.text}</span>
		<br />
	</div>
</g:each>