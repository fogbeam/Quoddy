<html>
	
	<head>
		<title>Quoddy: Edit LIST</title>
		<meta name="layout" content="main" />
	     <nav:resources />
	     <g:javascript src="userListWizard.js"/>		
	</head>
	
	<body>
  <div class="hero span6">
		<h2>Manage List Members</h2>
        <g:form controller="userList" action="editWizard" method="POST">
            
	        <g:select id="usersToAdd" from="" name="usersToAdd" multiple="true" style="display:none;" />
	        <g:select id="usersToRemove" from="" name="usersToRemove" multiple="true" style="display:none;" />		
		
			<div>
              <label for="users">Selected Users</label>
            </div>
    	    <div>                               
              <g:select name="users" from="${listToEdit.members}" optionKey="id" optionValue="fullName" multiple="true">
              </g:select> 
        	</div>
         
       		<div style="float:right;">
       	 		<a href="#" onclick="removeFromSelected();return false;" style="color:red;text-decoration:none;">&gt;</a>
        		<br />
        		<a href="#" onclick="removeAllFromSelected(); return false;" style="color:red;text-decoration:none;">&gt;&gt;</a>
        		<br />
        		<a href="#" onclick="addToSelected(); return false;" style="color:red;text-decoration:none;">&lt;</a>
        		<br />
        		<a href="#" onclick="addAllToSelected(); return false;" style="color:red;text-decoration:none;">&lt;&lt;</a>
        		<br />
        	</div>
        	<div style="float:right;">
              <label for="availableusers">Available Users</label>
        	</div>
        	<div style="float:right;">
              <g:select name="availableusers" from="${availableUsers}" optionKey="id" optionValue="fullName" multiple="true">
              </g:select> 
        	</div> 
			<g:submitButton name="finishWizard" value="Finish" />
		</g:form>
	</body>
</html>