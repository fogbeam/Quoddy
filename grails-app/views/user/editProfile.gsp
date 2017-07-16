<%@ page import="org.apache.shiro.SecurityUtils"%>

<html>
<head>
<title>Quoddy: Edit Profile</title>
<meta name="layout" content="user_profile" />
<nav:resources />
<g:render template="/userProfileJS"></g:render>
</head>
<body>
	<!-- start body content -->
	<div class="row">

		<div class="col-md-12"
			style="background-color: #F6F7F8; min-height: 85px;">

			<div style="float: left;">
				<g:form controller="user" action="saveProfileAvatarPic"
					id="profileFormAvatarPic" name="profileFormAvatarPic"
					enctype="multipart/form-data">

					<div
						style="background: #333; border: 5px solid #FEFEFE; height: 150px; margin-bottom: 6px; margin-left: 10px; margin-top: 6px; width: 180px;">
						<img id="profilePicImg" name="profilePicImg"
							style="float: left; height: 150px; width: 180px;"
							src="${createLink(controller:'profilePic',action:'full',id:profileToEdit.userId)}" />
					</div>

					<input type="hidden" name="userUuid" id="userUuid"
						value="${profileToEdit.userUuid}"></input>
					<input style="display: none;" type="file" name="your_photo"
						id="your_photo" value="|" title="|" />
					<a style="display: none;" id="save_photo" name="save_photo"
						href="#">save</a>
					<a style="display: none;" id="cancel_photo" name="cancel_photo"
						href="#">cancel</a>

				</g:form>
			</div>

			<div
				style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
				<label>Title:</label>

				<g:if
					test="${profileToEdit?.title == null || profileToEdit.title.isEmpty() }">
					<span name="title" id="title">...</span>
				</g:if>
				<g:else>
					<span name="title" id="title">
						${profileToEdit.title}
					</span>
				</g:else>

				<g:form id="saveProfileTitle" name="saveProfileTitle"
					controller="user" action="saveProfileTitle">
					<g:textField name="titleInput" id="titleInput" value=""
						style="display:none;" />
				</g:form>
				<a style="display: none;" id="save_title" name="save_title" href="#">save</a>
				<a style="display: none;" id="cancel_title" name="cancel_title"
					href="#">cancel</a>

			</div>


			<div
				style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">

				<label for="summary">Description:</label>

				<g:if
					test="${profileToEdit?.summary == null || profileToEdit.summary.isEmpty() }">
					<span name="summary" id="summary"> ... </span>
				</g:if>
				<g:else>
					<span name="summary" id="summary"> ${profileToEdit?.summary}
					</span>
				</g:else>

				<g:form id="saveProfileSummary" name="saveProfileSummary"
					controller="user" action="saveProfileSummary">
					<g:textField name="summaryInput" id="summaryInput" value=""
						style="display:none;" />
				</g:form>
				<a style="display: none;" id="save_summary" name="save_summary"
					href="#">save</a> <a style="display: none;" id="cancel_summary"
					name="cancel_summary" href="#">cancel</a>
			</div>


			<div
				style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
				<label>Phone:</label>

				<g:if
					test="${profileToEdit?.primaryPhone == null || profileToEdit.primaryPhone.isEmpty() }">
					<span name="primaryPhone" id="primaryPhone"
						style="display: inline;">...</span>
				</g:if>
				<g:else>
					<span name="primaryPhone" id="primaryPhone"
						style="display: inline;">
						${profileToEdit.primaryPhone}
					</span>
				</g:else>

				<g:form id="saveProfilePrimaryPhone" name="saveProfilePrimaryPhone"
					controller="user" action="saveProfilePrimaryPhone">
					<g:textField name="primaryPhoneInput" id="primaryPhoneInput"
						value="" style="display:none;" />
				</g:form>
				<a style="display: none;" id="save_primary_phone"
					name="save_primary_phone" href="#">save</a> <a
					style="display: none;" id="cancel_primary_phone"
					name="cancel_primary_phone" href="#">cancel</a>


			</div>


			<div
				style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
				<label>Instant Messenger:</label>

				<g:if
					test="${profileToEdit?.primaryInstantMessenger == null || profileToEdit.primaryInstantMessenger.isEmpty() }">
					<span name="primaryInstantMessenger" id="primaryInstantMessenger"
						style="display: inline;">...</span>
				</g:if>
				<g:else>
					<span name="primaryInstantMessenger" id="primaryInstantMessenger"
						style="display: inline;">
						${profileToEdit.primaryInstantMessenger}
					</span>
				</g:else>

				<g:form id="saveProfilePrimaryInstantMessenger"
					name="saveProfilePrimaryInstantMessenger" controller="user"
					action="saveProfilePrimaryInstantMessenger">
					<g:textField name="primaryInstantMessengerInput"
						id="primaryInstantMessengerInput" value="" style="display:none;" />
				</g:form>
				<a style="display: none;" id="save_primary_instant_messenger"
					name="save_primary_instant_messenger" href="#">save</a> <a
					style="display: none;" id="cancel_primary_instant_messenger"
					name="cancel_primary_instant_messenger" href="#">cancel</a>

			</div>


			<p />

			<div
				style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
				<label>Email:</label>
				<g:if
					test="${profileToEdit?.primaryEmail == null || profileToEdit.primaryEmail.isEmpty() }">
					<span name="primaryEmail" id="primaryEmail"
						style="display: inline;">...</span>
				</g:if>
				<g:else>
					<span name="primaryEmail" id="primaryEmail"
						style="display: inline;">
						${profileToEdit.primaryEmail}
					</span>
				</g:else>

				<g:form id="saveProfilePrimaryEmail" name="saveProfilePrimaryEmail"
					controller="user" action="saveProfilePrimaryEmail">
					<g:textField name="primaryEmailInput" id="primaryEmailInput"
						value="" style="display:none;" />
				</g:form>
				<a style="display: none;" id="save_primary_email"
					name="save_primary_email" href="#">save</a> <a
					style="display: none;" id="cancel_primary_email"
					name="cancel_primary_email" href="#">cancel</a>


			</div>

			<div
				style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
				<label>Location:</label>

				<g:if
					test="${profileToEdit?.location == null || profileToEdit.location.isEmpty() }">
					<span name="location" id="location" style="display: inline;">...</span>
				</g:if>
				<g:else>
					<span name="location" id="location" style="display: inline;">
						${profileToEdit.location}
					</span>
				</g:else>

				<g:form id="saveProfileLocation" name="saveProfileLocation"
					controller="user" action="saveProfileLocation">
					<g:textField name="locationInput" id="locationInput" value=""
						style="display:none;" />
				</g:form>
				<a style="display: none;" id="save_location" name="save_location"
					href="#">save</a> <a style="display: none;" id="cancel_location"
					name="cancel_location" href="#">cancel</a>

			</div>

			<div
				style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 200px; vertical-align: top;">
				<label>.plan</label>

				<g:if
					test="${profileToEdit?.dotPlan == null || profileToEdit.dotPlan.isEmpty() }">
					<span name="dotPlan" id="dotPlan" style="display: inline;">...</span>
				</g:if>
				<g:else>
					<span name="dotPlan" id="dotPlan" style="display: inline;">
						${profileToEdit.dotPlan}
					</span>
				</g:else>

				<g:form id="saveProfileDotPlan" name="saveProfileDotPlan"
					controller="user" action="saveProfileDotPlan">
					<g:textField name="dotPlanInput" id="dotPlanInput" value=""
						style="display:none;" />
				</g:form>
				<a style="display: none;" id="save_dot_plan" name="save_dot_plan"
					href="#">save</a> <a style="display: none;" id="cancel_dot_plan"
					name="cancel_dot_plan" href="#">cancel</a>

			</div>

		</div>
	</div>

	<div class="row">
		<div class="col-md-12"
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
					<li><a href="#tab3" data-toggle="tab">Education and Skills</a>
					</li>
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
							<ul list-style-type: none;>
								<g:each in="${profileToEdit.phoneNumbers}" var="phoneNumber">
									<li>
										${phoneNumber.address}
									</li>
								</g:each>
							</ul>
						</div>

						<div
							style="display: inline-block; vertical-align: top; height: 80px; width: 220px; margin-top: 7px; padding-top: 5px; padding-left: 5px;">
							<label>Email</label>
							<ul list-style-type: none;>
								<g:each in="${profileToEdit.emailAddresses}" var="email">
									<li>
										${email.address}
									</li>
								</g:each>
							</ul>
						</div>
						<div
							style="display: inline-block; vertical-align: top; height: 80px; width: 220px; margin-top: 7px; padding-top: 5px; padding-left: 5px;">
							<label>Instant Messenger</label>
							<ul list-style-type: none;>
								<g:each in="${profileToEdit.instantMessengers}" var="imAddress">
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
							<ul list-style-type: none;>
								<g:each in="${profileToEdit.educationHistory}" var="educationInstance">
									<li>
										<span>${educationInstance.institutionName}</span>
										-
										<span>${educationInstance.courseOfStudy}</span>
										- 
										<span>${months.find{ m -> m.id == Integer.parseInt( educationInstance.monthFrom ) }.text}</span>
										<span>${educationInstance.yearFrom}</span>
										to
										<span>${months.find{ m -> m.id == Integer.parseInt( educationInstance.monthTo ) }.text}</span>
										<span>${educationInstance.yearTo}</span>
									</li>
								</g:each>
							</ul>
							<a href="#addEducationHistoryModal" role="button" class="btn" data-toggle="modal" style="width:80px;">add</a>
						</div>
					</div>
					<div class="tab-pane" id="tab4">
						
						<div
							style="margin-left: 35px; display: inline-block; vertical-align: top; margin-top: 7px; padding-top: 5px; padding-left: 5px;">
							<label>Work and Projects</label>
							<div style="display:inline-block;width:300px;vertical-align:top;">
								<label>Employment</label>
								<div>
									<ul list-style-type: none;>
										<g:each in="${profileToEdit.employmentHistory}" var="employmentHistoryInstance">
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
									<a href="#addEmploymentHistoryModal" role="button" class="btn" data-toggle="modal" style="width:80px;">add</a>
								</div>
							</div>
							<div style="display:inline-block;margin-left:10px;width:300px;vertical-align:top">
								<label>Projects</label>
								<div>
								</div>
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


	<!--  new Bootstrap Modal dialogs -->

	<!-- Button to trigger modal -->


	<!-- Modal -->
	<div id="addEducationHistoryModal" class="modal hide fade" tabindex="-1" role="dialog"
		aria-labelledby="addEducationHistoryModalLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h3 id="addEducationHistoryModalLabel">Add Education</h3>
		</div>
		<div class="modal-body">
			
			<g:form id="addEducationHistoryForm" name="addEducationHistoryForm" controller="user" method="POST" action="addEducationHistory">
			
				<fieldset id="educationHistoryTemplate">
					<div>
						<label for="institutionName">Institution Name:</label>
						<g:textField name="institutionName" value="" />
					</div>

					<div>
						<label for="monthFrom">Time Period:</label>
						<div>
							
							<div>
								<label style="display:inline-block;width:45px;">From:</label>
								<g:select style="display:inline-block;" name="monthFrom" from="${months}" value="tbd"
								optionKey="id" noSelection="${['':'Choose...']}" optionValue="text" />
								&nbsp;
						
								<g:select style="display:inline-block;" name="yearFrom" from="${years}" value="tbd"
								optionKey="id" noSelection="${['':'Choose...']}" optionValue="text" />
							</div>
							<div>
								<label style="display:inline-block;width:45px;">To:</label>
								<g:select style="display:inline-block;" name="monthTo" from="${months}" value="tbd"
								optionKey="id" noSelection="${['':'Choose...']}" optionValue="text" />
								&nbsp;
								<!-- <g:textField name="yearTo" class="yearInput" /> -->
								<g:select style="display:inline-block;" name="yearTo" from="${years}" value="tbd"
								optionKey="id" noSelection="${['':'Choose...']}" optionValue="text" />
							</div>
						</div>
					</div>

					<label for="major">Major / Course of Study:</label>
					<g:textField name="major" id="major" />
				</fieldset>
			
			</g:form>
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
			<button id="addEducationBtn" name="addEducationBtn" class="btn btn-primary">Save changes</button>
		</div>
	</div>

	<div id="addEmploymentHistoryModal" class="modal hide fade" tabindex="-1" role="dialog"
		aria-labelledby="addEmploymentHistoryModalLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h3 id="addEmploymentHistoryModalLabel">Add Employment</h3>
		</div>
		<div class="modal-body">
			
			<g:form id="addEmploymentHistoryForm" name="addEmploymentHistoryForm" controller="user" method="POST" action="addEmploymentHistory">
			
				<fieldset id="employmentHistoryTemplate">
					<div>
						<label for="institutionName">Organization Name:</label>
						<g:textField name="companyName" value="" />
					</div>

					<div>
						<label for="monthFrom">Time Period:</label>
						<div>
							
							<div>
								<label style="display:inline-block;width:45px;">From:</label>
								<g:select style="display:inline-block;" name="monthFrom" from="${months}" value="tbd"
								optionKey="id" noSelection="${['':'Choose...']}" optionValue="text" />
								&nbsp;
						
								<g:select style="display:inline-block;" name="yearFrom" from="${years}" value="tbd"
								optionKey="id" noSelection="${['':'Choose...']}" optionValue="text" />
							</div>
							<div>
								<label style="display:inline-block;width:45px;">To:</label>
								<g:select style="display:inline-block;" name="monthTo" from="${months}" value="tbd"
								optionKey="id" noSelection="${['':'Choose...']}" optionValue="text" />
								&nbsp;
								<!-- <g:textField name="yearTo" class="yearInput" /> -->
								<g:select style="display:inline-block;" name="yearTo" from="${years}" value="tbd"
								optionKey="id" noSelection="${['':'Choose...']}" optionValue="text" />
							</div>
						</div>
					</div>

					<label for="major">Title</label>
					<g:textField name="title" id="title" />
				</fieldset>
			
			</g:form>
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
			<button id="addEmploymentBtn" name="addEmploymentBtn" class="btn btn-primary">Save changes</button>
		</div>
	</div>








	<!--  hidden stuff / templates for adding blocks -->
	<fieldset id="contactAddressTemplate" style="display: none;">

		<div>
			<g:select name="contactAddress[?].serviceType" from="${contactTypes}"
				value="" optionKey="id" noSelection="${['':'Select One...']}"
				optionValue="text" />

			<g:textField name="contactAddress[?].address" value="" />
		</div>
		<div>
			<input type="hidden" id="contactAddress[?].contactAddressId"
				name="contactAddress[?].contactAddressId" value="-1" />
		</div>
	</fieldset>

</body>
</html>