<script>



/***********************************************************************************************************
 **** Old javascript for managing the dynamic insertion of entries for things like education, employment, skills, etc. ****
 **************************************************************************************************************************
 */


    	
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