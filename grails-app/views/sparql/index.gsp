<html>
    <head>
        <title>Welcome to Quoddy - SPARQL Search</title>
          <meta name="layout" content="basic" />
          <nav:resources />
    </head>
    <body>
          <g:if test="${flash.message}">
               <div class="flash" style="padding-top:15px;color:red;">
                    ${flash.message}
               </div>
          </g:if>
          
          <div class="well" style="width:95%;min-height:300px;">
          
          	<g:form action="doSearch" controller="sparql" method="POST">
          		<g:textArea name="sparqlQuery" style="width:95%;margin:auto;min-height:250px;">
          		</g:textArea>
          		<br />
          		<g:submitButton name="search" value="Search" style="margin-top:25px;" />
          	
          	</g:form>
          </div>
          
          
     </body>
     
</html>