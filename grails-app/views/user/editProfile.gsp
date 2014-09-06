<%@ page import="org.apache.shiro.SecurityUtils" %>

<html>
    <head>
        <title>Quoddy: Edit Profile</title>
        <meta name="layout" content="user_profile" />
        <nav:resources />
    	<script type="text/javascript">

    	$j(document).ready(function(){
    		   // do jQuery

			$j('#profilePicImg').mouseover( function() {
				$j( '#your_photo' ).attr("style", "display:block;width:90px;color:transparent;" );
	    		$j( '#cancel_photo' ).attr( "style", "display:inline;");
				} );

		    $j( document ).on('change','#your_photo' , function(){
		    		$j( '#your_photo' ).attr("style", "display:block;color:black;");
		    		$j( '#save_photo' ).attr( "style", "display:inline;");
			     });

			$j( '#cancel_photo').click( function(){
				$j('#your_photo').parent('form').trigger('reset')
				
				// and hide the controls...
				$j( '#your_photo').attr( 'style', 'display:none');
	    		$j( '#cancel_photo' ).attr("style", "display:none;");
	    		$j( '#save_photo' ).attr( "style", "display:none;");
				
			});

			$j( '#save_photo').click( function() {

			    var formData = new FormData($j('#profileFormAvatarPic')[0]);

			    $j.ajax({
			        url: "${ createLink(controller:'user', action:'saveProfileAvatarPic')}",
			        type: 'POST',
			        data: formData,
			        async: false,
			        cache: false,
			        contentType: false,
			        processData: false,
			        success: function (returndata) {

			            // alert('done saving' );
						var srcUrl = "${createLink(controller:'profilePic',action:'full',id:profileToEdit.userId)}";
						var date = new Date();
						srcUrl = srcUrl + "?date=" + date.getTime();
			        	$j('#profilePicImg').attr('src', srcUrl );

						$j('#your_photo').parent('form').trigger('reset')
						
						// and hide the controls...
						$j( '#your_photo').attr( 'style', 'display:none');
			    		$j( '#cancel_photo' ).attr("style", "display:none;");
			    		$j( '#save_photo' ).attr( "style", "display:none;");
				         
			        },
			        error: function(){
			            alert("error in ajax form submission");
			            }
			    });
				
				return false;		
			});

    	});

        	
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
					
					<div style="display:inline-block;margin-left:20px;margin-top:15px;">
						<label for="summary">Description:</label>
						<span id="summary">${profileToEdit?.summary} ... </span>
						<!-- <g : textField name="summary" id="summary" value="" / > -->
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