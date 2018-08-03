$j(document).ready( function() 
{
	// find all the <divs> that are basic basicActivityStreamEntry
	// alert( "ready to start finding hashtags" );
	
	var aseNodes = $j( "div.basicActivityStreamEntry" );
	aseNodes.each( function() {
		// alert( "found a node");
		var textNode = $j( this );
		var text = textNode.text();
		// alert( "Text: " + text );
		
		// we know this text has a hashtag in it, now we need to pick out the
		// actual hashtag itself, so we can replace it with the linked
		// representation
		var tagslist = text.trim().split(' ');
		var arr=[];
		$j.each(tagslist,function(i,val) {
		    // alert( "token: " + val );
			if( tagslist[i].indexOf('#') == 0) {
				// alert( "this is a hashtag: " + val );
				arr.push(tagslist[i]);  
		    }
		    
		});
		
		$j.each( arr, function(i,val) {
			// alert( "here's a hashtag: " + val);
			// now do the replace
			var re = new RegExp(val,"ig");
			textNode.html( textNode.html().replace( re, '<a href="${createLink(controller:'search', action:'doSearch')}?queryString=' + encodeURIComponent(val) + '">' + val + '</a>' ));
	
		});
		
				
	});

});