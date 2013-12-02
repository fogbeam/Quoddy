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

			<!-- 
				<span><g:link controller="user" action="addToFollow" params="[userId:user.userId]">follow</g:link></span>
				<span><g:link controller="user" action="addToFriends" params="[userId:user.userId]">add to friends</g:link></span>
							
			 -->

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
			<div class="row">
				<div class="span3 well" style="min-height:400px;" >
					<h4 style="margin-top:0px;margin-bottom:8px;">About</h4>
					<ul id="profileAbout" name="profileAbout">
						<li class="profileFullname">${user.fullName}</li>
						<li><label>User ID: </label> ${user.userId}</li>
						<li><label>Nickname: </label>${user.displayName}</li>
						<li><label>Bio: </label>${user.profile.summary}</li>
						<li>
							<label>Homepage:</label>
					  		<a href="${user.homepage}">${user.homepage}</a>
						</li>
						<li><label>Location:</label>${user.profile.location}</li>
						<li><label>Hometown:</label>${user.profile.hometown}</li>
					</ul>
				</div>
				
				<div class="span4 well" style="min-height:400px;">
					<h4 style="margin-top:0px;margin-bottom:8px;">Connect</h4>
					<!-- TODO: iterate over the user's list of contact addresses -->
					
					<ul  style="list-style-type:none;">
						<li>
							<label>Internal Email:</label>
								<span>${user.email}</span> 
						</li>
						<g:each in="${user.profile.contactAddresses}" var="contactAddress">
							<li style="margin-top:4px; list-style-type:none;">
								<label>${contactAddress.typeMapping[contactAddress.serviceType]}:</label>
								   <span>${contactAddress.address}</span>
							</li>
						</g:each>
					</ul>
					
				</div>
			</div>
			
			<div class="row well" style="min-height:100px;">
				<h4>More...</h4>
			</div>
									
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