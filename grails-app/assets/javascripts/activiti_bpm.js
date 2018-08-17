$j(function() 
{
	// select all the buttons that start with activityClaimBtn
	// attach an onclick handler
	$j( "button[id^='activityClaimBtn']" ).click( activitiClaimTask );
	
	// select all the buttons that start with activityCompleteBtn
	// attach an onclick handler
	$j( "button[id^='activityCompleteBtn']" ).click( activitiCompleteTask );
	
	// select all the buttons that start with activityTransferBtn
	// attach an onclick handler
	$j( "button[id^='activityTransferBtn']" ).click( activitiTransferTask );
	
	// select all the buttons that start with activitySuspendBtn
	// attach an onclick handler
	$j( "button[id^='activitySuspendBtn']" ).click( activitiSuspendTask );
	
	// select all the buttons that start with activityRefreshBtn
	// attach an onclick handler
	$j( "button[id^='activityRefreshBtn']" ).click( activitiRefreshTask );
	
	
});


function activitiClaimTask()
{
				// substring after activityClaimBtn.
	var id = $j(this).attr( "id" ).substring(17);
	
	// alert( "Claiming task with id: " + id );
	
	// make ajax call to the activitiBPM controller and to claim this task
	var jqXhr = $j.ajax({
			url: appContext + "/activitiBPM/claimTask",
			type: "POST",
			data: { id : id },
			dataType: "json"
		});
	
		jqXhr.done(function(data, textStatus, jqXHR) {
			// alert( "success: " + JSON.stringify(data) );
		})
		.fail(function( jqXHR, textStatus, errorThrown) {
			// alert( "error: " + errorThrown );
		})
		.always(function() {
			// alert( "complete" );
		});
}

function activitiCompleteTask()
{
				// substring after activityCompleteBtn.
	var id = $j(this).attr( "id" ).substring(20);	
	// alert( "Completing task with id: " + id  );
	
	window.location.href = appContext + "/activitiBPM/completeTaskForm/" + id;
	
}

function activitiTransferTask()
{
				// substring after activityTransferBtn.
	var id = $j(this).attr( "id" ).substring(20);
	
	alert( "Transfering task with id: " + id );
}

function activitiSuspendTask()
{
				// substring after activitySuspendBtn.
	var id = $j(this).attr( "id" ).substring(19);
	
	alert( "Suspending task with id: " + id );
}

function activitiRefreshTask()
{
				// substring after activityRefreshBtn.
	var id = $j(this).attr( "id" ).substring(19);
	
	alert( "Refreshing task with id: " + id );
}