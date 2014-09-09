    	<script type="text/javascript">

    	$j(document).ready(function(){
    		 

    		/*******************************************************************************
    		*************************** FOR AVATAR PIC *************************************
    		********************************************************************************/
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


			/*******************************************************************************
    		*************************** FOR SUMMARY *************************************
    		********************************************************************************/
			$j( '#summary').on( 'mouseover', function() {

				// capture the current summary
				var currentSummary = $j(this).text().trim();
				// alert( "ok: " + currentSummary );
				// hide the <span> control
				$j(this).attr( "style", "display:none;");
				// and reveal an editable form
				$j("#summaryInput").val( currentSummary );
				$j("#summaryInput").removeAttr( "style");
				$j("#cancel_summary").removeAttr( "style");
				$j("#save_summary").removeAttr( "style");
				
				} );

			$j("#cancel_summary").click( function() { 
				$j("#summaryInput").val("");
				$j("#summaryInput").attr( 'style', 'display:none;');
				$j(this).attr('style', 'display:none;');
				$j("#save_summary").attr( 'style', 'display:none;' );
				$j('#summary').attr( 'style', 'display:inline;');

				return false;
			});

			$j("#save_summary").click( function() { 
				$j('#saveProfileSummary').submit();

				return false;
 
			});


			$j( "#saveProfileSummary").on( "submit", function(e) {
				// alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveProfileSummary',id:profileToEdit.userId)}";
				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			// alert( "success");

							// put the span back and hide the edit controls...
							if( data != null && data.trim().length > 0 )
							{
								$j("#summary").text(data.trim());
							}
							else
							{
								$j("#summary").text("...");
							}
							
							$j("#summary").attr("style", "display:inline;" );
							$j("#summaryInput").val("");
							$j("#summaryInput").attr( 'style', 'display:none;');
							$j("#save_summary").attr( 'style', 'display:none;' );
							$j("#cancel_summary").attr( 'style', 'display:none;' );	
			       		  },

			       		  'text'
			    	    );
			});



			/*******************************************************************************
    		*************************** FOR TITLE *************************************
    		********************************************************************************/
    		$j( '#title').on( 'mouseover', function() {

				// capture the current summary
				var currentTitle = $j(this).text().trim();
				// alert( "ok: " + currentSummary );
				// hide the <span> control
				$j(this).attr( "style", "display:none;");
				// and reveal an editable form
				$j("#titleInput").val( currentTitle );
				$j("#titleInput").removeAttr( "style");
				$j("#cancel_title").removeAttr( "style");
				$j("#save_title").removeAttr( "style");
				
				} );

			$j("#cancel_title").click( function() { 
				$j("#titleInput").val("");
				$j("#titleInput").attr( 'style', 'display:none;');
				$j(this).attr('style', 'display:none;');
				$j("#save_title").attr( 'style', 'display:none;' );
				$j('#title').attr( 'style', 'display:inline;');

				return false;
			});

			$j("#save_title").click( function() { 
				alert( "saving...");
				$j('#saveProfileTitle').submit();

				return false;
 
			});


			$j( "#saveProfileTitle").on( "submit", function(e) {
				alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveProfileTitle',id:profileToEdit.userId)}";

				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			// alert( "success");

							// put the span back and hide the edit controls...
							if( data != null && data.trim().length > 0 )
							{
								$j("#title").text(data.trim());
							}
							else
							{
								$j("#title").text("...");
							}
							
							$j("#title").attr("style", "display:inline;" );
							$j("#titleInput").val("");
							$j("#titleInput").attr( 'style', 'display:none;');
							$j("#save_title").attr( 'style', 'display:none;' );
							$j("#cancel_title").attr( 'style', 'display:none;' );	
			       		  },

			       		  'text'
			    	    );
			});			

			/* ******************************************************************************
    		   *************************** FOR PHONE *************************************
    		   *******************************************************************************
    		*/

    		$j( '#primaryPhone').on( 'mouseover', function() {

				// capture the current summary
				var currentPrimaryPhone = $j(this).text().trim();
				// alert( "ok" );
				// hide the <span> control
				$j(this).attr( "style", "display:none;");
				// and reveal an editable form
				$j("#primaryPhoneInput").val( currentPrimaryPhone );
				$j("#primaryPhoneInput").removeAttr( "style");
				$j("#cancel_primary_phone").removeAttr( "style");
				$j("#save_primary_phone").removeAttr( "style");
				
			 });
			 
			$j("#cancel_primary_phone").click( function() { 
				$j("#primaryPhoneInput").val("");
				$j("#primaryPhoneInput").attr( 'style', 'display:none;');
				$j(this).attr('style', 'display:none;');
				$j("#save_primary_phone").attr( 'style', 'display:none;' );
				$j('#primaryPhone').attr( 'style', 'display:inline;');

				return false;
			});

			$j("#save_primary_phone").click( function() { 
				alert( "saving...");
				$j('#saveProfilePrimaryPhone').submit();

				return false;
 
			});


			$j( "#saveProfilePrimaryPhone").on( "submit", function(e) {
				alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveProfilePrimaryPhone',id:profileToEdit.userId)}";

				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			// alert( "success");

							// put the span back and hide the edit controls...
							if( data != null && data.trim().length > 0 )
							{
								$j("#primaryPhone").text(data.trim());
							}
							else
							{
								$j("#primaryPhone").text("...");
							}
							
							$j("#primaryPhone").attr("style", "display:inline;" );
							$j("#primaryPhoneInput").val("");
							$j("#primaryPhoneInput").attr( 'style', 'display:none;');
							$j("#save_primary_phone").attr( 'style', 'display:none;' );
							$j("#cancel_primary_phone").attr( 'style', 'display:none;' );	
			       		  },

			       		  'text'
			    	    );
			});				 
			 

			/* ******************************************************************************
    		   *************************** FOR EMAIL *************************************
    		   *******************************************************************************
    		*/
    		$j( '#primaryEmail').on( 'mouseover', function() {

				// capture the current primary email
				var currentPrimaryEmail = $j(this).text().trim();
				// alert( "ok: " + currentPrimaryEmail );
				// hide the <span> control
				$j(this).attr( "style", "display:none;");
				// and reveal an editable form
				$j("#primaryEmailInput").val( currentPrimaryEmail );
				$j("#primaryEmailInput").removeAttr( "style");
				$j("#cancel_primary_email").removeAttr( "style");
				$j("#save_primary_email").removeAttr( "style");

				
			});

			$j("#cancel_primary_email").click( function() { 
				$j("#primaryEmailInput").val("");
				$j("#primaryEmailInput").attr( 'style', 'display:none;');
				$j(this).attr('style', 'display:none;');
				$j("#save_primary_email").attr( 'style', 'display:none;' );
				$j('#primaryEmail').attr( 'style', 'display:inline;');

				return false;
			});

			$j("#save_primary_email").click( function() { 
				alert( "saving...");
				$j('#saveProfilePrimaryEmail').submit();

				return false;
 
			});


			$j( "#saveProfilePrimaryEmail").on( "submit", function(e) {
				alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveProfilePrimaryEmail',id:profileToEdit.userId)}";

				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			// alert( "success");

							// put the span back and hide the edit controls...
							if( data != null && data.trim().length > 0 )
							{
								$j("#primaryEmail").text(data.trim());
							}
							else
							{
								$j("#primaryEmail").text("...");
							}
							
							$j("#primaryEmail").attr("style", "display:inline;" );
							$j("#primaryEmailInput").val("");
							$j("#primaryEmailInput").attr( 'style', 'display:none;');
							$j("#save_primary_email").attr( 'style', 'display:none;' );
							$j("#cancel_primary_email").attr( 'style', 'display:none;' );	
			       		  },

			       		  'text'
			    	    );
			});				 
			 

			/* ******************************************************************************
    		   ************************ FOR INSTANT MESSENGER ******************************
    		   ********************************************************************************
    		*/
    		$j( '#primaryInstantMessenger').on( 'mouseover', function() {

				// capture the current primary email
				var currentPrimaryInstantMessenger = $j(this).text().trim();
				// alert( "ok: " + currentPrimaryEmail );
				// hide the <span> control
				$j(this).attr( "style", "display:none;");
				// and reveal an editable form
				$j("#primaryInstantMessengerInput").val( currentPrimaryInstantMessenger );
				$j("#primaryInstantMessengerInput").removeAttr( "style");
				$j("#cancel_primary_instant_messenger").removeAttr( "style");
				$j("#save_primary_instant_messenger").removeAttr( "style");

			});

			$j("#cancel_primary_instant_messenger").click( function() { 
				$j("#primaryInstantMessengerInput").val("");
				$j("#primaryInstantMessengerInput").attr( 'style', 'display:none;');
				$j(this).attr('style', 'display:none;');
				$j("#save_primary_instant_messenger").attr( 'style', 'display:none;' );
				$j('#primaryInstantMessenger').attr( 'style', 'display:inline;');

				return false;
			});

			$j("#save_primary_instant_messenger").click( function() { 
				alert( "saving...");
				$j('#saveProfilePrimaryInstantMessenger').submit();

				return false;
 
			});


			$j( "#saveProfilePrimaryInstantMessenger").on( "submit", function(e) {
				alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveProfilePrimaryInstantMessenger',id:profileToEdit.userId)}";

				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			// alert( "success");

							// put the span back and hide the edit controls...
							if( data != null && data.trim().length > 0 )
							{
								$j("#primaryInstantMessenger").text(data.trim());
							}
							else
							{
								$j("#primaryInstantMessenger").text("...");
							}
							
							$j("#primaryInstantMessenger").attr("style", "display:inline;" );
							$j("#primaryInstantMessengerInput").val("");
							$j("#primaryInstantMessengerInput").attr( 'style', 'display:none;');
							$j("#save_primary_instant_messenger").attr( 'style', 'display:none;' );
							$j("#cancel_primary_instant_messenger").attr( 'style', 'display:none;' );	
			       		  },

			       		  'text'
			    	    );
			});


			/* ******************************************************************************
    		   ************************* FOR LOCATION *************************************
    		   *****************************************************************************
    		*/
    		$j( '#location').on( 'mouseover', function() {
        		
				// capture the current primary email
				var currentLocation = $j(this).text().trim();
				// alert( "ok: " + currentPrimaryEmail );
				// hide the <span> control
				$j(this).attr( "style", "display:none;");
				// and reveal an editable form
				$j("#locationInput").val( currentLocation);
				$j("#locationInput").removeAttr( "style");
				$j("#cancel_location").removeAttr( "style");
				$j("#save_location").removeAttr( "style");

			});

			$j("#cancel_location").click( function() { 
				$j("#locationInput").val("");
				$j("#locationInput").attr( 'style', 'display:none;');
				$j(this).attr('style', 'display:none;');
				$j("#save_location").attr( 'style', 'display:none;' );
				$j('#location').attr( 'style', 'display:inline;');

				return false;
			});

			$j("#save_location").click( function() { 
				alert( "saving...");
				$j('#saveProfileLocation').submit();

				return false;
 
			});


			$j( "#saveProfileLocation").on( "submit", function(e) {
				alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveProfileLocation',id:profileToEdit.userId)}";

				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			// alert( "success");

							// put the span back and hide the edit controls...
							if( data != null && data.trim().length > 0 )
							{
								$j("#location").text(data.trim());
							}
							else
							{
								$j("#location").text("...");
							}
							
							$j("#location").attr("style", "display:inline;" );
							$j("#locationInput").val("");
							$j("#locationInput").attr( 'style', 'display:none;');
							$j("#save_location").attr( 'style', 'display:none;' );
							$j("#cancel_location").attr( 'style', 'display:none;' );	
			       		  },

			       		  'text'
			    	    );
			});



			/******************************************************************************
    		*************************** FOR .PLAN *************************************
    		********************************************************************************
    		*/
    		$j( '#dotPlan').on( 'mouseover', function() {
        		
				// capture the current primary email
				var currentDotPlan = $j(this).text().trim();
				// alert( "ok: " + currentDotPlan );
				// hide the <span> control
				$j(this).attr( "style", "display:none;");
				// and reveal an editable form
				$j("#dotPlanInput").val( currentDotPlan);
				$j("#dotPlanInput").removeAttr( "style");
				$j("#cancel_dot_plan").removeAttr( "style");
				$j("#save_dot_plan").removeAttr( "style");

			});

			$j("#cancel_dot_plan").click( function() { 
				$j("#dotPlanInput").val("");
				$j("#dotPlanInput").attr( 'style', 'display:none;');
				$j(this).attr('style', 'display:none;');
				$j("#save_dot_plan").attr( 'style', 'display:none;' );
				$j('#dotPlan').attr( 'style', 'display:inline;');

				return false;
			});

			$j("#save_dot_plan").click( function() { 
				alert( "saving...");
				$j('#saveProfileDotPlan').submit();

				return false;
 
			});


			$j( "#saveProfileDotPlan").on( "submit", function(e) {
				alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveProfileDotPlan',id:profileToEdit.userId)}";

				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			// alert( "success");

							// put the span back and hide the edit controls...
							if( data != null && data.trim().length > 0 )
							{
								$j("#dotPlan").text(data.trim());
							}
							else
							{
								$j("#dotPlan").text("...");
							}
							
							$j("#dotPlan").attr("style", "display:inline;" );
							$j("#dotPlanInput").val("");
							$j("#dotPlanInput").attr( 'style', 'display:none;');
							$j("#save_dot_plan").attr( 'style', 'display:none;' );
							$j("#cancel_dot_plan").attr( 'style', 'display:none;' );	
			       		  },

			       		  'text'
			    	    );
			});
    		
    		
			
			/* next whatever goes here... */
			
		// end of jquery init block	
    	});



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