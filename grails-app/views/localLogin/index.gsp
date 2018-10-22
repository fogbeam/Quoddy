<!doctype html>
<html>
<head>
<meta name="layout" content="login" />
<title>Welcome to Fogcutter (by Fogbeam Labs)</title>

<asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>
	<div id="content" role="main">
	   
	   <g:if test="${flash.message}">
	       <g:message code="${flash.message}" />
	   </g:if>

		<g:form controller="localLogin" action="login">
			<input type="hidden" name="targetUri" value="${targetUri}" />
			<table>
				<tbody>
					<tr>
						<td>Username:</td>
						<td><input type="text" name="username" value="${username}" /></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="password" value="" /></td>
					</tr>
					<!--
					
					<tr>
						<td>Remember me?:</td>
						<td><g:checkBox name="rememberMe" value="${rememberMe}" /></td>
					</tr>
					-->
					
					<!--  only display this if fogbeam.devmode is on -->
					<g:if test="${grailsApplication.config.fogbeam.devmode}">
						<tr>
						   <td>
						       <select name="useLocal">
						           <option value="false">---</option>
	                                <option value="true">UseLocal</option>
						       </select>
						   </td>
						</tr>
					</g:if>
					
					<tr>
						<td />
						<td><input type="submit" value="Sign in" /></td>
					</tr>
				</tbody>
			</table>
		</g:form>
	</div>
</body>
</html>