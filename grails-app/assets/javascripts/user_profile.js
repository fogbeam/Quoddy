function addToFriends()
{
	// alert( "addToFriends called");
	var id = $j(this).attr('id').substring( 16 );
	// alert("clicked for id: " + id )
	
	// note: on the backend we assume the initiator of this operation is the
	// currently logged in user from the httpsession, so we only need to pass
	// the id of the user we want to "friend"
	
	var jqXhr = $j.ajax({
		url: appContext + "/user/addToFriends",
		type: "POST",
		data: { userId : id },
		dataType: "text"
	});

	jqXhr.done(function(data, textStatus, jqXHR) {
		// alert( "Friend request sent." );
	})
	.fail(function( jqXHR, textStatus, errorThrown) {
		alert( "error: " + errorThrown );
	})
	.always(function() {
		// alert( "complete" );
	});
	
}

function followUser()
{
	var id = $j(this).attr('id').substring( 14 );
	// alert("clicked for id: " + id )
	
	
	// note: on the backend we assume the initiator of this operation is the
	// currently logged in user from the httpsession, so we only need to pass
	// the id of the user we want to "follow"	

	var jqXhr = $j.ajax({
		url: appContext + "/user/addToFollow",
		type: "POST",
		data: { userId : id },
		dataType: "text"
	});

	jqXhr.done(function(data, textStatus, jqXHR) {
		// alert( "success: " + JSON.stringify(data) );
	})
	.fail(function( jqXHR, textStatus, errorThrown) {
		alert( "error: " + errorThrown );
	})
	.always(function() {
		// alert( "complete" );
	});	
	
}

function displayAnnotationDialog(btnId)
{   
	// alert( "here: " +  JSON.stringify( btnAddAnnotation ) );
	// btnAddAnnotation
	var userId = btnId.substring( 17 );
	// alert("clicked for id: " + userId )
	$j('#addAnnotationModal').data( 'userId', userId ).modal(
			  { keyboard: false }
	  );
}


$j(document).ready( function() 
{
	// alert( "Manage user profile stuff here...");
	/* setup typeahead completion for annotation objects */
	/*
		{
  			value: '@JakeHarding',
  			tokens: ['Jake', 'Harding']
		}	
	 */
	
	try
	{
		var jsonString = $j("#predicatesJSON").text();
		var json = $j.parseJSON( jsonString );
	
		var tahead = $j('#annotationObject').typeahead({
			name: 'objects',
			local: json
		});
	
		tahead.on( "typeahead:selected", function( e, datum ) {
			// alert( "setting from datum.qualifiedName: " + datum.qualifiedName );
			$j("#annotationObjectQN").val( datum.qualifiedName );
		} );
	}
	catch( err )
	{
		console.log( err );
	}
	
	// attach handler for "add to friends" button
	$j("button[id^='btnAddToFriends']").click( addToFriends );
	
	// attach handler for "follow user" button
	$j( "button[id^='btnFollowUser']" ).click( followUser );
	
	// attach handler for annotate button
	$j( "button[id^='btnAddAnnotation']" ).on( 'click', function() {
		var btnId = $j(this).attr('id');
		displayAnnotationDialog(btnId);
	});

	
	$j('#submitAddAnnotation').click( function submitAddAnnotation()
	{
			// alert( "submitting..." );
			
			var userId = $j('#addAnnotationModal').data('userId');
			alert( "submitting for userId: " + userId );
			
			// find our form object and submit it...
			$j('#userId').val( userId );
				
			// alert("submitting...");
			$j('#addAnnotationForm').submit();

			$j( '#addAnnotationModal').modal('toggle');
			
	} );

	
	
});
