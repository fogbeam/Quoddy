<html>

	<head>
		<title>Quoddy: Admin</title>
		<meta name="layout" content="admin" />
	     <nav:resources />
	</head>
	<body>
		<h2>Administration</h2>
		<p />
		<div>
			<ul>
				<li><a href="${createLink(controller:'user', action:'manageUsers')}">Manage Users</a></li>
				<li><a href="${createLink(controller:'admin', action:'importUsers')}">Import Users</a></span></li>
				<li><a href="${createLink(controller:'admin', action:'manageSecurity')}">Manage Security (Roles & Permissions)</a></li>
				<li><a href="${createLink(controller:'schedule', action:'index')}">Manage Scheduled Jobs</a></li>
				<li><a href="${createLink(controller:'siteConfigEntry', action:'index')}">Manage Site Config</a></li>
				<!-- <li><a href="#"></a></li> -->   
			</ul>
		</div>
	</body>

</html>