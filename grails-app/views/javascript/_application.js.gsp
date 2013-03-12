    function openShareDialog(entryId) {
       window.open( "/admin/importUsers/", "Quoddy - Import Users", 
            "status = 1, height = 300, width = 300, resizable = 0"  )
    }
    
	$j(document).ready( 
			function()
		 	{
				// alert( "JQuery ready for action!" );

				$j('#loadMoreLink').data("page", 1 );
				// setup an onclick handler for the status submit button
				// and serialize the associated form along with it...
				$j('#updateStatusSubmit').bind( 'click',  
						function(event) {
							
							// do post to server...
							$j.post("${ createLink(controller:'activityStream', action:'submitUpdate')}", 
									$("#statusText").serialize(), 

									function(data) 
									{ 
										$j('#statusText').val('');


										// now reload the content of the activity stream DIV
										// so that we pick up the new addition.  That sets us up to
										// add our timer based stuff to update things dynamically.
										$j('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml')}" );
										
									}, 

									"html" );

							return false;
				} );						


				// setup timer to refresh our "pending messages" link
				$j(this).everyTime( 2000, 
								function() 
								{
									$j.get( "${ createLink(controller:'activityStream', action:'getQueueSize')}", 
											function(data) 
											{
												$j('#refreshMessagesLink').html( data + " messages pending" );
											} );
								}, 
							0 );


				$j('#refreshMessagesLink').bind( 'click', function() {

					$j('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml')}" );
					return false;
				})
				
				$j('#loadMoreLink').bind( 'click', function() {
					
					var page = $j('#loadMoreLink').data("page");
					page = page +1;
					$j('#loadMoreLink').data("page", page );
					
					if( page > 1 )
					{
						// load more content from the server, pass the page so it knows how much
						// to return us...
						
						$j('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml')}" + "?page=" + page );
					}
					
					return false;
				} );
				
				$j( '.addCommentTextInput' ).bind( 'focus', function()  {
				
					var clicked = $j(this);
					clicked.val("" );
					clicked.parent().find('#submitCommentBtn').css( 'display', 'inline' );
					clicked.parent().find('#cancelCommentBtn').css( 'display', 'inline' );
					
				} );
				
				
				$j( '.cancelCommentBtn' ).bind( 'click', function()  {
					
					// alert( 'OK' );
					var clicked = $j(this);
					var submitBtn = clicked.parent().find( '#submitCommentBtn' );
					submitBtn.css('display', 'none');
					clicked.css('display', 'none');
					return false;
				} );
				
				
				$j( '.submitCommentBtn' ).bind( 'click', function( event ) {
				
					alert( "submitting Comment!" );
					// alert( "this: " + $j(this));
					// alert( "this: " + $j(this).css('class'));
					// find our parent <form> element and serialize it
					// it will have class ".addCommentForm"
					// alert( "this: " + $j(this));
					// alert( "this: " + $j(this).css('class'));
					// alert( "this: " + $j(this).css('id'));
					// alert( "this: " + $j(this).css('name'));
					
					var form = $j(this).parent(); // (".addCommentForm").first();
					alert( "submitting form: " + form.serialize() );
					
					$j.post("${ createLink(controller:'comment', action:'addComment')}", 
									form.serialize(), 

									function(data) 
									{ 
										alert( "whatever");
										// var form = $j(this).parents(".addCommentForm");
										// form.find('.addCommentTextInput').val('');

										// TODO: attach the new comment here
										
										

										/* OLD STUFF C&P'd for REFERENCE ONLY */
										// now reload the content of the activity stream DIV
										// so that we pick up the new addition.  That sets us up to
										// add our timer based stuff to update things dynamically.
										// $j('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml')}" );
										
									}, 

									"html" );

							return false;
					
					
					
				});
				
			} );
			
			
			    