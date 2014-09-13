    	<script type="text/javascript">

    	$j(document).ready(function(){
    		 

    		/*******************************************************************************
    		*************************** FOR AVATAR PIC *************************************
    		********************************************************************************/

    		var profPicTimer;
    		var profPicDelay = 1000;

			$j('#profilePicImg').hover( function() {

					profPicTimer = setTimeout( function() {
						$j( '#your_photo' ).attr("style", "display:block;width:90px;color:transparent;" );
	    				$j( '#cancel_photo' ).attr( "style", "display:inline;");
					}, profPicDelay );
			 	}, function() {
				  clearTimeout( profPicTimer );
			});

	
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

    		var summaryTimer;
    		var summaryDelay = 1000;
			$j( '#summary').hover( function() {

				var currentSummary = $j(this).text().trim();
				var selected = $j(this);
				
				summaryTimer = setTimeout( function() {
					// capture the current summary
					
					// alert( "ok: " + currentSummary );
					// hide the <span> control
					selected.attr( "style", "display:none;");
					// and reveal an editable form
					$j("#summaryInput").val( currentSummary );
					$j("#summaryInput").removeAttr( "style");
					$j("#cancel_summary").removeAttr( "style");
					$j("#save_summary").removeAttr( "style");
				}, summaryDelay );
			}, function() {
				clearTimeout( summaryTimer );
			});

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
    		var titleTimer;
    		var titleDelay = 1000;
    		$j( '#title').hover( function() {

    			// capture the current title
				var currentTitle = $j(this).text().trim();
    			var selected = $j(this);
    			
    			titleTimer = setTimeout( function () {
        			
					// alert( "ok: " + currentSummary );
					// hide the <span> control
					selected.attr( "style", "display:none;");
					// and reveal an editable form
					$j("#titleInput").val( currentTitle );
					$j("#titleInput").removeAttr( "style");
					$j("#cancel_title").removeAttr( "style");
					$j("#save_title").removeAttr( "style");
    				}, titleDelay );
				
				}, function() {

					clearTimeout( titleTimer )
			});

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
				// alert( "submitting...");
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
    		var phoneTimer;
    		var phoneDelay = 1000;
    		$j( '#primaryPhone').hover( function() {

				// capture the current phone val
				var currentPrimaryPhone = $j(this).text().trim();

				phoneTimer = setTimeout( function() {
					// alert( "ok" );
					// hide the <span> control
					$j(this).attr( "style", "display:none;");
					// and reveal an editable form
					$j("#primaryPhoneInput").val( currentPrimaryPhone );
					$j("#primaryPhoneInput").removeAttr( "style");
					$j("#cancel_primary_phone").removeAttr( "style");
					$j("#save_primary_phone").removeAttr( "style");
				}, phoneDelay );
				
			 }, function() {
				clearTimeout( phoneTimer );
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
				// alert( "submitting...");
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
    		var emailTimer;
    		var emailDelay = 1000;    		
    		$j( '#primaryEmail').hover( function() {

				// capture the current primary email
				var currentPrimaryEmail = $j(this).text().trim();
				var selected = $j(this);
				
				emailTimer = setTimeout( function() {
					// alert( "ok: " + currentPrimaryEmail );
					// hide the <span> control
					selected.attr( "style", "display:none;");
					// and reveal an editable form
					$j("#primaryEmailInput").val( currentPrimaryEmail );
					$j("#primaryEmailInput").removeAttr( "style");
					$j("#cancel_primary_email").removeAttr( "style");
					$j("#save_primary_email").removeAttr( "style");
				}, emailDelay );
				
			}, function() {
				clearTimeout( emailTimer );
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
				// alert( "submitting...");
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
    		var imTimer;
    		var imDelay = 1000;
    		$j( '#primaryInstantMessenger').hover( function() {

				// capture the current primary email
				var currentPrimaryInstantMessenger = $j(this).text().trim();
				var selected = $j(this);

					imTimer = setTimeout( function() {
					// alert( "ok: " + currentPrimaryEmail );
					// hide the <span> control
					selected.attr( "style", "display:none;");
					// and reveal an editable form
					$j("#primaryInstantMessengerInput").val( currentPrimaryInstantMessenger );
					$j("#primaryInstantMessengerInput").removeAttr( "style");
					$j("#cancel_primary_instant_messenger").removeAttr( "style");
					$j("#save_primary_instant_messenger").removeAttr( "style");
				}, imDelay );
				
			}, function() {

				clearTimeout( imTimer );
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
				// alert( "submitting...");
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
    		var locationTimer;
    		var locationDelay = 1000;
    		$j( '#location').hover( function() {
        		
				// capture the current primary email
				var currentLocation = $j(this).text().trim();
				var selected = $j(this);

				locationTimer = setTimeout( function() {
					// alert( "ok: " + currentPrimaryEmail );
					// hide the <span> control
					selected.attr( "style", "display:none;");
					// and reveal an editable form
					$j("#locationInput").val( currentLocation);
					$j("#locationInput").removeAttr( "style");
					$j("#cancel_location").removeAttr( "style");
					$j("#save_location").removeAttr( "style");
				}, locationDelay);
				
			}, function() {
				
				clearTimeout( locationTimer );
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
				// alert( "submitting...");
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
    		var dotPlanTimer;
    		var dotPlanDelay = 1000;    		
    		$j( '#dotPlan').hover( function() {
        		
				// capture the current primary email
				var currentDotPlan = $j(this).text().trim();
				var selected = $j(this);
				
				dotPlanTimer = setTimeout( function() { 
					// alert( "ok: " + currentDotPlan );
					// hide the <span> control
					selected.attr( "style", "display:none;");
					// and reveal an editable form
					$j("#dotPlanInput").val( currentDotPlan);
					$j("#dotPlanInput").removeAttr( "style");
					$j("#cancel_dot_plan").removeAttr( "style");
					$j("#save_dot_plan").removeAttr( "style");

				}, dotPlanDelay );

			}, function() {
				clearTimeout( dotPlanTimer );
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
				// alert( "submitting...");
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
    		
    		
			/****
			  for Education History
			*/
			$j('#addEducationBtn').click( function() {
				
				// cause our form to submit
				$j('#addEducationHistoryForm').submit();
				return false;
			});
			
			$j( "#addEducationHistoryForm").on( "submit", function(e) {
				// alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveEducationHistoryEntry',id:profileToEdit.userId)}";

				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			// alert( "success");

							// hide the dialog
							$j('#addEducationHistoryModal').modal('hide');
	
			       		  },

			       		  'text'
			    	    );			
			
			});


			/****
			  for Education History
			*/
			$j('#addEmploymentBtn').click( function() {
				
				// cause our form to submit
				$j('#addEmploymentHistoryForm').submit();
				return false;
			});
			
			$j( "#addEmploymentHistoryForm").on( "submit", function(e) {
				// alert( "submitting...");
				e.preventDefault();

				var url = "${createLink(controller:'user',action:'saveEmploymentHistoryEntry',id:profileToEdit.userId)}";

				$j.post(  url, 

						  $j(this).serialize(), 

						  function(data) {
			      			alert( "success");

							// hide the dialog
							$j('#addEmploymentHistoryModal').modal('hide');
	
			       		  },

			       		  'text'
			    	    );			
			
			});
			
			
			
			/* next whatever goes here... */
	
			
		// end of jquery init block	
    	});

</script>