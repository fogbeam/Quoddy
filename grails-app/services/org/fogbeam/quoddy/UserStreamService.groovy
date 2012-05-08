package org.fogbeam.quoddy

class UserStreamService
{
	public List<UserStream> getSystemDefinedStreamsForUser( final User user )
	{
		List<UserStream> streams = new ArrayList<UserStream>();
	
		// select activity from Activity as activity where
		List<UserStream> tempStreams = UserStream.executeQuery( "select stream from UserStream as stream where stream.owner = :owner and stream.definedBy = :definedBy", 
																['owner':user,'definedBy':UserStream.DEFINED_SYSTEM] );
		if( tempStreams )
		{
			streams.addAll( tempStreams );
		}

			
		return streams;
	}

	public UserStream getStreamForUser( final User user, String streamName ) 
	{
		UserStream stream = UserStream.executeQuery( "select stream from UserStream as stream where " 
													 + "stream.owner = ? and stream.name = ?", [user, streamName ] );
												 
		return stream;
	}
	
	public UserStream findStreamById( final Long streamId ) 
	{
		UserStream stream = UserStream.findById( streamId );
		return stream;
	}
	
	
	public List<UserStream> getSystemDefinedStreamsForUser( final User user, final int maxCount )
	{
		List<UserStream> streams = new ArrayList<UserStream>();
		
		List<UserStream> tempStreams = UserStream.executeQuery( "select stream from UserStream as stream where stream.owner = :owner and stream.definedBy = :definedBy", 
																['owner':user,'definedBy':UserStream.DEFINED_SYSTEM], ['max':maxCount] );
		if( tempStreams )
		{
			streams.addAll( tempStreams ); 
		}
		
		return streams;
	}

		
	public List<UserStream> getUserDefinedStreamsForUser( final User user )
	{
		List<UserStream> streams = new ArrayList<UserStream>();
		
		List<UserStream> tempStreams = UserStream.executeQuery( "select stream from UserStream as stream where stream.owner = :owner and stream.definedBy = :definedBy", 
																['owner':user,'definedBy':UserStream.DEFINED_USER] );
		if( tempStreams )
		{
			streams.addAll( tempStreams );
		}

		
		return streams;
	}
	
	public List<UserStream> getUserDefinedStreamsForUser( final User user, final int maxCount )
	{
		List<UserStream> streams = new ArrayList<UserStream>();
		
		List<UserStream> tempStreams = UserStream.executeQuery( "select stream from UserStream as stream where stream.owner = :owner and stream.definedBy = :definedBy", 
																['owner':user,'definedBy':UserStream.DEFINED_USER], ['max':maxCount] );
		if( tempStreams )
		{
			streams.addAll( tempStreams );
		}

		
		return streams;
	}
		
}
