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
			<div class="row-fluid">
  				<div class="span12">
					
					<g:form controller="statement" action="saveProperty" method="POST">
						<g:textField name="propertyUri"/>
						<br />
						<g:textField name="propertyLabel"/>
						<br />
						<g:submitButton name="submit"/>
					</g:form>
  				</div>
  			</div>
  		</div>
  	</body>
</html>