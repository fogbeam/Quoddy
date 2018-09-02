<html>
    <head>
        <title>Welcome to Quoddy - SPARQL Search Results</title>
          <meta name="layout" content="main" />
    </head>
    <body>
    
    	<div class="col-md-10" id="searchResults">  
			<p />
			
			<ul>
				<g:each in="${searchResults}" var="searchResult">
					<g:if test="${searchResult.object.streamObject != null}"> 
						<g:render template="${searchResult.object.streamObject.templateName}" var="item" bean="${searchResult.object}" /> 
					</g:if>
					<g:else>
						<g:render template="${searchResult.object.templateName}" var="item" bean="${searchResult.object}" />
					</g:else>
				
				</g:each>
			</ul>                
          </div> 
    </body>
</html>