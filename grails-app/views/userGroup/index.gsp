<html>
	
	<head>
		<title>Quoddy: The page for managing GROUPS</title>
		<meta name="layout" content="main" />	
	</head>
	
	<body>

  <div class="span8 listView">
  <!-- Blank state, should display when there are not any streams
  <div class="jumbotron">
    <h1>Groups</h1>
    <p>Add new groups and manage existing ones here. <a href="#">What are groups?</a></p>
    <p><a class="btn btn-primary btn-large">Create a New Group</a></p>
  </div>
  --> 


  <!-- populated state -->
  <div class="jumbotron">
    <div class="span4">
    <h2>Groups</h2>
    <p>Manage existing groups and create new ones.</p>
    </div>
    <div class="span4 offset3">
    <g:link controller="userGroup" action="create" class="btn-large btn btn-primary">Create a New Group</g:link>
    </div>
  <div class="clear"></div>
  </div>

			<div>
				<h4>Groups I'm In</h4>
				<ul>
					<g:each var="group" in="${userMembershipGroups}">
						<li><g:link controller="userGroup" action="edit" id="${group.id}" >${group.name}</g:link> </li>
					</g:each>
				</ul>
			</div>	
			
			<div>
				<h4>Groups I Own</h4>
				<ul>
					<g:each var="group" in="${userOwnedGroups}">
						<li><g:link controller="userGroup" action="edit" id="${group.id}" >${group.name}</g:link> </li>
					</g:each>
				</ul>
			</div>
		</div>	
		</div>	
	</body>
</html>