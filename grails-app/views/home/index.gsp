<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="main" />
       	<nav:resources />
    </head>
	<body>
		<h1>Welcome to Project Quoddy</h1>
		
		<p />
             <g:hasErrors>
                 <div class="errors">
                    <g:renderErrors bean="${flash.user}" as="list" />
                 </div>
             </g:hasErrors>		
		<p />
		<g:if test="${session.user != null}">
			<g:form controller="status" action="updateStatus" >
				<input type="text" name="statusText" />
				<input type="submit" value="Update Status" />
			</g:form>
			<br />
			
			<dl>
				<dt>Status:</dt>
				
				<g:if test="${user != null}">
					<dd><div class="myStatus">${session.user?.currentStatus?.text }</div></dd>
				</g:if>
			</dl>
			<hr />
			<h2>Activity Stream</h2>
			<p />
			<g:if test="${activities != null}">
			
				<div id="activityStream">
					<dl>
						<g:each in="${activities}" var="activity">
							<dd><span class="activityStreamEntry">${activity.text}</span></dd>
						</g:each>
					</dl>
				</div>
			
			</g:if>
		</g:if>
	</body>
</html>