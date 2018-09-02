<html>
	
	<head>
		<title>Quoddy: Edit User Stream</title>
		<meta name="layout" content="main" />
	     <style type="text/css">
	     	#userFilterSelections li {
	     		list-style-type:none;
	     	}
	     </style>
	</head>
	
	<body>
	  <div class="jumbotron span6">	
		<h2>Select users to include in this stream.</h2>
        <g:form controller="userStreamDefinition" action="createWizardFour" method="POST">
			<ul id="userFilterSelections">
				<li>
					<g:radio name="userFilter" value="no_users" checked="true" />&nbsp;Include Nobody (self posts only)
				</li>
				<li>
		        	<g:radio name="userFilter" value="all_users" checked="true" />&nbsp;Include Everbody (friends & people I follow)
        		</li>
        		<li>
	        		<g:radio name="userFilter" value="select_list" checked="true" />&nbsp;Included only selected users
				</li>
			</ul>
			<g:select optionKey="uuid" optionValue="fullName" 
        		name="users" 
        		from="${users}"
        		value="${selectedUsers}" 
        		multiple="true" />        	
			<g:submitButton name="stage4" value="Next" class="btn" />
		</g:form>
		</div>
	</body>
</html>