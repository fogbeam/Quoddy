package org.fogbeam.quoddy

class UserStreamDefinitionService
{
	public List<UserStreamDefinition> getSystemDefinedStreamsForUser( final User user )
	{
		List<UserStreamDefinition> streams = new ArrayList<UserStreamDefinition>();
	
		// select activity from ActivityStreamItem as activity where
		List<UserStreamDefinition> tempStreams = UserStreamDefinition.executeQuery( "select stream from UserStreamDefinition as stream where stream.owner = :owner and stream.definedBy = :definedBy", 
																['owner':user,'definedBy':UserStreamDefinition.DEFINED_SYSTEM] );
		if( tempStreams )
		{
			streams.addAll( tempStreams );
		}

			
		return streams;
	}

	public UserStreamDefinition getStreamForUser( final User user, String streamName ) 
	{
		List<UserStreamDefinition> streams = UserStreamDefinition.executeQuery( "select stream from UserStreamDefinition as stream where " 
													 + "stream.owner = ? and stream.name = ?", [user, streamName ] );
		
		if( streams.size() != 1 ){
			throw new RuntimeException( "Query returned the wrong number of results" );	
		}						
						 
		return streams.get(0); 
	}
	
	public UserStreamDefinition findStreamById( final Long streamId ) 
	{
		UserStreamDefinition stream = UserStreamDefinition.findById( streamId );
		return stream;
	}
	
	
	public List<UserStreamDefinition> getSystemDefinedStreamsForUser( final User user, final int maxCount )
	{
		List<UserStreamDefinition> streams = new ArrayList<UserStreamDefinition>();
		
		List<UserStreamDefinition> tempStreams = UserStreamDefinition.executeQuery( "select stream from UserStreamDefinition as stream where stream.owner = :owner and stream.definedBy = :definedBy", 
																['owner':user,'definedBy':UserStreamDefinition.DEFINED_SYSTEM], ['max':maxCount] );
		if( tempStreams )
		{
			streams.addAll( tempStreams ); 
		}
		
		return streams;
	}

		
	public List<UserStreamDefinition> getUserDefinedStreamsForUser( final User user )
	{
		List<UserStreamDefinition> streams = new ArrayList<UserStreamDefinition>();
		
		List<UserStreamDefinition> tempStreams = UserStreamDefinition.executeQuery( "select stream from UserStreamDefinition as stream where stream.owner = :owner and stream.definedBy = :definedBy", 
																['owner':user,'definedBy':UserStreamDefinition.DEFINED_USER] );
		if( tempStreams )
		{
			streams.addAll( tempStreams );
		}

		
		return streams;
	}
	
	public List<UserStreamDefinition> getUserDefinedStreamsForUser( final User user, final int maxCount )
	{
		List<UserStreamDefinition> streams = new ArrayList<UserStreamDefinition>();
		
		List<UserStreamDefinition> tempStreams = UserStreamDefinition.executeQuery( "select stream from UserStreamDefinition as stream where stream.owner = :owner and stream.definedBy = :definedBy", 
																['owner':user,'definedBy':UserStreamDefinition.DEFINED_USER], ['max':maxCount] );
		if( tempStreams )
		{
			streams.addAll( tempStreams );
		}

		
		return streams;
	}
		
}
