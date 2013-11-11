if( true )
	{
		return;
	}
	
	var obj = ${value};
	// alert( "OK2");
	// 				alert( obj['@graph'][0]['@id'] ); 
	
	
	var graph = obj['@graph'];
	
	// first, we have to loop over all our @graph elements and extract the
	// EntityAnnotations and store the TextAnnotations in an associative
	// array keyed by @id, so we can quickly find the TextAnnotation that
	// corresponds to a particular EntityAnnotation when we start actually
	// doing the substitutions to apply the annotation.
	
	var textAnnotations = {};
	var entityAnnotations = [];
	var owlThings = {};
	
	for( var i = 0; i < graph.length; i++)
	{
		// alert( "graphElem: " + i );
		var graphElem = graph[i];
		
		// var count = 0;
		for (var key in graphElem )
		{
			// alert( 'key: ' + key ); // + ', val: ' + graph[key] );
		
			// look for the @type attribute
			// dereference it's value(s) and look for
			// an enhancer:EntityAnnotation value
		
			
			if( key == "@type")
			{
				// alert( "working with an @type attribute!" );
				
				var typeAtt = graphElem[key];
	
				// alert( typeAtt );
				
				var isEntity = false;
				var isText = false;
				var isOwlThing = false;
				
				// alert( "here1");
				if( $j.isArray( typeAtt ))
				{
					// alert("isArray was true");
					
					for( var j = 0; j < typeAtt.length; j++) 
					{
						var typeVal = typeAtt[j];
						
						// alert( "@type: " + typeVal );
						
						if( typeVal == "enhancer:EntityAnnotation")
						{	
							isEntity = true;
							break;
						}
						else if( typeVal == "enhancer:TextAnnotation" )
						{
							isText = true;
							break;
						}
						else if( typeVal == "owl:Thing" )
						{
							isOwlThing = true;
							break;
						}
					}
				}
				else if( typeAtt == "enhancer:EntityAnnotation")
				{	
					// alert( "not an Array, but typeAtt is Entity");
					isEntity = true;
				}
				else if( typeAtt == "enhancer:TextAnnotation" )
				{
					isText = true;
				}
				else if( typeAtt == "" )
				{
					isOwlThing = true;
				}
				else
				{
					// alert( "neither, ignoring this one...");
					// ignore this
					isEntity = false;
				}
	
				if( isEntity == true )
				{
					// alert( "We found an Entity in here! :-) ");
					// alert( "found graphElem as EntityAnnotation with id " + graphElem['@id']);
					
					// store this in our entities array...
					entityAnnotations.push( graphElem );
					
				}
				else if( isText == true )
				{
					// If this is a TextAnnotation instead
					// store it in an array
					// keyed by the @id so
					// we can quickly retrieve this
					// info when we start working with
					// our EntityAnnotations
	
					var id = graphElem['@id'];
					id = id.replace( /\:/ig, "" );
					textAnnotations[id] = graphElem;
	
				}
				else if( isOwlThing == true )
				{
					// save this object in the things array using it's ID as the
					// key
					var id = graphElem['@id'];
					// id = id.replace( /\:/ig, "" );
					owlThings[id] = graphElem;
				}
				else
				{
					// just ignore it...
				}
				
			}
			else
			{
				// not @type
				// alert( "not @type, something else...");
			}
		}
	} 		
	
	// alert( "OK3");
	
	// ok we've parsed the entire graph.  Now loop over each Entity in the EntityAnnotation
	// list, find it's associated TextAnnotation, and then do the appropriate manipulations
	// to apply the annotation
	var toolTipped = [];
	
	// as we walk through this, keep track of what annotations we apply and
	// assign them an index that will be used to key them to the corresponding anchor
	// element.
	var appliedAnnotations = {};
	
	for( var ei = 0; ei < entityAnnotations.length; ei++ )
	{
		var entityAnnotation = entityAnnotations[ei];
		var id = entityAnnotation['dc:relation'];
	
		id = id.replace( /\:/ig, "" );
		
		// find the associated TextAnnotation
		var textAnnotation = textAnnotations[id];
					
		// find the text value that we are going to annotate
		var textToReplace = textAnnotation['enhancer:selected-text']['@value'];
		// alert( "working on annotation for " + textToReplace );
		
		var dbPediaLink = entityAnnotation['enhancer:entity-reference'];
	
	
		// alert( "found potential EntityAnnotation for TextAnnotation'" + id + "'");
	
		if( !toolTipped[id] )
		{
			
			// find the matching text in the enrichment div's contained text
			// and turn that text into a hyperlink that links to the dbpedia link
			var enrichedContentDiv = $j('#enrichedContent');
			var re = new RegExp(textToReplace,"ig");
			enrichedContentDiv.html( enrichedContentDiv.html().replace( re, '<a id="' + id + '" title="empty" href="' + dbPediaLink + '">' + textToReplace + '</a>' ));
	
			// alert( toolTipped[id] );
		
		
			// let's attach a tooltip as well, so we can add some additional context information
			var owlThing = owlThings[dbPediaLink];
			// alert( "adding initial tooltip: owlThing: " + owlThing );
			var comment = owlThing['rdfs:comment']['@value'];
			// alert( "for initial tooltip: comment: " + comment );
			
			
			// $j('#' + id ).tooltip({ content: comment });
			$j('#'+id).attr("title", comment );
			
			// alert( "added initial tooltip for anchor with id: " +$j('#'+id).attr('id') );
			// alert( "storing in appliedAnnotations for entity-reference: "  + id );
			toolTipped[id] = true;
			
			appliedAnnotations[id] = entityAnnotation;
		}
		else
		{
			// alert("testing a possible replacement" );
			
			// get the entityhub:entityRank of the existing tooltip text
			// and see if this one is higher.  If it is, we can apply this one
			// even if it's for the same text. 
			var prevEntityEnhancement = appliedAnnotations[id];
			var prevConfidence = prevEntityEnhancement['enhancer:confidence'];
			var thisConfidence = entityAnnotation['enhancer:confidence'];
			
			var prevEntityReference = prevEntityEnhancement['enhancer:entity-reference']
			// alert( "prevEntity resource: " + prevEntityReference + ", prevEntityConfidence: " + prevConfidence 
			// 		+ "\n" + "currEntity resource: " + entityAnnotation['enhancer:entity-reference'] + ", currEntityConfidence: " + thisConfidence );
	
			
			if( Number(thisConfidence) > Number(prevConfidence))
			{
				// switch the tooltip 
				// alert( "switching our tooltip here" );
				var link = $j('#' + id );
				// link.tooltip( "destroy" );
				// alert( "here1");
				link.attr( "href", dbPediaLink );
				// alert("here2");
				// let's attach a tooltip as well, so we can add some additional context information
				var owlThing = owlThings[dbPediaLink];
				// alert( "owlThing: " + owlThing );
				var comment = owlThing['rdfs:comment']['@value'];
				// alert( "new tooltip comment: " + comment );
				// link.tooltip({ content: comment });
				$j('#'+id).attr("title", comment );
				// alert("here3");
				toolTipped[id] = true;
				
				appliedAnnotations[id] = entityAnnotation;		
			}
			else
			{
				// don't do anything...
				// alert( "Don't switch...");
			}
		}
	}
	
	$j(document).tooltip();