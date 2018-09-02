<html>
	
	<head>
		<title>Quoddy: My Updates</title>
        <meta name="layout" content="main"/>
	</head>
	
	<body>
		<p />
		<h2>My Updates</h2>
		<p />
		<g:if test="${flash.message}">
	        <div class="flash">
	             ${flash.message}
	        </div>
	   </g:if>

		<ul>
			<g:each in="${updates}" var="update">
			
				<!- - display discrete entries here - ->
				<li>
					${update.text} [ Posted at: ${update.dateCreated} ]
				</li>
			</g:each>
		</ul>
	</body>
	
</html>