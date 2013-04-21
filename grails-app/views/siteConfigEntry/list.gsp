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
          
       <div class="nav" style="margin-top:15px;margin-left:100px;">  
            <span class="menuButton"><g:link controller="siteConfigEntry" action="create" class="create">New Entry</g:link></span> 
        </div> 
        <div class="body" style="margin-left:100px;"> 
            
            <div style="margin-top:15px;" > 
                <table> 
                    <thead> 
                        <tr> 
                            <th>Id</th> 
                            <th style="padding-left:8px;">Name</th>
                            <th style="padding-left:8px;">Value</th>  
                        </tr> 
                    </thead> 
                    <tbody>
                    
                         <g:each in="${allEntries}" var="entry" status="oddEven">
                              <tr> 
                                   <td style="padding-top:8px;">
                                        <g:link controller="siteConfigEntry" action="edit" id="${entry.id}">${entry.id}</g:link>
                                   </td>  
                                   <td style="padding-left:8px;padding-top:8px;">${entry.name}</td> 
                                   <td style="padding-left:8px;padding-top:8px;">${entry.value}</td>
                              </tr> 
                         </g:each>         
                    </tbody> 
                </table> 
            </div> 
        </div> 
                    
    </body>
</html>