package org.fogbeam.quoddy.hashtag

class HashTagParser
{
	List<String> findHashtags( String inputText )
	{
		// loop through all of the tokens in the input and for
		// any that start with a # sign, return them in the
		// list of Hashtags
		
		String[] candidateTags = inputText.split( "\\s+" );
		
		List<String> hashtags = new ArrayList<String>();
		
		for( String candidateTag : candidateTags )
		{
			// should we have a minimum length?  And or a rule that
			// it's not a hashtag if it has only numeric characters?
			if( candidateTag.startsWith( "#" ) && candidateTag.length() >= 3 && !isAllNumeric( candidateTag ))
			{
				hashtags.add( candidateTag );
			}
		}
		
		return hashtags;
	}
	
	static boolean isAllNumeric( String testString )
	{
		if( testString.matches( "[0-9]+" ))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
