<html>
    <head>
        <title>Quoddy: Edit Profile</title>
        <meta name="layout" content="main"/>
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
           <div style="margin-left:35px;padding-top:30px;">

              <!-- start body content -->
              <h1>Edit Profile</h1>

             <g:hasErrors>
                 <div class="errors">
                    <g:renderErrors bean="${flash.user}" as="list" />
                 </div>
             </g:hasErrors>

             <g:form action="saveProfile">
             
             	<g:hiddenField name="userUuid" value="${profileToEdit?.userUuid}" />
                 <dl>
                 	
                 	<dt>Summary:</dt>
                 	<dd><g:textField name="summary" value="${profileToEdit?.summary}" /></dd>
                    <dt>Birthday (mm/dd/yyyy)</dt>
                    <dd>
                    <g:select name="birthDayOfMonth" from="${days}" value="${profileToEdit.birthDayOfMonth}" optionKey="id" 
			       							noSelection="${['':'Choose...']}" optionValue="text" />
                        	            
    	            <g:select name="birthMonth" from="${months}" value="${profileToEdit.birthMonth}" optionKey="id" 
			       							noSelection="${['':'Choose...']}" optionValue="text" />   	
        	            
        	            <g:textField name="birthYear" value="${profileToEdit?.birthYear}" />
                    </dd>
                    <dt>Sex</dt>
                    <dd><g:select name="sex" from="${sexOptions}" value="${profileToEdit?.sex}" optionKey="id" 
       					noSelection="${['':'Select One...']}" optionValue="text" />
                    </dd>
                    
                    <!--  other fields -->
                    <dt>Location:</dt>
                    <dd><g:textField name="location" value="${profileToEdit?.location}" /></dd>
                    <dt>Hometown:</dt>
                    <dd><g:textField name="hometown" value="${profileToEdit?.hometown}" /></dd>
                   
                   
                    <dt>Contact Addresses:</dt>
                    <dd>
                    <div id="contactAddresses">
                    	
                    	<input type="hidden" id="contactAddressCount" name="contactAddressCount" value="${profileToEdit?.contactAddresses?.size()}" />
                    	
	                 	<g:if test="${profileToEdit.contactAddressCount > 0}">
			                    <g:each status="caStatus" in="${profileToEdit?.contactAddresses}" var="contactAddress">
									<fieldset id="contactAddress${caStatus}">
	                    				<table>
	                    				<tr>				
	                    					<td>
	                    						<g:select name="contactAddress[${caStatus}].serviceType" from="${contactTypes}" value="${contactAddress.serviceType}" optionKey="id" 
	       										noSelection="${['':'Select One...']}" optionValue="text" />
	                    					</td>
	                    					<td>
	                    						<g:textField name="contactAddress[${caStatus}].address" value="${contactAddress.address}" />
	                    					</td>
	                    				</tr>
	                    				</table>
	                   				<input type="hidden" id="contactAddress[${caStatus}].contactAddressId" name="contactAddress[${caStatus}].contactAddressId" value="${contactAddress.id}" />
	                   				</fieldset>
	
	                   			</g:each>
	                   	
	                   	</g:if>
	                   	<g:else>
	                		<fieldset id="contactAddress0">
	                    		<table>
	                    			<tr>
			                    		<td>
			                    			<g:select name="contactAddress[0].serviceType" from="${contactTypes}" value="" optionKey="id" 
	       									noSelection="${['':'Select One...']}" optionValue="text" />
	       								</td>
	       								<td>
	                    					<g:textField name="contactAddress[0].address" value="" />
	                    				</td>
	                    			</tr>
	                    		</table>	
								<input type="hidden" id="contactAddress[0].contactAddressId" name="contactAddress[0].contactAddressId" value="-1" />
	                   		</fieldset>
	                   	</g:else>
                   </div>
    				<a href="#" onclick="return addContactAddressBlock();" style="font-size:24pt;">+</a>
                   	</dd>
                    
                   
                    <!--
                    <dt>Favorites:</dt>
                    <dd><g:textArea name="favorites" value="${profileToEdit?.favorites}" rows="5" cols="40"/></dd>
                    -->
                    
                    <!--
                    <dt>Languages:</dt>
                    <dd><g:textArea name="languages" value="${profileToEdit?.languages}" rows="5" cols="40"/></dd>
                    -->
                    
                    
                    <dt>Interests:</dt>
                    <dd><g:textArea name="interests" value="${profileToEdit?.interests}" rows="5" cols="40"/></dd>
                    <dt>Skills:</dt>
                    <dd><g:textArea name="skills" value="${profileToEdit?.skills}" rows="5" cols="40"/></dd>
                    <dt>Groups & Organizations:</dt>
                    <dd><g:textArea name="groupsOrgs" value="${profileToEdit?.groupsOrgs}" rows="5" cols="40"/></dd>
	                
	                
	                <dt>Employment History:</dt>
	                <dd>            
            			<div id="employmentHistory">

							<!-- we can ixnay this, no?  -->
            				<input type="hidden" id="employerCount" name="employerCount" value="${profileToEdit?.employmentHistory?.size()}" />

	                    	<g:if test="${profileToEdit.employerCount > 0}">
							
		                    <g:each status="iStatus" in="${profileToEdit?.employmentHistory}" var="employment">

								<fieldset id="employer${iStatus}">
								  	
								  	<table>
			                    		<tr>
			                    			<td>Company Name:</td>
											<td><g:textField name="employment[${iStatus}].companyName" value="${employment.companyName}"/></td>
			                    		</tr>
			                    		<tr>
			                    			<td>Title:</td>
											<td><g:textField name="employment[${iStatus}].title" value="${employment.title}"/></td>
			                    		</tr>
			                    		<tr>
			                    			<td>Time Period:</td>
											<td><g:select name="employment[${iStatus}].monthFrom" from="${months}" value="${employment.monthFrom}" optionKey="id" 
			       							noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[${iStatus}].yearFrom" value="${employment.yearFrom}"/>
			       							to <g:select name="employment[${iStatus}].monthTo" from="${months}" value="${employment.monthFrom}" optionKey="id" 
			       							noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[${iStatus}].yearTo" value="${employment.yearTo}"/>
			       							</td>
										</tr>
			       						<tr>
			       							<td>Description:</td>
											<td><g:textArea name="employment[${iStatus}].description" value="${employment.description}" rows="5" cols="40"/></td>
			       						</tr>
			                    		
			                    	</table>
			                    	<input type="hidden" id="employment[${iStatus}].historicalEmploymentId" name="employment[${iStatus}].historicalEmploymentId" value="${employment.id}" />
								</fieldset>

							</g:each>      
						</g:if>
						<g:else>
						<fieldset id="employer0">
					
						<table>
							<tr>
								<td>Company Name:</td><td><g:textField name="employment[0].companyName" /></td>
							</tr>
							<tr>
								<td>Title:</td><td><g:textField name="employment[0].title" /></td>
							</tr>
							<tr>
								<td>Time Period:</td>
								<td><g:select name="employment[0].monthFrom" from="${months}" value="tbd" optionKey="id"
								   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[0].yearFrom" />
								   to <g:select name="employment[0].monthTo" from="${months}" value="tbd" optionKey="id"
								   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[0].yearTo" />
								</td>
							</tr>
							   <tr>
								   <td>Description:</td><td><g:textArea name="employment[0].description" value="" rows="5" cols="40"/></td>
							   </tr>
						</table>
					   		<input type="hidden" id="employment[0].historicalEmploymentId" name="" value="-1" />
						</fieldset>
					</g:else>
					</div>
					<a href="#" onclick="return addEmploymentBlock();" style="font-size:24pt;">+</a>
                    </dd>


					<dt>Educational History</dt>
					<!--
						<dd><g:textArea name="educationHistory" value="${profileToEdit?.educationHistory}" rows="5" cols="40"/></dd>
					-->
					<dd>
            			<div id="educationHistory">

            				<input type="hidden" id="educationHistoryCount" name="educationHistoryCount" value="${profileToEdit?.educationHistoryCount}" />

	                    	<g:if test="${profileToEdit.educationHistoryCount > 0}">
							
		                    	<g:each status="edStatus" in="${profileToEdit?.educationHistory}" var="education">

									<fieldset id="education${edStatus}">
									  	<table>
				                    		<tr>
				                    			<td>Institution Name:</td>
												<td><g:textField name="education[${edStatus}].institutionName" value="${education.institutionName}"/></td>
				                    		</tr>
				                    		
				                    		<tr>
												<td>Time Period:</td>
												<td><g:select name="education[${edStatus}].monthFrom" from="${months}" value="${education.monthFrom}" optionKey="id"
												   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[${edStatus}].yearFrom" value="${education.yearFrom}"/>
												   to <g:select name="education[${edStatus}].monthTo" from="${months}" value="${education.monthTo}" optionKey="id"
												   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[${edStatus}].yearTo" value="${education.yearTo}"/>
												</td>
											</tr>
											<tr>
												<td>Major / Course of Study: </td>
												<td><g:textField name="education[${edStatus}].major" id="education[${edStatus}].major" value="${education.courseOfStudy}" /></td>
											</tr>
				                    	</table>
				                    	<input type="hidden" id="education[${edStatus}].educationalExperienceId" name="education[${edStatus}].educationalExperienceId" value="${education.id}" />
									</fieldset>
							
								</g:each>
							
							</g:if>
							<g:else>

								<fieldset id="education0">
									  	<table>
				                    		<tr>
				                    			<td>Institution Name:</td>
												<td><g:textField name="education[0].institutionName" value=""/></td>
				                    		</tr>
				                    		<tr>
												<td>Time Period:</td>
												<td><g:select name="education[0].monthFrom" from="${months}" value="tbd" optionKey="id"
												   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[0].yearFrom" />
												   to <g:select name="education[0].monthTo" from="${months}" value="tbd" optionKey="id"
												   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[0].yearTo" />
												</td>
											</tr>
											<tr>
												<td>Major / Course of Study: </td>
												<td><g:textField name="education[0].major" id="education[0].major" /></td>
											</tr>
				                    	</table>
				                    	<input type="hidden" id="education[0].educationalExperienceId" name="education[0].educationalExperienceId" value="-1" />
								</fieldset>
							
							</g:else>
						</div>
						<a href="#" onclick="return addEducationHistoryBlock();" style="font-size:24pt;">+</a>					
					</dd>

					<!-- 
                    <dt>Projects:</dt>
                    <dd><g:textArea name="projects" value="${profileToEdit?.projects}" rows="5" cols="40"/></dd>
                    -->
                    
                    <dt>&nbsp;</dt>
                    <dd><g:submitButton name="saveProfile" value="Save"/></dd>
                 </dl>

             </g:form>
          </div>
    
    
    
    <!-- hidden template fieldset for educational history -->
	<fieldset id="educationHistoryTemplate" style="display:none;">
	  	<table>
        	<tr>
            	<td>Institution Name:</td>
				<td><g:textField name="education[?].institutionName" value=""/></td>
          	</tr>

           	<tr>
				<td>Time Period:</td>
				<td><g:select name="education[?].monthFrom" from="${months}" value="tbd" optionKey="id"
				   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[?].yearFrom" />
				   to <g:select name="education[?].monthTo" from="${months}" value="tbd" optionKey="id"
				   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="education[?].yearTo" />
				</td>
			</tr>
			<tr>
				<td>Major / Course of Study: </td>
				<td><g:textField name="education[?].major" id="education[?].major" /></td>
			</tr>

       	</table>
        <input type="hidden" id="education[?].educationalExperienceId" name="education[?].educationalExperienceId" value="-1" />
	</fieldset>
    
    <!-- hidden template fieldset for contact address -->
    <fieldset id="contactAddressTemplate" style="display:none;">
     	<table>
      		<tr>
        		<td>
        			<g:select name="contactAddress[?].serviceType" from="${contactTypes}" value="" optionKey="id" 
						noSelection="${['':'Select One...']}" optionValue="text" />
				</td>
				<td>
      				<g:textField name="contactAddress[?].address" value="" />
      			</td>
      		</tr>
      	</table>
      	<input type="hidden" id="contactAddress[?].contactAddressId" name="contactAddress[?].contactAddressId" value="-1" />     	
    </fieldset>
    
	<!-- hidden template fieldset for employment history -->
	<fieldset id="employmentHistoryTemplate" style="display:none;" >
		<dd>
			<table>
				<tr>
					<td>Company Name:</td>
					<td><g:textField name="employment[?].companyName" /></td>
				</tr>
				<tr>
					<td>Title:</td>
					<td><g:textField name="employment[?].title" /></td>
				</tr>
				<tr>
					<td>Time Period:</td>
					<td><g:select name="employment[?].monthFrom" from="${months}" value="" optionKey="id"
					   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[?].yearFrom" />
					   to <g:select name="employment[?].monthTo" from="${months}" value="" optionKey="id"
					   noSelection="${['':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[?].yearTo" />
					</td>
				</tr>
				<tr>
					   <td>Description:</td>
					   <td><g:textArea name="employment[?].description" value="" rows="5" cols="40"/></td>
				   </tr>
				</table>
		   </dd>
		<input type="hidden" id="employment[?].historicalEmploymentId" name="employment[?].historicalEmploymentId" value="-1" />   
		</fieldset>
	</body>
</html>