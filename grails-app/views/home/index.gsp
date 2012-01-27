<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="main" />
    </head>
	<body>
		<div id="bodyContent">	
			
			<g:if test="${session.user != null}">
				<g:form name="updateStatusForm" controller="status" action="updateStatus" >
					<input type="text" id="statusText" name="statusText" />
					<input id="updateStatusSubmit" type="submit" value="Update Status" />
				</g:form>
				
            	
            	<!--  we should have a collection available to render, with the statusupdates for this
            	User, that exist when this page is loaded.  Once we're loaded, we'll update an "available posts"
            	message with AJAX and provide an AJAX powered "update" link that will refresh this view
            	using JQuery's AJAX support. -->
            	<div id="messageCount">
            		<a href="#" id="refreshMessagesLink">0 messages pending</a>
            	</div>
            	
            	<g:if test="${session.user != null}">
					<g:if test="${activities != null}">
			
						<div id="activityStream">
							
							<g:render template="/activityStream" />
							
						</div>
					</g:if>
            	</g:if>
            </g:if>
    	</div>
	</body>
</html>