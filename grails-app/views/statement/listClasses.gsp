<html>
    <head>
        <title>Welcome to Grails</title>
        <meta name="layout" content="main" />
    </head>
    <body>
         <div id="pageBody">         
        	 <div class="navbar">
  				<div class="navbar-inner">
    				<g:render template="/navbar" />
  				</div>
			</div>
			<div class="row">
  				<div class="col-md-12">
					<ul>
						<g:each in="${allStatements}" var="statement">
							<li>${statement.toString().replace("<", "[").replace( ">", "]") }</li>
						</g:each>
					</ul>
  				</div>
  			</div>
  		</div>
  	</body>
</html>