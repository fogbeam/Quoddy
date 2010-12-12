<html>
    <head>
        <title>Quoddy: Edit Profile</title>
        <meta name="layout" content="main"/>
        <nav:resources />
    </head>
    <body>
           <div style="margin-left:35px;padding-top:30px;">

              <!-- start body content -->
              <h1>Edit Profile</h1>

             <g:hasErrors>
                 <div class="errors">
                    <g:renderErrors bean="${flash.user}" as="list" />
                 </div>
             </g:hasErrors>

             <g:form action="saveProfile">
             
             	<g:hiddenField name="uuid" value="${user?.uuid}" />
                 <dl>
                     <dt><g:submitButton name="saveProfile" value="Save"/></dt>
                 </dl>

             </g:form>


          </div>
    </body>
</html>
