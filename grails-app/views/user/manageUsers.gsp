<html>
	
	<head>
		<title>Quoddy: View User</title>
        <meta name="layout" content="admin"/>
	</head>
	
	<body>
		
		<div class="adminContentArea">
			<div class="row">
				<div class="col-md-2">
					<a href="${createLink(controller:'user', action:'adminAddUser')}" class="btn btn-primary">Add User</a>
				
				</div>
				
				<div class="col-md-2">
					<a href="${createLink(controller:'admin', action:'importUsers')}" class="btn btn-primary">Import Users</a>
				</div>
				
				<div class="col-md-8">&nbsp;</div>
				
			</div>
	
		<div class="flash" style="margin-top:25px;color:red;">
			<g:message code= "${flash.message}" default="${flash.message}" />
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
						<td><a href="${createLink(controller:'user', action:'adminEditUser', params:[id:aUser.uuid])}">${aUser.id}</a></td>
						<!-- <td>${aUser.uuid}</td> -->
						<td>${aUser.fullName}</td>
						<td>${aUser.userId}</td>
						<td>${aUser.displayName}</td>
						<td>${aUser.email}</td>
						<td>disabled: ${aUser.disabled}</td>
						<td>
							<g:if test="${aUser.disabled == false}">
								<a href="${createLink(controller:'user', action:'disableUser', params:[id:aUser.uuid])}">disable user</a></td>
							</g:if>
							<g:else>
								<a href="${createLink(controller:'user', action:'enableUser', params:[id:aUser.uuid])}">enable user</a></td>
							</g:else>
							
						<td><a href="${createLink(controller:'user', action:'deleteUser', params:[id:aUser.uuid])}">delete user</a></td>
						
					</tr>
				</g:each>
			</table>
		</div>
		
		</div>
	</body>
</html>