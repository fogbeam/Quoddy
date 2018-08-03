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
					
					<g:form controller="statement" action="save" method="POST">
						<g:textField name="subject"/>
						<br />
						<g:textField name="predicate"/>
						<br />
						<g:textField name="object"/>
						<br />
						<g:submitButton name="submit"/>
					</g:form>
  				</div>
  			</div>
  		</div>
  	</body>
</html>