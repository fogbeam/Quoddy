package org.fogbeam.quoddy

class SpecialController
{

	def index = 
	{
		
		switch( request.method )
		{
			case "POST":
				println "POST: ${params}";
				println "text:\n\n ${request.reader.text}\n\n";
				break;
			default:
				println "OTHER: ${params}";
				println "text:\n\n ${request.reader.text}\n\n";
				break;
		}
		
		render(text: "OK", contentType: "text/plain", encoding: "UTF-8")
	}
}
