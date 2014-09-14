<html>

<head>
<title>Quoddy: View User</title>
<meta name="layout" content="user_profile" />
<nav:resources />

<g:javascript library="typeahead" />
<g:javascript library="user_profile" />

</head>

<body>

	<div id="bodyContent">
		
		<div class="row-fluid">
			<div class="span12">
				<g:if test="${flash.message}">
					<div class="flash">
					${flash.message}
					</div>
				</g:if>
	
	
				<div class="viewUserButtons">
					<div class="btn-group">
						<g:link controller="user" action="viewUser"
							params="[userId:user.userId]" elementid="btn1" name="btn1"
							type="button" class="btn btn-info btn-small">Timeline</g:link>
						<g:link controller="user" action="viewUserProfile"
							params="[userId:user.userId]" elementid="btn2" name="btn2"
							type="button" class="btn btn-info btn-small">Profile</g:link>
						<button id="btnAddToFriends.${user.userId}"
							name="btnAddToFriends.${user.userId}" type="button"
							class="btn btn-info btn-small">Add To Friends</button>
						<button id="btnFollowUser.${user.userId}"
							name="btnFollowUser.${user.userId}" type="button"
							class="btn btn-info btn-small">Follow User</button>
						<button id="btnAddAnnotation.${user.userId}"
							name="btnAddAnnotation.${user.userId}" type="button"
							class="btn btn-info btn-small">Annotate</button>
						<button id="btn6" name="btn6" type="button"
							class="btn btn-info btn-small">More...</button>
					</div>
				</div>
	
				<hr />
			</div>
		</div>
		
		<div class="row-fluid">

			<div class="span12"
				style="background-color: #F6F7F8; min-height: 85px;">

				<div style="float: left;">
					<g:form controller="user" action="saveProfileAvatarPic"
						id="profileFormAvatarPic" name="profileFormAvatarPic"
						enctype="multipart/form-data">

						<div
							style="background: #333; border: 5px solid #FEFEFE; height: 150px; margin-bottom: 6px; margin-left: 10px; margin-top: 6px; width: 180px;">
							<img id="profilePicImg" name="profilePicImg"
								style="float: left; height: 150px; width: 180px;"
								src="${createLink(controller:'profilePic',action:'full',id:user.userId)}" />
						</div>
					</g:form>
				</div>

				<div
					style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
					<label>Title:</label>

					<g:if
						test="${user.profile?.title == null || user.profile.title?.isEmpty() }">
						<span name="title" id="title">...</span>
					</g:if>
					<g:else>
						<span name="title" id="title"> ${user.profile.title}
						</span>
					</g:else>
				</div>


				<div
					style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">

					<label for="summary">Description:</label>

					<g:if
						test="${user.profile?.summary == null || user.profile.summary?.isEmpty() }">
						<span name="summary" id="summary"> ... </span>
					</g:if>
					<g:else>
						<span name="summary" id="summary"> ${user.profile?.summary}
						</span>
					</g:else>

				</div>


				<div
					style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
					<label>Phone:</label>

					<g:if
						test="${user.profile?.primaryPhoneNumber == null || user.profile?.primaryPhoneNumber?.address.isEmpty() }">
						<span name="primaryPhone" id="primaryPhone"
							style="display: inline;">...</span>
					</g:if>
					<g:else>
						<span name="primaryPhone" id="primaryPhone"
							style="display: inline;"> ${user.profile.primaryPhoneNumber.address}
						</span>
					</g:else>

				</div>


				<div
					style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
					<label>Instant Messenger:</label>

					<g:if
						test="${user.profile?.primaryInstantMessenger == null || user.profile.primaryInstantMessenger?.address.isEmpty() }">
						<span name="primaryInstantMessenger" id="primaryInstantMessenger"
							style="display: inline;">...</span>
					</g:if>
					<g:else>
						<span name="primaryInstantMessenger" id="primaryInstantMessenger"
							style="display: inline;"> ${user.profile.primaryInstantMessenger.address}
						</span>
					</g:else>

				</div>


				<p />

				<div
					style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
					<label>Email:</label>
					<g:if
						test="${user.profile?.primaryEmailAddress == null || user.profile.primaryEmailAddress?.address.isEmpty() }">
						<span name="primaryEmail" id="primaryEmail"
							style="display: inline;">...</span>
					</g:if>
					<g:else>
						<span name="primaryEmail" id="primaryEmail"
							style="display: inline;"> ${user.profile.primaryEmailAddress.address}
						</span>
					</g:else>

				</div>

				<div
					style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
					<label>Location:</label>

					<g:if
						test="${user.profile?.location == null || user.profile.location?.isEmpty() }">
						<span name="location" id="location" style="display: inline;">...</span>
					</g:if>
					<g:else>
						<span name="location" id="location" style="display: inline;">
							${user.profile.location}
						</span>
					</g:else>
				</div>

				<div
					style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
					<label>.plan</label>

					<g:if
						test="${user.profile?.dotPlan == null || user.profile.dotPlan?.isEmpty() }">
						<span name="dotPlan" id="dotPlan" style="display: inline;">...</span>
					</g:if>
					<g:else>
						<span name="dotPlan" id="dotPlan" style="display: inline;">
							${user.profile.dotPlan}
						</span>
					</g:else>

				</div>

			</div>
		</div>

		<div class="row-fluid">
			<div class="span12"
				style="margin-top: 15px; background-color: #F6F7F8; min-height: 85px;">

				<div class="tabbable tabs-left">
					<!-- Only required for left/right tabs -->
					<ul class="nav nav-tabs">

						<!--
						<li class="active">
							<a href="#tab1" data-toggle="tab">Overview</a>
						</li>
						-->

						<li class="active"><a href="#tab2" data-toggle="tab">Contact
								and Basic Info</a></li>
						<li><a href="#tab3" data-toggle="tab">Education and
								Skills</a></li>
						<li><a href="#tab4" data-toggle="tab">Work and Projects</a></li>
						<li><a href="#tab5" data-toggle="tab">Details and
								Interests</a></li>
					</ul>
					<div class="tab-content">
						<!--
						<div class="tab-pane active" id="tab1">
							<p>Overview Tab</p>
						</div>
						-->

						<div class="tab-pane active" id="tab2">

							<div
								style="margin-left: 35px; display: inline-block; vertical-align: top; height: 80px; width: 220px; margin-top: 7px; padding-top: 5px; padding-left: 5px;">
								<label>Phone</label>
								<ul style="list-style-type: none;padding-left:0px;margin-left:0px;">
									<g:each in="${user.profile.phoneNumbers}" var="phoneNumber">
										<li>
											${phoneNumber.address}
										</li>
									</g:each>
								</ul>
							</div>
							<div
								style="display: inline-block; vertical-align: top; height: 80px; width: 220px; margin-top: 7px; padding-top: 5px; padding-left: 5px;">
								<label>Email</label>
								<ul style="list-style-type: none;padding-left:0;margin-left:0px;">
									<g:each in="${user.profile.emailAddresses}" var="email">
										<li>
											${email.address}
										</li>
									</g:each>
								</ul>
							</div>
							<div
								style="display: inline-block; vertical-align: top; height: 80px; width: 220px; margin-top: 7px; padding-top: 5px; padding-left: 5px;">
								<label>Instant Messenger</label>
								<ul style="list-style-type: none;padding-left:0;margin-left:0px;">
									<g:each in="${user.profile.instantMessengerAddresses}" var="imAddress">
										<li>
											${imAddress.address}
										</li>
									</g:each>
								</ul>
							</div>


						</div>
						<div class="tab-pane" id="tab3">
							<div
								style="margin-left: 35px; display: inline-block; vertical-align: top; margin-top: 7px; padding-top: 5px; padding-left: 5px;">
								<label>Education</label>
								<ul style="list-style-type: none;padding-left:0;margin-left:0px;">
									<g:each in="${user.profile.educationHistory}"
										var="educationInstance">
										<li><span> ${educationInstance.institutionName}
										</span> - <span> ${educationInstance.courseOfStudy}
										</span> - <span> ${months.find{ m -> m.id == Integer.parseInt( educationInstance.monthFrom ) }?.text}
										</span> <span> ${educationInstance.yearFrom}
										</span> to <span> ${months.find{ m -> m.id == Integer.parseInt( educationInstance.monthTo ) }?.text}
										</span> <span> ${educationInstance.yearTo}
										</span></li>
									</g:each>
								</ul>
							</div>
						</div>
						<div class="tab-pane" id="tab4">

							<div
								style="margin-left: 35px; display: inline-block; vertical-align: top; margin-top: 7px; padding-top: 5px; padding-left: 5px;">
								<label>Work and Projects</label>
								<div
									style="display: inline-block; width: 300px; vertical-align: top;">
									<label>Employment</label>
									<div>
										<ul style="list-style-type: none;padding-left:0;margin-left:0px;">
											<g:each in="${user.profile.employmentHistory}"
												var="employmentHistoryInstance">
												<li>
													<span> ${employmentHistoryInstance.companyName}</span> 
													- 
													<span> ${employmentHistoryInstance.title}</span> 
													- 
													<span> ${months.find{ m -> m.id == Integer.parseInt( employmentHistoryInstance.monthFrom ) }?.text}</span> 
													<span> ${employmentHistoryInstance.yearFrom}</span> 
													to 
													<span> ${months.find{ m -> m.id == Integer.parseInt( employmentHistoryInstance.monthTo ) }?.text}</span> 
													<span> ${employmentHistoryInstance.yearTo}</span>
												</li>
											</g:each>
										</ul>
									</div>
								</div>
								<div
									style="display: inline-block; margin-left: 10px; width: 300px; vertical-align: top">
									<label>Projects</label>
									<div></div>
								</div>
							</div>


						</div>
						<div class="tab-pane" id="tab5">
							<p>Details and Interests</p>
						</div>
					</div>
				</div>

			</div>
		</div>


		<!-- hidden dialog for adding annotations -->
		<div id="annotationDialog" name="annotationDialog"
			style="display: none;" title="Add Annotation">
			<script id="predicatesJSON" name="predicatesJSON">
				${predicatesJSON}
			</script>
			<g:formRemote name="addAnnotationForm"
				url="[controller: 'user', action:'addAnnotation']">

				<select name="annotationPredicate">
					<g:each in="${predicates}" var="predicate">
						<option value="${predicate.qualifiedName}">
							${predicate.label}
						</option>
					</g:each>
				</select>

				<!--  text of an (optional) comment -->
				<input id="annotationObject" name="annotationObject" type="text"
					value="" />

				<input id="userId" name="userId" type="hidden" value="" />
				<input id="annotationObjectQN" name="annotationObjectQN"
					type="hidden" value="" />
				<br />
			</g:formRemote>
		</div>
</body>

</html>