<html>
	
	<head>
		<title>Quoddy: People Search Results</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div class="span8">
			<p />
			Matching People:
			<p />
			<ul style="list-style-type:none;">
				<g:each in="${allUsers}" var="result">
				
					<!-- display discrete entries here -->
					<li>
						<g:link controller="user" action="viewUserProfile" params="[userId:result.object.userId]">${result.object.fullName}</g:link>
					</li>
				</g:each>
			</ul>
		</div>
	</body>
	
</html>