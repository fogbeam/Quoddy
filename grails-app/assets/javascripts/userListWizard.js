function addToSelected() {
	// alert( "addToSelected" );
	
	$j( 'select#availableusers :selected' ).each( function(i, selected )
		{
		
			// check if this user is already in "usersToRemove".  If it is, somebody
			// clicked "remove" then re-added it.  But since the net-net of that
			// is the same settings we currently have, we don't actually want to do
			// anything.
			var checkValue = $j(selected).attr('value');
			// alert( "checkValue: " + checkValue );
			var existingOption = $j("select#usersToRemove option[value='" + checkValue + "']");
			// alert( "existing option length: " + existingOption.length );
			if( existingOption.length > 0 )
			{		
				// don't add to usersToAdd,  but we do need to re-add it back 
				// to the "users" list, so it "looks" selected
				var newOption2 = $j(selected).clone();
				newOption2.appendTo( "select#users" );
			}
			else
			{
				// do everything as normal
				var newOption = $j(selected).clone();
				newOption.attr('selected', 'selected');
				newOption.appendTo( "select#usersToAdd" );
				var newOption2 = $j(selected).clone();
				newOption2.appendTo( "select#users" );				
			
			}
			
			var removeMeId = $j(selected).attr('value');
			$j(selected).remove();
			
			// if this had previously been selected for removal (and then added
			// back) , then it's in usersToRemove now.  We need to remove it from there, since
			// we've decided not to remove it.				
			var removeMe = $j("select#usersToRemove option[value='" + removeMeId + "']");
			if( removeMe )
			{
				removeMe.remove();
			}
			
		}
	);
}

function addAllToSelected() {
	// alert( "addAllToSelected" );
	
	$j( 'select#availableusers option' ).each( function(i, selected )
		{	

			// check if this user is already in "users".  If it is, somebody
			// clicked "remove" then re-added it.  But since the net-net of that
			// is the same settings we currently have, we don't actually want to do
			// anything.
			var existingOption = $j("select#usersToRemove option[value='" + $j(selected).attr('value') + "']");
			// alert( "existing option length: " + existingOption.length );
			if( existingOption.length > 0 )
			{		
				// don't add to usersToAdd,  but we do need to re-add it back 
				// to the "users" list, so it "looks" selected
				var newOption2 = $j(selected).clone();
				newOption2.appendTo( "select#users" );
			}
			else
			{
				// do everything as normal
				var newOption = $j(selected).clone();
				newOption.attr('selected', 'selected');
				newOption.appendTo( "select#usersToAdd" );
				var newOption2 = $j(selected).clone();
				newOption2.appendTo( "select#users" );				
			
			}
			
			var removeMeId = $j(selected).attr('value');
			$j(selected).remove();
			
			// if this had previously been selected for removal (and then added
			// back) , then it's in usersToRemove now.  We need to remove it from there, since
			// we've decided not to remove it.				
			var removeMe = $j("select#usersToRemove option[value='" + removeMeId + "']");
			if( removeMe )
			{
				removeMe.remove();
			}
			
			
		}
	);	
}

function removeFromSelected() {
	// alert( "removeFromSelected" );
	$j( 'select#users :selected' ).each( function(i, selected )
		{
			var newOption = $j(selected).clone();
			newOption.attr('selected', 'selected');
			newOption.appendTo( "select#usersToRemove" );
			
			var newOption2 = $j(selected).clone();
			newOption2.appendTo( "select#availableusers" );
			// remove this from the original list
			var removeMeId = $j(selected).attr('value');
			$j(selected).remove();
			
			// if this had previously been added from availableusers, then
			// it's in usersToAdd now.  We need to remove it from there, since
			// we've decided not to add it.				
			var removeMe = $j("select#usersToAdd option[value='" + removeMeId + "']");
			if( removeMe )
			{
				removeMe.remove();
			}
		}
	);

}

function removeAllFromSelected() {
	// alert( "removeAllFromSelected" );
	
	$j( 'select#users option' ).each( function(i, selected )
		{	
			var newOption = $j(selected).clone();
			newOption.attr('selected', 'selected');
			newOption.appendTo( "select#usersToRemove" );
			var newOption2 = $j(selected).clone();
			newOption2.appendTo( "select#availableusers" );
			
			$j(selected).remove();					
		}
	);	

	// if we've said "remove all" from selected users, then we definitely
	// aren't adding any users.  So blow away anything that might be
	// in usersToAdd
	$j( 'select#usersToAdd option' ).each( function(i, selected )
		{	
			$j(selected).remove();			
		}
	);
	
}