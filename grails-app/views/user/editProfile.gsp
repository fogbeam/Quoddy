<%@ page import="org.apache.shiro.SecurityUtils" %>

<html>
    <head>
        <title>Quoddy: Edit Profile</title>
        <meta name="layout" content="user_profile" />
        <nav:resources />
		<g:render template="/userProfileJS"></g:render>
    </head>
    	<body>
			<!-- start body content -->
			<div class="row-fluid">
				
				<div class="span12" style="background-color:#F6F7F8;min-height:85px;">
					
					<div style="float:left;">				
					<g:form controller="user" action="saveProfileAvatarPic" id="profileFormAvatarPic" name="profileFormAvatarPic" enctype="multipart/form-data" >					
							
						<div style="background:#333; border: 5px solid #FEFEFE; height: 150px; margin-bottom: 6px; margin-left:10px; margin-top:6px; width: 180px;">
							<img id="profilePicImg" name="profilePicImg" style="float:left;height:150px;width:180px;" src="${createLink(controller:'profilePic',action:'full',id:profileToEdit.userId)}" />
						</div>
						
						<input type="hidden" name="userUuid" id="userUuid" value="${profileToEdit.userUuid}"></input>		
						<input style="display:none;" type="file" name="your_photo" id="your_photo" value="|" title="|" />
						<a style="display:none;" id="save_photo" name="save_photo" href="#">save</a> <a style="display:none;" id="cancel_photo" name="cancel_photo" href="#">cancel</a>
									
					</g:form>
					</div>
					
					<div style="display:inline-block;margin-left:20px;margin-top:15px;height:110px;width:200px;vertical-align:top;">
						<label>Title:</label>
						
						<g:if test="${profileToEdit?.title == null || profileToEdit.title.isEmpty() }">
							<span name="title" id="title" >...</span>
						</g:if>
						<g:else>
							<span name="title" id="title" >${profileToEdit.title}</span>
						</g:else>
						
						<g:form id="saveProfileTitle" name="saveProfileTitle" controller="user" action="saveProfileTitle">
							<g:textField name="titleInput" id="titleInput" value="" style="display:none;" />
						</g:form>
						<a style="display:none;" id="save_title" name="save_title" href="#">save</a> 
						<a style="display:none;" id="cancel_title" name="cancel_title" href="#">cancel</a>			
						
					</div>
				
				
					<div style="display:inline-block;margin-left:20px;margin-top:15px;height:110px;width:200px;vertical-align:top;">
						
						<label for="summary">Description:</label>
						
							<g:if test="${profileToEdit?.summary == null || profileToEdit.summary.isEmpty() }">
								<span name="summary" id="summary">
								...
								</span>
							</g:if>
							<g:else>
								<span name="summary" id="summary">
									${profileToEdit?.summary}
								</span>
							</g:else>
						
						<g:form id="saveProfileSummary" name="saveProfileSummary" controller="user" action="saveProfileSummary">
							<g:textField name="summaryInput" id="summaryInput" value="" style="display:none;" />
						</g:form>
						<a style="display:none;" id="save_summary" name="save_summary" href="#">save</a> <a style="display:none;" id="cancel_summary" name="cancel_summary" href="#">cancel</a>			
					</div>
					
								
					<div style="display:inline-block;margin-left:20px;margin-top:15px;height:110px;width:200px;vertical-align:top;">
						<label>Phone:</label>
						
						<g:if test="${profileToEdit?.primaryPhone == null || profileToEdit.primaryPhone.isEmpty() }">
							<span name="primaryPhone" id="primaryPhone" style="display:inline;">...</span>
						</g:if>
						<g:else>
							<span name="primaryPhone" id="primaryPhone" style="display:inline;">${profileToEdit.primaryPhone}</span>
						</g:else>						

						<g:form id="saveProfilePrimaryPhone" name="saveProfilePrimaryPhone" controller="user" action="saveProfilePrimaryPhone">
							<g:textField name="primaryPhoneInput" id="primaryPhoneInput" value="" style="display:none;" />
						</g:form>
						<a style="display:none;" id="save_primary_phone" name="save_primary_phone" href="#">save</a> 
						<a style="display:none;" id="cancel_primary_phone" name="cancel_primary_phone" href="#">cancel</a>	

						
					</div>


					<div style="display:inline-block;margin-left:20px;margin-top:15px;height:110px;width:200px;vertical-align:top;">
						<label>Instant Messenger:</label>
						
						<g:if test="${profileToEdit?.primaryInstantMessenger == null || profileToEdit.primaryInstantMessenger.isEmpty() }">
							<span name="primaryInstantMessenger" id="primaryInstantMessenger" style="display:inline;">...</span>
						</g:if>
						<g:else>
							<span name="primaryInstantMessenger" id="primaryInstantMessenger" style="display:inline;">${profileToEdit.primaryInstantMessenger}</span>
						</g:else>						

						<g:form id="saveProfilePrimaryInstantMessenger" name="saveProfilePrimaryInstantMessenger" controller="user" action="saveProfilePrimaryInstantMessenger">
							<g:textField name="primaryInstantMessengerInput" id="primaryInstantMessengerInput" value="" style="display:none;" />
						</g:form>
						<a style="display:none;" id="save_primary_instant_messenger" name="save_primary_instant_messenger" href="#">save</a> 
						<a style="display:none;" id="cancel_primary_instant_messenger" name="cancel_primary_instant_messenger" href="#">cancel</a>	
						
					</div>

					
					<p />
					
					<div style="display:inline-block;margin-left:20px;margin-top:15px;height:110px;width:200px;vertical-align:top;">
						<label>Email:</label>
						<g:if test="${profileToEdit?.primaryEmail == null || profileToEdit.primaryEmail.isEmpty() }">
							<span name="primaryEmail" id="primaryEmail" style="display:inline;">...</span>
						</g:if>
						<g:else>
							<span name="primaryEmail" id="primaryEmail" style="display:inline;">${profileToEdit.primaryEmail}</span>
						</g:else>
						
						<g:form id="saveProfilePrimaryEmail" name="saveProfilePrimaryEmail" controller="user" action="saveProfilePrimaryEmail">
							<g:textField name="primaryEmailInput" id="primaryEmailInput" value="" style="display:none;" />
						</g:form>
						<a style="display:none;" id="save_primary_email" name="save_primary_email" href="#">save</a> 
						<a style="display:none;" id="cancel_primary_email" name="cancel_primary_email" href="#">cancel</a>	
					
							
					</div>
					
					<div style="display:inline-block;margin-left:20px;margin-top:15px;height:110px;width:200px;vertical-align:top;">
						<label>Location:</label>
						
						<g:if test="${profileToEdit?.location == null || profileToEdit.location.isEmpty() }">
							<span name="location" id="location" style="display:inline;">...</span>
						</g:if>
						<g:else>
							<span name="location" id="location" style="display:inline;">${profileToEdit.location}</span>
						</g:else>
						
						<g:form id="saveProfileLocation" name="saveProfileLocation" controller="user" action="saveProfileLocation">
							<g:textField name="locationInput" id="locationInput" value="" style="display:none;" />
						</g:form>
						<a style="display:none;" id="save_location" name="save_location" href="#">save</a> 
						<a style="display:none;" id="cancel_location" name="cancel_location" href="#">cancel</a>	
					
					</div>
					
					<div style="display:inline-block;margin-left:20px;margin-top:15px;height:110px;width:200px;vertical-align:top;">
						<label>.plan</label>
	
						<g:if test="${profileToEdit?.dotPlan == null || profileToEdit.dotPlan.isEmpty() }">
							<span name="dotPlan" id="dotPlan" style="display:inline;">...</span>
						</g:if>
						<g:else>
							<span name="dotPlan" id="dotPlan" style="display:inline;">${profileToEdit.dotPlan}</span>
						</g:else>
	
						<g:form id="saveProfileDotPlan" name="saveProfileDotPlan" controller="user" action="saveProfileDotPlan">
							<g:textField name="dotPlanInput" id="dotPlanInput" value="" style="display:none;" />
						</g:form>
						<a style="display:none;" id="save_dot_plan" name="save_dot_plan" href="#">save</a> 
						<a style="display:none;" id="cancel_dot_plan" name="cancel_dot_plan" href="#">cancel</a>	
						
					</div>
							
				</div>
			</div>
			
			<div class="row-fluid">
			<div class="span12" style="margin-top:15px;background-color:#F6F7F8;min-height:85px;">
				
				<div class="tabbable tabs-left">
					<!-- Only required for left/right tabs -->
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#tab1" data-toggle="tab">Overview</a>
						</li>
						<li>
							<a href="#tab2" data-toggle="tab">Contact and Basic Info</a>
						</li>
						<li>
							<a href="#tab3" data-toggle="tab">Education and Skills</a>
						</li>
						<li>
							<a href="#tab4" data-toggle="tab">Work and Projects</a>
						</li>
						<li>
							<a href="#tab5" data-toggle="tab">Details and Interests</a>
						</li>																		
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="tab1">
							<p>Overview Tab</p>
						</div>
						<div class="tab-pane" id="tab2">
							<p>Contact and Basic Info Tab</p>
						</div>
						<div class="tab-pane" id="tab3">
							<p>Education and Skills Tab</p>
						</div>
						<div class="tab-pane" id="tab4">
							<p>Work and Projects Tab</p>
						</div>
						<div class="tab-pane" id="tab5">
							<p>Details and Interests Tab</p>
						</div>						
					</div>
				</div>			
				
    		</div>
    		</div>
    		
    		
    		
    		<!--  hidden stuff / templates for adding blocks -->
			<fieldset id="educationHistoryTemplate" style="display:none;">
				<div>
            		<label for="education[?].institutionName">Institution Name:</label>
					<g:textField name="education[?].institutionName" value="" />
      			</div>

        		<div>
					<label for="education[?].monthFrom">Time Period:</label>
					<g:select name="education[?].monthFrom" from="${months}" value="tbd" optionKey="id"
			   		noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[?].yearFrom" class="yearInput" />
			   		to <g:select name="education[?].monthTo" from="${months}" value="tbd" optionKey="id"
			   		noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[?].yearTo" class="yearInput" />
			
				</div>
			
				<label for="education[?].major">Major / Course of Study:</label>
				<g:textField name="education[?].major" id="education[?].major" />
      			<input type="hidden" id="education[?].educationalExperienceId" name="education[?].educationalExperienceId" value="-1" />
      			
			</fieldset>
    
    		<fieldset id="contactAddressTemplate" style="display:none;">
     	
      			<div>
        			<g:select name="contactAddress[?].serviceType" from="${contactTypes}" value="" optionKey="id" 
						noSelection="${['':'Select One...']}" optionValue="text" />
						
      				<g:textField name="contactAddress[?].address" value="" />
      			</div>
      			<div>
		    	  	<input type="hidden" id="contactAddress[?].contactAddressId" name="contactAddress[?].contactAddressId" value="-1" />
	      		</div>     	
    		</fieldset>
    
			<fieldset id="employmentHistoryTemplate" style="display:none;" >
			
				<div>
					<label for="employment[?].companyName">Company Name:</label>
					<g:textField name="employment[?].companyName" />
				</div>
				<div>
					<label for="employment[?].title">Title:</label>
					<g:textField name="employment[?].title" />
				</div>
				<div>
					<label for="employment[?].monthFrom">Time Period:</label>
					<g:select name="employment[?].monthFrom" from="${months}" value="" optionKey="id"
					   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[?].yearFrom" class="yearInput" />
						   to <g:select name="employment[?].monthTo" from="${months}" value="" optionKey="id"
					   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[?].yearTo" class="yearInput" />
				</div>
				<div>
				   <label for="employment[?].description">Description:</label>
				   <g:textArea name="employment[?].description" value="" rows="5" cols="40"/>
				</div>
				<div>
					<input type="hidden" id="employment[?].historicalEmploymentId" name="employment[?].historicalEmploymentId" value="-1" />   
				</div>
			</fieldset>
			
			<div class="clear"></div>	
	</body>
</html>