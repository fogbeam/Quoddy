<html>
    <head>
        <title>Quoddy: Edit Profile</title>
        <meta name="layout" content="basic"/>
        <nav:resources />
    	<script type="text/javascript">

			var educationHistoryBlocks = 0;
			function addEducationHistoryBlock()
			{
				// alert( 'hi' );

				if( educationHistoryBlocks == 0 )
				{
					// get the beginning count
					var tempVal = $("input#educationHistoryCount").attr('value');
					var intVal = parseInt(tempVal);
					// there will always be at least one existing block, even if it's the initial empty block when there
					// are no entries.  So if this is zero, increment it to one now
					if( intVal == 0 )
					{
						intVal++;
					}
					
					educationHistoryBlocks = intVal;
				}
				else
				{
					// we've been called a second or subsequent time, so just increment
					educationHistoryBlocks = educationHistoryBlocks + 1;
				}

				var edFieldset = $('fieldset#educationHistoryTemplate').clone();
				edFieldset.removeAttr('style').attr("style", "border:none;"); 
				edFieldset.attr( 'id', "education" + educationHistoryBlocks );

				// iterate over the <input> fields in the fieldset and change the ids to reflect the
				// item count
				var $inputKids = edFieldset.find('input');	
				$.each( $inputKids, function(index) 
				{
				
					if( $(this).attr('name').indexOf( ".institutionName" ) != -1 )
					{
						$(this).attr( 'name', "education[" + educationHistoryBlocks + "].institutionName" );
						$(this).attr( 'id', "education[" + educationHistoryBlocks + "].institutionName" );	
					}
					else if( $(this).attr('name').indexOf( ".yearFrom" ) != -1 )
					{
						$(this).attr( 'name', "education[" + educationHistoryBlocks + "].yearFrom" );
						$(this).attr( 'id', "education[" + educationHistoryBlocks + "].yearFrom" );	
					}
					else if( $(this).attr('name').indexOf( ".yearTo" ) != -1 )
					{
						$(this).attr( 'name', "education[" + educationHistoryBlocks + "].yearTo" );
						$(this).attr( 'id', "education[" + educationHistoryBlocks + "].yearTo" );	
					}
					else if( $(this).attr('name').indexOf( ".major" ) != -1 )
					{
						$(this).attr( 'name', "education[" + educationHistoryBlocks + "].major" );
						$(this).attr( 'id', "education[" + educationHistoryBlocks + "].major" );	
					}					
					else if( $(this).attr('name').indexOf( ".educationalExperienceId" ) != -1 )
					{
						$(this).attr( 'name', "education[" + educationHistoryBlocks + "].educationalExperienceId" );
						$(this).attr( 'id', "education[" + educationHistoryBlocks + "].educationalExperienceId" );	
					}					
				});

				// do the same thing for the <select> fields
				var $selectKids = edFieldset.find('select');
				$.each( $selectKids, function(index) 
				{						
					if( $(this).attr('name').indexOf( ".monthFrom" ) != -1 )
					{
						$(this).attr( 'name', "education[" + educationHistoryBlocks + "].monthFrom" );
						$(this).attr( 'id', "education[" + educationHistoryBlocks + "].monthFrom" );
					}
					else if( $(this).attr('name').indexOf( ".monthTo" ) != -1 )
					{
						$(this).attr( 'name', "education[" + educationHistoryBlocks + "].monthTo" );
						$(this).attr( 'id', "education[" + educationHistoryBlocks + "].monthTo" );
					}	
				});


				edFieldset.appendTo( "div#educationHistory" );				
				return false;
			}

    	
			var contactAddressBlocks = 0;
			function addContactAddressBlock()
			{
			
				if( contactAddressBlocks == 0 )
				{
					// get the beginning count
					var tempVal = $("input#contactAddressCount").attr('value');
					var intVal = parseInt(tempVal);
					// there will always be at least one existing block, even if it's the initial empty block when there
					// are no entries.  So if this is zero, increment it to one now
					if( intVal == 0 )
					{
						intVal++;
					}
					
					contactAddressBlocks = intVal;					
				}
				else
				{
					// we've been called a second or subsequent time, so just increment
					contactAddressBlocks = contactAddressBlocks + 1;
				}
				
				var caFieldset = $('fieldset#contactAddressTemplate').clone();
				caFieldset.removeAttr('style').attr("style", "border:none;"); 
				caFieldset.attr( 'id', "contactAddress" + contactAddressBlocks );

				// iterate over the <input> fields in the fieldset and change the ids to reflect the
				// item count
				var $inputKids = caFieldset.find('input');	
				$.each( $inputKids, function(index) 
				{
				
					if( $(this).attr('name').indexOf( ".address" ) != -1 )
					{
						$(this).attr( 'name', "contactAddress[" + contactAddressBlocks + "].address" );
						$(this).attr( 'id', "contactAddress[" + contactAddressBlocks + "].address" );	
					}
					else if( $(this).attr('name').indexOf( ".contactAddressId" ) != -1 )
					{
						$(this).attr( 'name', "contactAddress[" + contactAddressBlocks + "].contactAddressId" );
						$(this).attr( 'id', "contactAddress[" + contactAddressBlocks + "].contactAddressId" );
					}
				});

				
				// do the same thing for the <select> fields
				var $selectKids = caFieldset.find('select');
				$.each( $selectKids, function(index) 
				{						
					if( $(this).attr('name').indexOf( ".serviceType" ) != -1 )
					{
						$(this).attr( 'name', "contactAddress[" + contactAddressBlocks + "].serviceType" );
						$(this).attr( 'id', "contactAddress[" + contactAddressBlocks + "].serviceType" );
					}	
				});
				
				caFieldset.appendTo( "div#contactAddresses" );
				
				return false;
			};
		
			var employerBlocks = 0;
			function addEmploymentBlock() 
			{
				if( employerBlocks == 0 )
				{
					// get the beginning count
					var tempVal = $("input#employerCount").attr('value');
					var intVal = parseInt(tempVal);
					employerBlocks = intVal;
				}
				else
				{
					// we've been called a second or subsequent time, so just increment
					employerBlocks = employerBlocks + 1;
				}
				
				
    			var fieldset = $('fieldset#employmentHistoryTemplate').clone();
    			fieldset.removeAttr('style').attr("style", "border:none;"); 
				fieldset.attr( 'id', "employer" + employerBlocks );
				
				// iterate over the <input> fields in the fieldset and change the ids to reflect the
				// item count
				var $kids = fieldset.find('input');
				$.each( $kids, function(index) 
				{
						if( $(this).attr('name').indexOf( ".companyName" ) != -1 )
						{
							$(this).attr( 'name', "employment[" + employerBlocks + "].companyName" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].companyName" );	
						} 
						else if( $(this).attr('name').indexOf( ".title" ) != -1 )
						{
							$(this).attr( 'name', "employment[" + employerBlocks + "].title" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].title" );
						}
						else if( $(this).attr('name').indexOf( ".yearFrom" ) != -1 )
						{
							$(this).attr( 'name', "employment[" + employerBlocks + "].yearFrom" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].yearFrom" );
						}
						else if( $(this).attr('name').indexOf( ".yearTo" ) != -1 )
						{
							$(this).attr( 'name', "employment[" + employerBlocks + "].yearTo" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].yearTo" );
						}									
						else if( $(this).attr('name').indexOf( ".historicalEmploymentId" ) != -1 )
						{
							$(this).attr( 'name', "employment[" + employerBlocks + "].historicalEmploymentId" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].historicalEmploymentId" );
						}							
								
					});

					// do the same thing for the <select> fields
					var $kids = fieldset.find('select');
					$.each( $kids, function(index) 
					{						
						if( $(this).attr('name').indexOf( ".monthTo" ) != -1 )
						{
							$(this).attr( 'name', "employment[" + employerBlocks + "].monthTo" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].monthTo" );
						}	
						else if( $(this).attr('name').indexOf( ".monthFrom" ) != -1 )
						{
							$(this).attr( 'name', "employment[" + employerBlocks + "].monthFrom" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].monthFrom" );
						}				
					});				
					
					// and the <textarea> fields 
					var $kids = fieldset.find('textarea');
					$.each( $kids, function(index) 
					{
						if( $(this).attr('name').indexOf( ".description" ) != -1 )
						{
							$(this).attr( 'name', "employment[" + employerBlocks + "].description" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].description" );
						}
						
					});

				fieldset.appendTo( "div#employmentHistory" );
 	
				return false;
		};
    	</script>
    </head>
    	<body>
			<!-- start body content -->
			<div class="hero-unit span6">
              
				<h2>Edit Profile</h2>
				
				<g:form action="saveProfile" id="profileForm" name="profileForm" enctype="multipart/form-data" >					
                  
                  	<div class="currentPhoto">
						<div style="background:#333; border: 10px solid #FEFEFE; height: 150px; margin-bottom: 6px; width: 180px;">
						</div>
						<label for="your_photo">Edit profile photo</label>
						<input type="file" name="your_photo" id="your_photo" value="" />
					</div>

             		<g:hiddenField name="userUuid" value="${profileToEdit?.userUuid}" />
                 
								<div class="left">
									<label for="summary">Summary</label>
									<g:textField name="summary" id="summary" value="${profileToEdit?.summary}" />
								</div>
									<div class="dateRow left">
										<b>Birthday</b>
										<div class="span1">
											<label for="birthMonth">Month</label>
											<g:select name="birthMonth" from="${months}" value="${profileToEdit.birthMonth}" optionKey="id" 
                          noSelection="${['':'Choose...']}" optionValue="text" />

										</div>
										<div class="span1">
											<label for="birthDayOfMonth">Day</label>
											<g:select name="birthDayOfMonth" from="${days}" value="${profileToEdit.birthDayOfMonth}" optionKey="id" 
                          noSelection="${['':'Choose...']}" optionValue="text" />
										</div>
										<div class="span1">
											<label for="birthYear">Year</label>
                      <g:select name="birthYear" from="${years}" value="${profileToEdit.birthYear}" optionKey="id" 
                          noSelection="${['':'Choose...']}" optionValue="text" />
										</div>
								</div>
								<div class="left">
                    	<label for="sex" >Sex:</label>
                    	<g:select name="sex" from="${sexOptions}" value="${profileToEdit?.sex}" optionKey="id" 
       						noSelection="${['':'Select One...']}" optionValue="text" />
                </div> 
								<div class="left">
                    <!--  other fields -->
	                    <label for="location">Location:</label>
    	                <g:textField name="location" value="${profileToEdit?.location}" />
                </div>    
								<div class="left">
                    	<label for="hometown">Hometown:</label>
                    	<g:textField name="hometown" value="${profileToEdit?.hometown}" />
                </div>   
                    
								<div class="left contactContainer">
                    	<label>Contact Addresses:</label>
                    	<div id="contactAddresses">
                    		<input type="hidden" id="contactAddressCount" name="contactAddressCount" value="${profileToEdit?.contactAddresses?.size()}" />
											</div>
	                 		<g:if test="${profileToEdit.contactAddressCount > 0}">
			                    <g:each status="caStatus" in="${profileToEdit?.contactAddresses}" var="contactAddress">
													<fieldset id="contactAddress${caStatus}">
	                    				<div class="contactAddressPosition" >				
	                    						<g:select name="contactAddress[${caStatus}].serviceType" from="${contactTypes}" value="${contactAddress.serviceType}" optionKey="id" 
	       										noSelection="${['':'Select One...']}" optionValue="text" />
	                    					
	                    						<g:textField name="contactAddress[${caStatus}].address" value="${contactAddress.address}" />
	                    				</div>
	                   					<input type="hidden" id="contactAddress[${caStatus}].contactAddressId" name="contactAddress[${caStatus}].contactAddressId" value="${contactAddress.id}" />
	                   				</fieldset>
	                   			</g:each>
	                   		</g:if>
	                   		<g:else>
	                			<fieldset id="contactAddress0">
	                    				<div class="contactAddressPosition" >
			                    				<g:select name="contactAddress[0].serviceType" from="${contactTypes}" value="" optionKey="id" 
	       										noSelection="${['':'Select One...']}" optionValue="text" />
	                    						<g:textField name="contactAddress[0].address" value="" />
	                    				</div>
															<input type="hidden" id="contactAddress[0].contactAddressId" name="contactAddress[0].contactAddressId" value="-1" />
	                   			</fieldset>
	                   		</g:else>
											<a href="#" onclick="return addContactAddressBlock();" class="btn btn-primary contactBtn">add another</a>
											</div>
										<div class="clear"></div>
                    <div class="left">
                    	<label for="interests">Interests:</label>		
 		                   	<g:textArea name="interests" value="${profileToEdit?.interests}" rows="5" cols="40"/>
      				</div>
      
      				<div class="left">
                    	<label for="skills">Skills:</label>
                    		
                    		<g:textArea name="skills" value="${profileToEdit?.skills}" rows="5" cols="40"/>
      					
      				</div>
      
      				<div class="left">
                    	<label for="groupsOrgs">Groups & Organizations:</label>
                    		<g:textArea name="groupsOrgs" value="${profileToEdit?.groupsOrgs}" rows="5" cols="40"/>
	                </div>

					<!-- Start employment -->
	                <div class="employmentContainer">
	                	<label for="employmentHistory" id="employmentHistoryLabel">Employment History</label>            
											<div id="employmentHistory">
												<input type="hidden" id="employerCount" name="employerCount" value="${profileToEdit?.employmentHistory?.size()}" />
	                    	<g:if test="${profileToEdit.employerCount > 0}">
		                    <g:each status="iStatus" in="${profileToEdit?.employmentHistory}" var="employment">
												<fieldset id="employer${iStatus}">
											</div>
			                    		<div class="companyName">
			                    			<label for="employment[${iStatus}].companyName">Company Name:</label>
											<g:textField name="employment[${iStatus}].companyName" value="${employment.companyName}"/>
			                    		</div>
			                    		<div class="companyTitle">
			                    			<label for="employment[${iStatus}].title">Title:</label>
											<g:textField name="employment[${iStatus}].title" value="${employment.title}"/>
			                    		</div>
															<div class="clear"></div>
			                    		<div class="companyTime">
			                    			<label for="employment[${iStatus}].monthFrom">Time Period:</label>
											<g:select name="employment[${iStatus}].monthFrom" from="${months}" value="${employment.monthFrom}" optionKey="id" 
			       							noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[${iStatus}].yearFrom" value="${employment.yearFrom}" class="yearInput" />
			       							<span>to</span> <g:select name="employment[${iStatus}].monthTo" from="${months}" value="${employment.monthFrom}" optionKey="id" 
			       							noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[${iStatus}].yearTo" value="${employment.yearTo}" class="yearInput" />
										</div>
			       						<div>
			       							<label for="employment[${iStatus}].description">Description:</label>
											<g:textArea name="employment[${iStatus}].description" value="${employment.description}" rows="5" cols="40"/>
			       						</div>
				                    		<input type="hidden" id="employment[${iStatus}].historicalEmploymentId" name="employment[${iStatus}].historicalEmploymentId" value="${employment.id}" />
									</fieldset>
											</div>

								</g:each>      
							</g:if>
							<g:else>
								<fieldset id="employer0">
									<div>
										<label for="employment[0].companyName">Company Name:</label>
										<g:textField name="employment[0].companyName" />
									</div>
									<div>
										<label for="employment[0].title">Title:</label>
										<g:textField name="employment[0].title" />
									</div>
									<div>
										<label for="employment[0].monthFrom">Time Period:</label>
										<g:select name="employment[0].monthFrom" from="${months}" value="tbd" optionKey="id"
							   				noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[0].yearFrom" class="yearInput" />
							   				<span>to</span> <g:select name="employment[0].monthTo" from="${months}" value="tbd" optionKey="id"
							   					noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[0].yearTo" class="yearInput" />
									</div>
										<div class="clear"></div>
						   			<div>
							   			<label for="employment[0].description">Description:</label>
							   			<g:textArea name="employment[0].description" value="" rows="5" cols="40"/>
						   			</div>
									<div>
					   					<input type="hidden" id="employment[0].historicalEmploymentId" name="" value="-1" />
					   				</div>
					   				
								</fieldset>
							</g:else>
						</div>
						<a href="#" onclick="return addEmploymentBlock();" class="btn btn-primary employmentBtn">add another</a>
                    </div>


					<div>
						<label for="educationHistory" id="educationHistoryLabel">Education History:</label>
            			<div id="educationHistory">

            				<input type="hidden" id="educationHistoryCount" name="educationHistoryCount" value="${profileToEdit?.educationHistoryCount}" />

	                    	<g:if test="${profileToEdit.educationHistoryCount > 0}">
							
		                    	<g:each status="edStatus" in="${profileToEdit?.educationHistory}" var="education">

									<fieldset id="education${edStatus}">
										<div>
											<label for="education[${edStatus}].institutionName">Institution Name:</label>
											<g:textField name="education[${edStatus}].institutionName" value="${education.institutionName}"/>
										</div>
										<div>
                      <label for="education[${edStatus}].major">Major / Course of Study:</label>
                      <g:textField name="education[${edStatus}].major" id="education[${edStatus}].major" value="${education.courseOfStudy}" />
                    </div>
										
										<div>
											<label for="education[${edStatus}].monthFrom">Time Period:</label>
											<g:select name="education[${edStatus}].monthFrom" from="${months}" value="${education.monthFrom}" optionKey="id"
											   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[${edStatus}].yearFrom" value="${education.yearFrom}" class="yearInput"/>
		   										to <g:select name="education[${edStatus}].monthTo" from="${months}" value="${education.monthTo}" optionKey="id"
		   										noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[${edStatus}].yearTo" value="${education.yearTo}" class="yearInput" />
										</div>

										<div>
					                    	<input type="hidden" id="education[${edStatus}].educationalExperienceId" name="education[${edStatus}].educationalExperienceId" value="${education.id}" />
										</div>
									</fieldset>
								</g:each>
							</g:if>
							<g:else>
								<fieldset id="education0">
									 <div>
				                    		<label for="education[0].institutionName">Institution Name:</label>
											<g:textField name="education[0].institutionName" value="" />
				                    </div>
										<div>

                      <label for="education[0].major">Major / Course of Study:</label>
                      <g:textField name="education[0].major" id="education[0].major" />

                  </div>
				                    <div>
				                    		
											<label for="education[0].monthFrom">Time Period:</label>
											<g:select name="education[0].monthFrom" from="${months}" value="tbd" optionKey="id"
											   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[0].yearFrom" class="yearInput" />
											   <span>to</span> <g:select name="education[0].monthTo" from="${months}" value="tbd" optionKey="id"
											   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[0].yearTo" class="yearInput" />
									</div>
				                   	<div>
				                    	<input type="hidden" id="education[0].educationalExperienceId" name="education[0].educationalExperienceId" value="-1" />
				                    </div>
								</fieldset>							
							</g:else>
						</div>
						<a href="#" onclick="return addEducationHistoryBlock();" class="btn btn-primary" id="educationBtn">add another</a>					
					</div>
					
					
               		<div class="saveFooter well">     
                    	<g:submitButton name="saveProfile" class="btn btn-large btn-primary" value="Save Profile"/>
                 	</div>
                 	
             </g:form>
          </div>
    
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
		</div>	
	</body>
</html>