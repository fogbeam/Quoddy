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
             
             	<g:hiddenField name="userUuid" value="${profileToEdit?.userUuid}" />
                 <dl>
                 	
                 	<dt>Summary:</dt>
                 	<dd><g:textField name="summary" value="${profileToEdit?.summary}" /></dd>
                    <dt>Birthday (mm/dd/yyyy)</dt>
                    <dd>
 	                   	<g:textField name="birthDayOfMonth" value="${profileToEdit?.birthDayOfMonth}" />
    	               	<g:textField name="birthMonth" value="${profileToEdit?.birthMonth}" />
        	            <g:textField name="birthYear" value="${profileToEdit?.birthYear}" />
                    </dd>
                    <dt>Sex</dt>
                    <dd><g:select name="sex" from="${sexOptions}" value="${profileToEdit?.sex}" optionKey="id" 
       					noSelection="${['null':'Select One...']}" optionValue="text" />
                    </dd>
                    
                    <!--  other fields -->
                    <dt>Location:</dt>
                    <dd><g:textField name="location" value="${profileToEdit?.location}" /></dd>
                    <dt>Hometown:</dt>
                    <dd><g:textField name="hometown" value="${profileToEdit?.hometown}" /></dd>
                    <dt>Languages:</dt>
                    <dd><g:textArea name="languages" value="${profileToEdit?.languages}" rows="5" cols="40"/></dd>
                    <dt>Interests:</dt>
                    <dd><g:textArea name="interests" value="${profileToEdit?.interests}" rows="5" cols="40"/></dd>
                    <dt>Skills:</dt>
                    <dd><g:textArea name="skills" value="${profileToEdit?.skills}" rows="5" cols="40"/></dd>
                    <dt>Groups & Organizations</dt>
                    <dd><g:textArea name="groupsOrgs" value="${profileToEdit?.groupsOrgs}" rows="5" cols="40"/></dd>
                    <dt>Employment History</dt>
                    <dd><g:textArea name="employmentHistory" value="${profileToEdit?.employmentHistory}" rows="5" cols="40"/></dd>
                    <dt>Educational Hisotry</dt>
                    <dd><g:textArea name="educationHistory" value="${profileToEdit?.educationHistory}" rows="5" cols="40"/></dd>
                    <dt>Contact Addresses:</dt>
                    <dd><g:textArea name="contactAddresses" value="${profileToEdit?.contactAddresses}" rows="5" cols="40"/></dd>
                    <dt>Favorites:</dt>
                    <dd><g:textArea name="favorites" value="${profileToEdit?.favorites}" rows="5" cols="40"/></dd>
                    <dt>Projects:</dt>
                    <dd><g:textArea name="projects" value="${profileToEdit?.projects}" rows="5" cols="40"/></dd>
                    
                    
                    <dt>&nbsp;</dt>
                    <dd><g:submitButton name="saveProfile" value="Save"/></dd>
                 </dl>

             </g:form>


          </div>
    </body>
</html>
