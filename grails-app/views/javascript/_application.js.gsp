    function openShareDialog(entryId) {
       window.open( "/admin/importUsers/", "Quoddy - Import Users", 
            "status = 1, height = 300, width = 300, resizable = 0"  )
    }
    
	$j(document).ready( 
			function()
		 	{
				// alert( "JQuery ready for action!" );
				$j( "#dialog" ).dialog(
				{
					autoOpen: false,
					show: {
					effect: "blind",
					duration: 200
					},
					hide: {
					effect: "explode",
					duration: 1500
					},
					buttons: [ { text: "Cancel", click: function() { $j( this ).dialog( "close" ); } },
							   { text: "Submit", click: function() 
				   					{ 
				   						
				   						var shareItemUuid = $j(this).data('shareItemUuid');
				   							
				   						// find our form object and submit it...
				   						$j('#shareItemUuid').val( shareItemUuid );
				   						$j('#shareItemForm').submit();
				   						// alert("submitting...");
				   						$j( this ).dialog( "close" );
				   					
				   					} 
							 	} 
							 ]
				});


				$j( ".shareButton" ).click(function() {
				
					var name = $j(this).attr('name');
					var shareItemUuid = name.split( "." )[1];
					$j( "#dialog" ).data('shareItemUuid', shareItemUuid).dialog( "open" );
				});




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
										$j('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml',
																		params:['streamId':streamId])}" );
										
									}, 

									"html" );

							return false;
				} );						


				// setup timer to refresh our "pending messages" link
				$j(this).everyTime( 2000, 
								function() 
								{
									$j.get( "${ createLink(controller:'activityStream', action:'getQueueSize',
															 params:['streamId':streamId ] )}", 
											function(data) 
											{
												$j('#refreshMessagesLink').html( data + " messages pending" );
											} );
								}, 
							0 );


				$j('#refreshMessagesLink').bind( 'click', function() {

					$j('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml', params:['streamId':streamId ] )}" );
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
						
						$j('#activityStream').load( "${ createLink(controller:'activityStream', action:'getContentHtml', params:['streamId':streamId, 'page':page ] )}" );
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
				
				
				$j( '.showHideCommentsButton' ).bind ( 'click', function() {
				
					// alert( 'OK' );
					var clicked = $j(this);
				
					// we really don't need to do any of this if the comment
					// count isn't > 0
					// var intermediate = clicked.parent( '.aseFooter' );					
					var wrapperDiv = clicked.parents( '.aseWrapper' ).eq(0);
					var individualCommentBoxes = wrapperDiv.find( '.individualCommentBox' );
					if( individualCommentBoxes == null || individualCommentBoxes.length < 1 ) 
					{
						// alert( "no comments yet" );
						return false;
					}
					else
					{
						// alert( "toggling comment display" );
						// if the button says "show" then slidedown and change the
						// text to "hide""
						var text = clicked.find('a').text();
						// alert( "text = " + text );
						if( text == "Show Comments")
						{
							clicked.find('a').text("Hide Comments");
							wrapperDiv.find( '.commentsArea' ).slideDown();
						}					
						else if( text == "Hide Comments" )
						{
							// if it says "hide" then slide up and change the text to
							// "show"
							clicked.find('a').text("Show Comments");
							wrapperDiv.find( '.commentsArea' ).slideUp();
						}
					}		
				} );
				
				$j( '.submitCommentBtn' ).bind( 'click', function( event ) {
									
					var form = $j(this).parent(); // (".addCommentForm").first();
					
					// alert( "submitting form: " + form.serialize() );
					var theButton = $j(this);
					
					$j.post("${ createLink(controller:'comment', action:'addComment')}", 
									form.serialize(), 

									function(data) 
									{ 
									
										// clear the text input box after submitting and hide
										// the buttons
										var inputBox = form.find( ".addCommentTextInput" );
										inputBox.val( "" );
										form.find('.submitCommentBtn').css( 'display', 'none' );
										form.find('.cancelCommentBtn').css( 'display', 'none' );
									
										// alert( "refreshing comments in Javascript...");
										// alert( "this: " + $j('<div>').append(theButton.clone()).html() );
										
										var commentBoxWrapper = theButton.parents(".commentBoxWrapper");
										var commentsDiv = commentBoxWrapper.find(".commentsArea");
										commentsDiv.html( data );
										return true;
									}, 

									"html" );

							return false;					
				});
				
			} );
			
			
			    