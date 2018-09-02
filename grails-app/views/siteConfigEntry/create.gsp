<html>
    <head>
        <title>Welcome to Neddick</title>
          <meta name="layout" content="main" />
    </head>
    <body>
          <g:if test="${flash.message}">
               <div class="flash" style="padding-top:15px;color:red;">
                    ${flash.message}
               </div>
          </g:if>
          
        <div class="body" style="margin-left:100px;"> 
           
           <g:form style="margin-top:15px;" controller="siteConfigEntry" action="save" >
               <label for="entryName">Name:</label> <g:textField name="entryName" value="" />
               <br />
               <label for="entryValue">Name:</label> <g:textField name="entryValue" value="" />
               <br />
               <g:submitButton name="Create" />
           </g:form>
           
        </div> 
                    
    </body>
</html>