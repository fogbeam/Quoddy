<html>
	
	<head>
		<title>Quoddy: View User</title>
        <meta name="layout" content="admin"/>
        <nav:resources />
	</head>
	
	<body>
		<div class="adminContentArea">
			<div class="row">
				<div class="span2">
					<a href="#" class="btn btn-primary">Add User</a>
				
				</div>
				
				<div class="span2">
					<a href="#" class="btn btn-primary">Import Users</a>
				</div>
				
				<div class="span8">&nbsp;</div>
				
			</div>
		
		<!--  a searchable user grid pane thing... -->
		<!--  TODO: user table w/ search  -->
		<div style="margin-top:100px;">
			<g:form controller="user" action="manageUsers">
				<g:textField name="queryString" />
				<input type="submit" class="btn bn-primary" style="margin-right: 650px; float: right; margin-top: 5px;" value="Search"></input>
			</g:form>
		</div>
		
		
		<div style="margin-top:50px;">
			<table class="table table-striped">
				<g:each in="${users}" var="aUser">
					<tr>
						<td><a href="${createLink(controller:'user', action:'editUser')}">${aUser.id}</a></td>
						<!-- <td>${aUser.uuid}</td> -->
						<td>${aUser.fullName}</td>
						<td>${aUser.userId}</td>
						<td>${aUser.displayName}</td>
						<td>${aUser.email}</td>
						<td><a href="#">disable user</a></td>
						<td><a href="#">delete user</a></td>
					</tr>
				</g:each>
			</table>
		</div>
		
		</div>
	</body>
</html>