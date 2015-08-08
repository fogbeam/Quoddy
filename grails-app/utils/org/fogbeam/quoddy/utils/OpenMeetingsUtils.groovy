package org.fogbeam.quoddy.utils

import java.io.StringReader;

class OpenMeetingsUtils
{
	public static String convertStringReaderToString( StringReader reader )
	{
		int numChars = -1;
		char[] chars = new char[100];
		StringBuilder builder = new StringBuilder();
		while( ( numChars = reader.read(chars,0,chars.length ) ) > 0 )
		{
			builder.append( chars, 0, numChars );
		}
		
		String stringReadFromReader = builder.toString();
		return stringReadFromReader;
	}

}
