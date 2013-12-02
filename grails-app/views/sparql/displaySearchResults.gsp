<html>
    <head>
        <title>Welcome to Quoddy - SPARQL Search Results</title>
          <meta name="layout" content="basic" />
          <nav:resources />
    </head>
    <body>
    
    	<p />
    
    	<div class="well searchResults" id="searchResults" style="margin-left:35px;padding-top:20px;">  
			<p />
			
			<ul style="margin-left:315px;">
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