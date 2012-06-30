<html>

	<head>
		<title>Quoddy: Login</title>
		<meta name="layout" content="login" />
	     <nav:resources />
	</head>
	
	<body>
          <div class="hero-unit span6">
                                   
               <!-- begin body content -->
               <h2>Login</h2>
                <p />
          
               <g:if test="${flash.message}">
                    <div class="flash">
                         ${flash.message}
                    </div>
               </g:if>
          
               <g:form controller="login" class="login" action="login">
                    <dl>
               
                         <dt>Username:</dt>
                         <dd><g:textField name="username" /></dd>
               
                         <dt>Password:</dt>
                         <dd><g:passwordField name="password" /></dd>
                    </dl>
                    
                    <g:submitButton name="login" class="btn btn-large" value="Login" />
               </g:form>                            
     
          </div>
	</body>
</html>