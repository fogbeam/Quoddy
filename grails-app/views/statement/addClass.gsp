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
					
					<g:form controller="statement" action="saveClass" method="POST">
						<g:textField name="classUri"/>
						<br />
						<g:textField name="classLabel"/>
						<br />
						<g:submitButton name="submit"/>
					</g:form>
  				</div>
  			</div>
  		</div>
  	</body>
</html>