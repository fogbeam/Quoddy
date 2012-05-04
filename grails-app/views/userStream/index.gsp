<html>
	
	<head>
		<title>Quoddy: The page for managing STREAMS</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<p />
		<h3>The page for managing STREAMS</h3>
		<g:link controller="userStream" action="createWizard" style="float:right;color:orange;margin-right:200px;margin-bottom:10px;">Create New Stream</g:link>
		<p />
		<ul style="margin-left:25px;margin-top:40px;">
			<g:each var="stream" in="${sysDefinedStreams}">
				<li><g:link controller="userStream" action="editWizard" params="[streamId:stream.id]" >${stream.name}</g:link> </li>
			</g:each>
				
			<g:each var="stream" in="${userDefinedStreams}">
				<li><g:link controller="userStream" action="editWizard" params="[streamId:stream.id]" >${stream.name}</g:link></li>
			</g:each> 
		</ul>
	</body>
	
</html>