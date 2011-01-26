<html>
    <head>
        <title>Quoddy: Edit Profile</title>
        <meta name="layout" content="main"/>
        <nav:resources />
    	<script type="text/javascript">
			
			var employerBlocks = 0;
			function addEmploymentBlock() {
				if( employerBlocks == 0 )
				{
					// get the beginning count
					// TODO: ...employerCount
					var tempVal = $("input#employerCount").attr('value');
					var intVal = parseInt(tempVal);
					employerBlocks = intVal;
				}
				else
				{
					// we've been called a second or subsequent time, so just increment
					employerBlocks = employerBlocks++;
				}

				alert( 'addEmploymentBlock' );
				
				employerBlocks = employerBlocks + 1;
    			alert( "ok: " + employerBlocks );
    			var fieldset = $('fieldset#employmentHistoryTemplate').clone();
    			fieldset.removeAttr('style').attr("style", "border:none;"); 
				alert( fieldset.attr('id'));
				fieldset.attr( 'id', "employer" + employerBlocks );
				
				// TODO: iterate over the <input> fields in the fieldset and change the ids to reflect the
				// item count
				var $kids = fieldset.find('input');
				$.each( $kids, function(index) {
						/* alert(index); */
						// alert($(this).attr('name'));
						if( $(this).attr('name').indexOf( ".companyName" ) != -1 )
						{
							// alert( "new title value " + books );
							$(this).attr( 'name', "employment[" + employerBlocks + "].companyName" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].companyName" );	
						} 
						else if( $(this).attr('name').indexOf( ".title" ) != -1 )
						{
							// alert( "new author value " + books );
							$(this).attr( 'name', "employment[" + employerBlocks + "].title" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].title" );
						}
						else if( $(this).attr('name').indexOf( ".yearFrom" ) != -1 )
						{
							// alert( "new author value " + books );
							$(this).attr( 'name', "employment[" + employerBlocks + "].yearFrom" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].yearFrom" );
						}
						else if( $(this).attr('name').indexOf( ".yearTo" ) != -1 )
						{
							// alert( "new author value " + books );
							$(this).attr( 'name', "employment[" + employerBlocks + "].yearTo" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].yearTo" );
						}									
						else if( $(this).attr('name').indexOf( ".historicalEmploymentId" ) != -1 )
						{
							// alert( "new author value " + books );
							$(this).attr( 'name', "employment[" + employerBlocks + "].historicalEmploymentId" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].historicalEmploymentId" );
						}							
								
					});

					// do the same thing for the <select> fields
					var $kids = fieldset.find('select');
					$.each( $kids, function(index) {
						/* alert(index); */
						// alert($(this).attr('name'));
						
						if( $(this).attr('name').indexOf( ".monthTo" ) != -1 )
						{
							// alert( "new author value " + books );
							$(this).attr( 'name', "employment[" + employerBlocks + "].monthTo" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].monthTo" );
						}	
						else if( $(this).attr('name').indexOf( ".monthFrom" ) != -1 )
						{
							// alert( "new author value " + books );
							$(this).attr( 'name', "employment[" + employerBlocks + "].monthFrom" );
							$(this).attr( 'id', "employment[" + employerBlocks + "].monthFrom" );
						}				
					});				
					
					// and the <textarea> fields 
					var $kids = fieldset.find('textarea');
					$.each( $kids, function(index) {
						/* alert(index); */
						// alert($(this).attr('name'));
						if( $(this).attr('name').indexOf( ".description" ) != -1 )
						{
							// alert( "new author value " + books );
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
 	                   	<g:textField name="birthDayOfMonth" value="${profileToEdit?.birthDayOfMonth}" />
    	               	<g:textField name="birthMonth" value="${profileToEdit?.birthMonth}" />
        	            <g:textField name="birthYear" value="${profileToEdit?.birthYear}" />
                    </dd>
                    <dt>Sex</dt>
                    <dd><g:select name="sex" from="${sexOptions}" value="${profileToEdit?.sex}" optionKey="id" 
       					noSelection="${['null':'Select One...']}" optionValue="text" />
                    </dd>
                    
                    <!--  other fields -->
                    <dt>Location:</dt>
                    <dd><g:textField name="location" value="${profileToEdit?.location}" /></dd>
                    <dt>Hometown:</dt>
                    <dd><g:textField name="hometown" value="${profileToEdit?.hometown}" /></dd>
                    <dt>Contact Addresses:</dt>
                    <dd><g:textArea name="contactAddresses" value="${profileToEdit?.contactAddresses}" rows="5" cols="40"/></dd>
                    <dt>Favorites:</dt>
                    <dd><g:textArea name="favorites" value="${profileToEdit?.favorites}" rows="5" cols="40"/></dd>
                    <dt>Languages:</dt>
                    <dd><g:textArea name="languages" value="${profileToEdit?.languages}" rows="5" cols="40"/></dd>
                    <dt>Interests:</dt>
                    <dd><g:textArea name="interests" value="${profileToEdit?.interests}" rows="5" cols="40"/></dd>
                    <dt>Skills:</dt>
                    <dd><g:textArea name="skills" value="${profileToEdit?.skills}" rows="5" cols="40"/></dd>
                    <dt>Groups & Organizations:</dt>
                    <dd><g:textArea name="groupsOrgs" value="${profileToEdit?.groupsOrgs}" rows="5" cols="40"/></dd>
	                
	                
	                <dt>Employment History:</dt>
	                <dd>            
            			<div id="employmentHistory">
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
			       							noSelection="${['null':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[${iStatus}].yearFrom" value="${employment.yearFrom}"/>
			       							to <g:select name="employment[${iStatus}].monthTo" from="${months}" value="${employment.monthFrom}" optionKey="id" 
			       							noSelection="${['null':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[${iStatus}].yearTo" value="${employment.yearTo}"/>
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
						<fieldset id="employer1">
					
						<table>
							<tr>
								<td>Company Name:</td><td><g:textField name="employment[1].companyName" /></td>
							</tr>
							<tr>
								<td>Title:</td><td><g:textField name="employment[1].title" /></td>
							</tr>
							<tr>
								<td>Time Period:</td>
								<td><g:select name="employment[1].monthFrom" from="${months}" value="tbd" optionKey="id"
								   noSelection="${['null':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[1].yearFrom" />
								   to <g:select name="employment[1].monthTo" from="${months}" value="tbd" optionKey="id"
								   noSelection="${['null':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[1].yearTo" />
								</td>
							</tr>
							   <tr>
								   <td>Description:</td><td><g:textArea name="employment[1].description" value="" rows="5" cols="40"/></td>
							   </tr>
						</table>
					   		<input type="hidden" id="employment[1].historicalEmploymentId" name="" value="-1" />
						</fieldset>
					</g:else>
					</div>
					<a href="#" onclick="return addEmploymentBlock();" style="font-size:24pt;">+</a>
                    </dd>
					<dt>Educational History</dt>
                    <dd><g:textArea name="educationHistory" value="${profileToEdit?.educationHistory}" rows="5" cols="40"/></dd>

                    <dt>Projects:</dt>
                    <dd><g:textArea name="projects" value="${profileToEdit?.projects}" rows="5" cols="40"/></dd>
                    
                    
                    <dt>&nbsp;</dt>
                    <dd><g:submitButton name="saveProfile" value="Save"/></dd>
                 </dl>

             </g:form>
          </div>
    
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
					   noSelection="${['null':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[?].yearFrom" />
					   to <g:select name="employment[?].monthTo" from="${months}" value="" optionKey="id"
					   noSelection="${['null':'Choose...']}" optionValue="text" /> &nbsp; <g:textField name="employment[?].yearTo" />
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