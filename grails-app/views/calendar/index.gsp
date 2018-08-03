<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="main" />
    </head>
	<body>
		<div id="bodyContent">	
		
			<h3>Manage Calendar Feeds</h3>
			<g:link controller="calendar" action="createFeed" style="float:right;color:orange;margin-right:200px;margin-bottom:10px;">Create New Calendar Feed</g:link>
			<p />
			<ul style="margin-left:25px;margin-top:40px;">
				<g:each var="calFeed" in="${calFeeds}">
					<li><g:link controller="calendar" action="editFeed" params="[calFeedId:calFeed.id]" >${calFeed.name} : ${calFeed.url}</g:link> </li>
				</g:each>
			</ul>				
		
		</div>
	</body>
</html>