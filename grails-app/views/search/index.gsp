<html>
	
	<head>
		<title>Quoddy: Advanced Search</title>
		<meta name="layout" content="main" />
	</head>
	
	<body>
		<h2>Quoddy: Advanced Search</h2>
		<p>
			<g:form controller="search" action="doAdvancedSearch" method="GET" >	
			<label for="searchQuery">Search:</label><g:textField name="queryString" value="" />
			<br />
			<label for="searchEverything">Everything</label><g:checkBox name="searchEverything"/>
			<label for="searchStatusUpdates">Status Updates</label><g:checkBox name="searchStatusUpdates"/>
			<label for="searchCalendarFeedItems">Calendar Feeds</label><g:checkBox name="searchCalendarFeedItems"/>
			<label for="searchBusSubItems">Business Event Subscriptions</label><g:checkBox name="searchBusSubItems"/>
			<label for="searchRssFeedItems">Rss Feeds</label><g:checkBox name="searchRssFeedItems"/>
			<label for="searchActivityStreamItems">External Activities</label><g:checkBox name="searchActivityStreamItems"/>
			<label for="searchUsers">Users</label><g:checkBox name="searchUsers"/>
			<label for="searchFriends">Friends</label><g:checkBox name="searchFriends"/>
			<label for=""></label><g:submitButton name="Search" />
			</g:form>
		</p>
		
	</body>
	
</html>