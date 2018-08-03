<html>
	
	<head>
		<title>Quoddy: View User</title>
        <meta name="layout" content="user_profile"/>
        <nav:resources />

        <g:javascript library="typeahead" />        
        <g:javascript library="user_profile" />

        
	</head>
	
	<body>
		
		<div id="bodyContent" class="span8">
		
			<g:if test="${flash.message}">
	        	<div class="flash">
	             ${flash.message}
	        	</div>
	   		</g:if>

			<div class="viewUserButtons">
				<div class="btn-group">
					<g:link controller="user" action="viewUser" params="[userId:user.userId]" elementid="btn1" name="btn1" type="button" class="btn btn-info btn-small">Timeline</g:link>
					<g:link controller="user" action="viewUserProfile" params="[userId:user.userId]" elementid="btn2" name="btn2" type="button" class="btn btn-info btn-small">Profile</g:link>
					<button id="btnAddToFriends.${user.userId}" name="btnAddToFriends.${user.userId}" type="button" class="btn btn-info btn-small">Add To Friends</button>
					<button id="btnFollowUser.${user.userId}" name="btnFollowUser.${user.userId}" type="button" class="btn btn-info btn-small">Follow User</button>
					<button id="btnAddAnnotation.${user.userId}" name="btnAddAnnotation.${user.userId}" type="button" class="btn btn-info btn-small">Annotate</button>
					<button id="btn6" name="btn6" type="button" class="btn btn-info btn-small">More...</button>
				</div>		
			</div>
			
			<hr />
			
			<g:if test="${activities != null}">

				<div id="activityStream">

					<g:render template="/activityStream" />

				</div>
			</g:if>			
		</div>
		
		<!-- hidden dialog for adding annotations -->
		<div id="annotationDialog" name="annotationDialog" style="display:none;" title="Add Annotation">
			<script id="predicatesJSON" name="predicatesJSON" >
				${predicatesJSON}
			</script>
			<g:formRemote name="addAnnotationForm" url="[controller: 'user', action:'addAnnotation']">
	
				<select name="annotationPredicate" >
					<g:each in="${predicates}" var="predicate">
					 	<option value="${predicate.qualifiedName}">${predicate.label}</option>
					</g:each>
				</select>
	
				<!--  text of an (optional) comment -->
				<input id="annotationObject" name="annotationObject" type="text" value="" />
	
				<input id="userId" name="userId" type="hidden" value="" />
				<input id="annotationObjectQN" name="annotationObjectQN" type="hidden" value="" />
				<br />
			</g:formRemote>
		</div>
		
	</body>
	
</html>