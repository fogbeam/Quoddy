<html>
	
	<head>
		<title>Quoddy: The page for managing LISTS</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
  <div class="span8 listView">
  <!-- Blank state, should display when there are not any lists
  <div class="hero-unit">
    <h1>Lists</h1>
    <p>Add new lists and manage existing ones here. <a href="#">What are lists?</a></p>
    <p><a class="btn btn-primary btn-large">Create a New List</a></p>
  </div>
  --> 


  <!-- populated state -->
  <div class="hero-unit">
    <div class="span4">
    <h2>Lists</h2>
    <p>Manage existing lists and create new ones.</p>
    </div>
    <div class="span4 offset3">
		<g:link controller="userList" action="createWizard" class="btn btn-primary btn-large">Create New List</g:link>
    </div>
  <div class="clear"></div>
  </div>

		<ul>
			<g:each var="list" in="${userLists}">
				<li><g:link controller="userList" action="editWizard" event="start" params="[listId:list.id]" >${list.name}</g:link> </li>
			</g:each>
		</ul>		
	</div>
	</body>
	
</html>