<html>
    <head>
        <title>Quoddy: Register New User</title>
        <meta name="layout" content="main"/>
    </head>
    <body>
           <div style="margin-left:35px;padding-top:30px;">

              <!-- start body content -->
              <h1>Register New User</h1>

	  <g:if test="${flash.message}">
    		<div class="message">${flash.message}</div>
	  </g:if>

             <g:hasErrors>
                 <div class="errors">
                    <g:renderErrors bean="${flash.user}" as="list" />
                 </div>
             </g:hasErrors>

             <g:form action="adminSaveUser">
                 <dl>
                     <dt>User Id</dt>
                     <dd><g:textField name="userId" value="${user?.userId}"/></dd>
                     <dt>Password</dt>
                     <dd><g:passwordField name="password" value="${user?.password}"/></dd>
                     <dt>Confirm Password</dt>
                     <dd><g:passwordField name="passwordRepeat" value="${user?.passwordRepeat}"/></dd>
                     
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
                     <dt><g:submitButton name="register" value="Register"/></dt>
                 </dl>

             </g:form>


          </div>
    </body>
</html>
