<html>

	<head>
		<title>Quoddy: Import Users</title>
		<meta name="layout" content="admin_dialog" />
	</head>
	
	<body>
		<p />
		<div>
		<g:form action="addImportedUsers" >
			<table>
				<tr>
					<th>&nbsp;</th>
					<th>Name</th>
				</tr>
				<g:each in="${ldapPersons}">
	     		<tr>
	     			<td>
	     				<g:checkBox name="importUser.${it.uid}" value="${false}" />
	     			</td>
	     			<td>${it.fullName}</td>
	     			<td>${it.displayName}</td>
	     			<td>${it.email}</td>
	     			
				</tr>
				</g:each>	
			</table>
			<input type="submit" value="Add Users" />
		</g:form>
		</div>	
	</body>

</html>