<html>
    <head>
        <title>Welcome to Neddick</title>
          <meta name="layout" content="main" />
          <nav:resources />
    </head>
    <body>
          <g:if test="${flash.message}">
               <div class="flash" style="padding-top:15px;color:red;">
                    ${flash.message}
               </div>
          </g:if>
          
        <div class="body" style="margin-left:100px;"> 
           
           <g:form style="margin-top:15px;" controller="siteConfigEntry" action="update" >
               <g:hiddenField name="entryId" value="${theEntry.id}" />
               <label for="entryName">Name:</label> <g:textField name="entryName" value="${theEntry.name}" />
               <div style="margin-top:8px;">
               <label for="entryValue">Value:</label> <g:textField name="entryValue" value="${theEntry.value}" />
               </div>
               <g:submitButton name="Save" style="margin-top:10px;" />
           </g:form>
           <g:form style="margin-left:50px;margin-top:-19px;" controller="siteConfigEntry" action="delete" >
               <g:hiddenField name="entryId" value="${theEntry.id}" />
               <g:submitButton name="Delete" />
           </g:form>
           
        </div> 
                    
    </body>
</html>