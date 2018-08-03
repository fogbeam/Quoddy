<html>
	
	<head>
		<title>Quoddy: Display Group</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div id="bodyContent" class="span8">

        <shiro:authenticated>
			<p />
			<g:if test="${userIsGroupMember}" >
				<g:link controller="userGroup" 
					action="leaveGroup" 
					style="float:right;color:orange;margin-right:50px;margin-bottom:10px;"
					params="[groupId:group.id]">Leave This Group</g:link>
			</g:if>
			<g:else>
				<g:link controller="userGroup" 
					action="joinGroup" 
					style="float:right;color:orange;margin-right:50px;margin-bottom:10px;"
					params="[groupId:group.id]">Join This Group</g:link>
	
			</g:else>

	   		<g:if test="${flash.message}">
   				<div class="message" style="font-size:14pt;color:red;">${flash.message}</div>
 			</g:if>

			
			<p />  
  

		  	<g:if test="${userIsGroupMember}">
				<g:form name="postToGroupForm" controller="userGroup" action="postToGroup" >
					<g:hiddenField name="groupId" value="${group.id}" />
					<input type="text" id="statusText" name="statusText" />
					<input id="postToGroupSubmit" type="submit" value="Post To Group" />
				</g:form>
        	</g:if>
        	<g:else>
        		<span>If you would like to post to this Group, please <g:link controller="userGroup" 
				action="joinGroup" style="color:red;" params="[groupId:group.id]">join first</g:link></span>
        	</g:else>
  
        	<p />
        
			<g:if test="${activities != null}">

				<div id="activityStream">
				
					<g:render template="/activityStream" />
				
				</div>
			</g:if>
		</shiro:authenticated>
		</div>
	</body>
</html>