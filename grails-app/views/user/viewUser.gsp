 <html>
	
	<head>
		<title>Quoddy: View User</title>
        <meta name="layout" content="user_profile"/>
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
		<!-- Modal -->
		<div class="modal fade" id="addAnnotationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		  <div class="modal-dialog" role="document">
    		<div class="modal-content">
      			<div class="modal-header">
        			<h5 class="modal-title" id="exampleModalLabel">Add Annotation</h5>
        			<button type="button" class="close" data-dismiss="modal" aria-label="Close">
          				<span aria-hidden="true">&times;</span>
        			</button>
      			</div>
      			
      		    <script id="predicatesJSON" name="predicatesJSON">
				   ${raw(predicatesJSON)}
				</script>
      			
      			<div class="modal-body">
					
					<g:formRemote name="addAnnotationForm" url="[controller: 'user', action:'addAnnotation']">

						<select name="annotationPredicate">
							<g:each in="${predicates}" var="predicate">
								<option value="${predicate.qualifiedName}">
									${predicate.label}
								</option>
							</g:each>
						</select>
	
						<!--  text of an (optional) comment -->
						<input id="annotationObject" name="annotationObject" type="text" value="" />
	
						<input id="userId" name="userId" type="hidden" value="" />
						<input id="annotationObjectQN" name="annotationObjectQN" type="hidden" value="" />
					
					</g:formRemote>
					
      			</div>
      			<div class="modal-footer">
        			<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        			<button id="submitAddAnnotation" name="submitAddAnnotation" type="button" class="btn btn-primary">Submit</button>
      			</div>
    		</div>
  		   </div>
	     </div>
		
		
		
		
	</body>
	
</html>