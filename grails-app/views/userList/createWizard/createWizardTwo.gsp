<html>
	
	<head>
		<title>Quoddy: Create User List</title>
		<meta name="layout" content="main" />
	     <nav:resources />
	     <g:javascript src="userListWizard.js"/>		
	</head>
	
	<body>
		<div class="jumbotron span6">
			
			<h3 style="margin-left:-40px;">Edit List Membership</h3>
	        
	        <g:form controller="userList" action="createWizard" method="POST">
	            
		        <g:select id="usersToAdd" from="" name="usersToAdd" multiple="true" style="display:none;" />
		        <g:select id="usersToRemove" from="" name="usersToRemove" multiple="true" style="display:none;" />		
			
	    	    <div style="float:left;margin-top:40px;margin-left:-40px;width:47%">                               
					<label for="users">Selected Users</label>
					<br />
	              	<g:select name="users" from="" optionKey="id" optionValue="fullName" multiple="true">
	              	</g:select> 
	        	</div>
	         
	       		<div style="float:left;margin-left:18px;margin-top:80px;width:6%">
	       	 		<a href="#" onclick="removeFromSelected();return false;" style="color:red;text-decoration:none;">&gt;</a>
	        		<br />
	        		<a href="#" onclick="removeAllFromSelected(); return false;" style="color:red;text-decoration:none;">&gt;&gt;</a>
	        		<br />
	        		<a href="#" onclick="addToSelected(); return false;" style="color:red;text-decoration:none;">&lt;</a>
	        		<br />
	        		<a href="#" onclick="addAllToSelected(); return false;" style="color:red;text-decoration:none;">&lt;&lt;</a>
	        		<br />
	        	</div>
	        
	          
	        	<div style="float:left;margin-left:18px;margin-top:40px;width:47%">
	        		<label for="availableusers">Available Users</label>                               
	              	<br />
	              	<g:select name="availableusers" from="${availableUsers}" optionKey="id" optionValue="fullName" multiple="true">
	              	</g:select> 
	        	</div> 
				<div style="clear:both;padding-top:35px;">
					<g:submitButton name="finishWizard" value="Finish" />
				</div>
			</g:form>
		</div>
	</body>
</html>