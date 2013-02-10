<html>
	
	<head>
		<title>Quoddy: Edit LIST</title>
		<meta name="layout" content="main" />
	     <nav:resources />
	     <g:javascript src="userListWizard.js"/>		
	</head>
	
	<body>
  <div class="hero span6">
		<h2>Edit Users</h2>
        <g:form controller="userList" action="editWizard" method="POST">
            
	        <g:select id="usersToAdd" name="usersToAdd" multiple="true" style="display:none;" />
	        <g:select id="usersToRemove" name="usersToRemove" multiple="true" style="display:none;" />		
		
			<div>
              <label for="users">Selected Users</label>
            </div>
    	    <div>                               
              <g:select name="users" from="${listToEdit.members}" optionKey="id" optionValue="fullName" multiple="true">
              </g:select> 
        	</div>
         
       		<div>
       	 		<a href="#" onclick="removeFromSelected();return false;" style="color:red;text-decoration:none;">&gt;</a>
        		<br />
        		<a href="#" onclick="removeAllFromSelected(); return false;" style="color:red;text-decoration:none;">&gt;&gt;</a>
        		<br />
        		<a href="#" onclick="addToSelected(); return false;" style="color:red;text-decoration:none;">&lt;</a>
        		<br />
        		<a href="#" onclick="addAllToSelected(); return false;" style="color:red;text-decoration:none;">&lt;&lt;</a>
        		<br />
        	</div>
        	<div>
              <label for="availableusers">Available Users</label>
        	</div>
        	<div>
              <g:select name="availableusers" from="${availableUsers}" optionKey="id" optionValue="fullName" multiple="true">
              </g:select> 
        	</div> 
			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
	</body>
</html>