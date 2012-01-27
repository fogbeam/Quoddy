<html>
	
	<head>
		<title>Quoddy: Edit LIST</title>
		<meta name="layout" content="main" />
	     <nav:resources />
	     <g:javascript src="userListWizard.js"/>		
	</head>
	
	<body>
	
		<h3>Edit Users</h3>
        <g:form controller="userList" action="editWizard" method="POST">
            
	        <g:select id="usersToAdd" name="usersToAdd" multiple="true" style="display:none;" />
	        <g:select id="usersToRemove" name="usersToRemove" multiple="true" style="display:none;" />		
		
			<div style="margin-top:20px;">
              <label for="users">Selected Users</label>
            </div>
    	    <div style="float:left;margin-top:35px;margin-left:-85px;">                               
              <g:select name="users" from="${listToEdit.members}" optionKey="id" optionValue="displayName" multiple="true">
              </g:select> 
        	</div>
         
       		<div style="margin-left:300px;margin-top:7px;">
       	 		<a href="#" onclick="removeFromSelected();return false;" style="color:red;text-decoration:none;">&gt;</a>
        		<br />
        		<a href="#" onclick="removeAllFromSelected(); return false;" style="color:red;text-decoration:none;">&gt;&gt;</a>
        		<br />
        		<a href="#" onclick="addToSelected(); return false;" style="color:red;text-decoration:none;">&lt;</a>
        		<br />
        		<a href="#" onclick="addAllToSelected(); return false;" style="color:red;text-decoration:none;">&lt;&lt;</a>
        		<br />
        	</div>
        
          
        	<div style="margin-left:440px;margin-top:35px;">
              <label for="availableusers">Available Users</label>
        	</div>
        	<div style="margin-left:320px;margin-top:7px;">                               
              <g:select name="availableusers" from="${availableUsers}" optionKey="id" optionValue="fullName" multiple="true">
              </g:select> 
        	</div> 

			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
		
	</body>
</html>