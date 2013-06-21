<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img height="48" width="48" src="${createLinkTo(dir:'images', file:'task_report_hot.png')}"/>
	</div>
	<div class="aseTitleBar"> <!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
		${item.streamObject.owningSubscription.name}
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
	<div class="aseClear" >
	</div>
	<div class="aseFooter" >
		<g:formatDate date="${item.streamObject.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"/>
	</div>
</div>