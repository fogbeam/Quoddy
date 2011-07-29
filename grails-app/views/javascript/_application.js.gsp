    function openShareDialog(entryId) {
       window.open( "/admin/importUsers/", "Quoddy - Import Users", 
            "status = 1, height = 300, width = 300, resizable = 0"  )
    }
    
	$(document).ready( 
			function()
		 	{
				// alert( "JQuery ready for action!" );

				$('#loadMoreLink').data("page", 1 );
				// setup an onclick handler for the status submit button
				// and serialize the associated form along with it...
				$('#updateStatusSubmit').bind( 'click',  
						function(event) {
							
							// do post to server...
							$.post("${ createLink(controller:'activityStream', action:'submitUpdate')}", 
									$("#statusText").serialize(), 

									function(data) 
									{ 
										$('#statusText').val('');


										// now reload the content of the activity stream DIV
										// so that we pick up the new addition.  That sets us up to
										// add our timer based stuff to update things dynamically.
										$('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml')}" );
										
									}, 

									"html" );

							return false;
				} );						


				// setup timer to refresh our "pending messages" link
				$(this).everyTime( 2000, 
								function() 
								{
									$.get("${ createLink(controller:'activityStream', action:'getQueueSize')}", 
											function(data) 
											{
												$('#refreshMessagesLink').html( data + " messages pending" );
											} );
								}, 
							0 );


				$('#refreshMessagesLink').bind( 'click', function() {

					$('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml')}" );
					return false;
				})
				
				$('#loadMoreLink').bind( 'click', function() {
					
					var page = $('#loadMoreLink').data("page");
					page = page +1;
					$('#loadMoreLink').data("page", page );
					
					if( page > 1 )
					{
						// load more content from the server, pass the page so it knows how much
						// to return us...
						
						$('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml')}" + "?page=" + page );
					}
					
					return false;
				} );
				
				
			} );    