<html>
	
	<head>
		<title>Quoddy: Create User List</title>
		<meta name="layout" content="main" />
	     <nav:resources />
	     <g:javascript src="userListWizard.js"/>		
	</head>
	
	<body>
	
		<h3>Edit List Membership</h3>
        <g:form controller="userList" action="createWizard" method="POST">
            
	        <g:select id="usersToAdd" from="" name="usersToAdd" multiple="true" style="display:none;" />
	        <g:select id="usersToRemove" from="" name="usersToRemove" multiple="true" style="display:none;" />		
		
			<div style="margin-top:20px;">
              <label for="users">Selected Users</label>
            </div>
    	    <div style="float:left;margin-top:35px;margin-left:10px;">                               
              <g:select name="users" from="" optionKey="id" optionValue="fullName" multiple="true">
              </g:select> 
        	</div>
         
       		<div style="margin-left:480px;margin-top:40px;">
       	 		<a href="#" onclick="removeFromSelected();return false;" style="color:red;text-decoration:none;">&gt;</a>
        		<br />
        		<a href="#" onclick="removeAllFromSelected(); return false;" style="color:red;text-decoration:none;">&gt;&gt;</a>
        		<br />
        		<a href="#" onclick="addToSelected(); return false;" style="color:red;text-decoration:none;">&lt;</a>
        		<br />
        		<a href="#" onclick="addAllToSelected(); return false;" style="color:red;text-decoration:none;">&lt;&lt;</a>
        		<br />
        	</div>
        
          
        	<div style="margin-left:550px;margin-top:-100px;">
              <label for="availableusers">Available Users</label>
        	</div>
        	<div style="margin-left:540px;margin-top:40px;">                               
              <g:select style="margin-top:-35px;" name="availableusers" from="${availableUsers}" optionKey="id" optionValue="fullName" multiple="true">
              </g:select> 
        	</div> 

			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
		
	</body>
</html>