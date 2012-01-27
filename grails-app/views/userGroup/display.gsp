<html>
	
	<head>
		<title>Quoddy: Display GROUP</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	
		<h3>Display GROUP</h3>
		<g:link controller="userGroup" 
				action="joinGroup" 
				style="float:right;color:orange;margin-right:200px;margin-bottom:10px;"
				params="[groupId:group.id]">Join This Group</g:link>
		
		<p />
        <g:if test="${session.user != null}">
  
			<g:form name="postToGroupForm" controller="userGroup" action="postToGroup" >
				<g:hiddenField name="groupId" value="${group.id}" />
				<input type="text" id="statusText" name="statusText" />
				<input id="postToGroupSubmit" type="submit" value="Post To Group" />
			</g:form>      
        
        	<p />
        
			<g:if test="${activities != null}">

				<div id="activityStream">
				
					<g:render template="/ugroupStream" />
				
				</div>
			</g:if>
		</g:if>
	</body>
</html>