<html>
    <head>
        <title>Quoddy: Edit Account</title>
        <meta name="layout" content="main"/>
        <nav:resources />
    </head>
    <body>
           <div class="jumbotron span6">

              <!-- start body content -->
              <h2>Edit Account</h2>

             <g:hasErrors>
                 <div class="errors">
                    <g:renderErrors bean="${flash.user}" as="list" />
                 </div>
             </g:hasErrors>

             <g:form action="saveAccount">
             
             	<g:hiddenField name="uuid" value="${user?.uuid}" />
                 <dl>
                     <dt>User Id</dt>
                     <dd><g:textField name="userId" value="${user?.userId}"/></dd>                     
					 <dt>First Name</dt>
                     <dd><g:textField name="firstName" value="${user?.firstName}"/></dd>
                     <dt>Last Name</dt>
              		 <dd><g:textField name="lastName" value="${user?.lastName}"/></dd>
                     <dt>Display Name</dt>
                     <dd><g:textField name="displayName" value="${user?.displayName}"/></dd>
                     <dt>Bio</dt>
                     <dd><g:textArea name="bio" value="${user?.bio}"/></dd>
                     <dt>Email</dt>
                     <dd><g:textField name="email" value="${user?.email}"/></dd>
                     <dt><g:submitButton class="btn btn-large" name="saveProfile" value="Save"/></dt>
                 </dl>

             </g:form>


          </div>
    </body>
</html>
