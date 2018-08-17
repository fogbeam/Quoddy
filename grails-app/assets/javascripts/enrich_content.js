$j(document).ready( function() 
{
				
	// alert( "Starting content enhancement...");
	
	// locate all the <script> elements with class "enhancementJSON"
	
	// iterate over each of them
	
		// parse the json 
	
		// and find the corresponding Status Update note to apply the annotation
		// within
	var scriptNodes = $j( ".enhancementJSON" );
	
	scriptNodes.each( function() {
	
		var jsonNode = $j( this );
		var json = jsonNode.text();
		
		var obj = $j.parseJSON(json);
		
		// alert( obj['@graph']);
		
		var graph = obj['@graph'];
		
		if( graph == null )
		{
			return;
		}
		
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
		
					// alert( "working with an @type attribute: " + typeAtt );
					
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
							
							if( typeVal == "enhancer:EntityAnnotation" || typeVal == "EntityAnnotation")
							{	
								isEntity = true;
								break;
							}
							else if( typeVal == "enhancer:TextAnnotation" || typeVal == "TextAnnotation" )
							{
								isText = true;
								break;
							}
							else if( typeVal == "owl:Thing" || typeVal == "Thing" )
							{
								// alert( "isOwlThing = true" );
								isOwlThing = true;
								break;
							}
						}
					}
					else if( typeAtt == "enhancer:EntityAnnotation" || typeAtt == "EntityAnnotation")
					{	
						// alert( "not an Array, but typeAtt is Entity");
						isEntity = true;
					}
					else if( typeAtt == "enhancer:TextAnnotation" || typeAtt == "TextAnnotation" )
					{
						isText = true;
					}
					else if( typeAtt == "" )
					{
						// alert( "typeAtt: " + typeAtt );
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
					// alert( "saving into owlThings array using id: " + id );
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
			
			// alert( JSON.stringify( entityAnnotation ));
			
			var id = entityAnnotation['dc:relation'];
			
			if( !id )
			{
				id = entityAnnotation['relation'];
			}
			
			
			id = id.replace( /\:/ig, "" );
			
			// find the associated TextAnnotation
			var textAnnotation = textAnnotations[id];
						
			// find the text value that we are going to annotate
			var textToReplace = null;
			try
			{
				textToReplace = textAnnotation['enhancer:selected-text']['@value'];
			}
			catch( e )
			{}
			
			if( !textToReplace)
			{
				textToReplace = textAnnotation['selected-text']['@value'];
			}
			// alert( "working on annotation for " + textToReplace );
			
			var dbPediaLink = null;
			try
			{
				dbPediaLink = entityAnnotation['enhancer:entity-reference'];
			}
			catch( e )
			{
				
			}
			
			if( !dbPediaLink )
			{
				dbPediaLink = entityAnnotation['entity-reference'];
			}
			
			// alert( "looking for: " + dbPediaLink );
		
			// alert( "found potential EntityAnnotation for TextAnnotation'" + id + "'");
		
			if( !toolTipped[id] )
			{
				
				// find the matching text in the enrichment div's contained text
				// and turn that text into a hyperlink that links to the dbpedia link
				
				
				/* NOTE: this has to change to support the new approach */
				
				var enrichedContentDiv = jsonNode.siblings(".basicActivityStreamEntry");
				// alert( enrichedContentDiv );
				

				var owlThing = owlThings[dbPediaLink];
				
				var comment = null;
				try
				{
					comment = owlThing['rdfs:comment']['@value'];
				}
				catch(e)
				{
					
				}
				
				if( !comment )
				{
					comment = "No comment";
				}
				// alert( "for initial tooltip: comment: " + comment );
				
				
				var re = new RegExp(textToReplace,"ig");
				if( !dbPediaLink.startsWith( "http://customers.fogbeam" ))
				{
					enrichedContentDiv.html( enrichedContentDiv.html().replace( re, '<a id="' + id + '" title="empty" href="' + dbPediaLink + '">' + textToReplace + '</a>' ));
				}
				else
				{
					// alert( "fogcust found" );
					// alert( JSON.stringify( owlThing ));
					var internalLink = owlThing['http://schema.fogbeam.com#internalLink'];
					enrichedContentDiv.html( enrichedContentDiv.html().replace( re, '<a id="' + id + '" title="empty" href="' + internalLink + '">' + textToReplace + '</a>' ));
					
					comment = owlThing['description'];
					
				}
			
				// alert( toolTipped[id] );
				// let's attach a tooltip as well, so we can add some additional context information
				// alert( "adding initial tooltip: owlThing: " + owlThing );

				
				
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
	});
	
	try
	{
		$j(document).tooltip();	
	}
	catch( err )
	{
		console.log( err );
	}
});