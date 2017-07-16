<html>
    <head>
        <title>Welcome to Grails</title>
        <meta name="layout" content="main" />
    </head>
    <body>

		<div class="col-md-10">	
			<ul>
				<g:each in="${allStatements}" var="statement">
					<li>${statement}</li>
				</g:each>
			</ul>
		</div>
  	</body>
</html>