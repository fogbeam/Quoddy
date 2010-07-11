<html>

	<head>
		<title>Shelley1: Login</title>
		<meta name="layout" content="main" />
	     <nav:resources />
	</head>
	
	<body>
          <div style="margin-left:35px;padding-top:30px;">
                                   
               <!-- begin body content -->
               <h3>Login</h3>
                <p />
          
               <g:if test="${flash.message}">
                    <div class="flash">
                         ${flash.message}
                    </div>
               </g:if>
          
               <g:form controller="login" action="login">
                    <dl>
               
                         <dt>Username:</dt>
                         <dd><g:textField name="username" /></dd>
               
                         <dt>Password:</dt>
                         <dd><g:passwordField name="password" /></dd>
                    </dl>
                    
                    <g:submitButton name="login" value="Login" />
               </g:form>                            
     
          </div>
	</body>
</html>