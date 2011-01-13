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
             
             	<g:hiddenField name="uuid" value="${userToEdit?.uuid}" />
                 <dl>
                 	
                 	<dt>Summary:</dt>
                 	<dd><g:textField name="summary" value="${userToEdit?.profile?.summary}" /></dd>
                    <dt>Birthday (mm/dd/yyyy)</dt>
                    <dd>
 	                   	<g:textField name="birthDayOfMonth" value="${userToEdit?.profile?.birthDayOfMonth}" />
    	               	<g:textField name="birthMonth" value="${userToEdit?.profile?.birthMonth}" />
        	            <g:textField name="birthYear" value="${userToEdit?.profile?.birthYear}" />
                    </dd>
                    <dt>Sex</dt>
                    <dd><g:textField name="sex" value="${userToEdit?.profile?.sex}" /></dd>
                    
                    <!--  other fields -->
                    <dt>Location:</dt>
                    <dd>&nbsp;</dd>
                    <dt>Hometown:</dt>
                    <dd>&nbsp;</dd>
                    <dt>Languages:</dt>
                    <dd>&nbsp;</dd>
                    <dt>Interests:</dt>
                    <dd>&nbsp;</dd>
                    <dt>Skills:</dt>
                    <dd>&nbsp;</dd>
                    <dt>Groups & Organizations</dt>
                    <dd>&nbsp;</dd>
                    <dt>Employment History</dt>
                    <dd>&nbsp;</dd>
                    <dt>Educational Hisotry</dt>
                    <dd>&nbsp;</dd>
                    <dt>Contact Addresses:</dt>
                    <dd>&nbsp;</dd>
                    <dt>Favorites:</dt>
                    <dd></dd>
                    <dt>Projects:</dt>
                    <dd>&nbsp;</dd>
                    
                    
                    
                    <dt>&nbsp;</dt>
                    <dd><g:submitButton name="saveProfile" value="Save"/></dd>
                 </dl>

             </g:form>


          </div>
    </body>
</html>
