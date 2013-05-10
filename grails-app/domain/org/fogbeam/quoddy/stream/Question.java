package org.fogbeam.quoddy.stream;

public class Question extends StreamItemBase
{
	public Question()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
		
	}
	
	
	String uuid;
}
