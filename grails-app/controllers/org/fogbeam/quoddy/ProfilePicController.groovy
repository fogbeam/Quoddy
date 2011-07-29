package org.fogbeam.quoddy

class ProfilePicController
{
	def thumbnail = {
		
		if( !params.id ) 
		{
			[]	
		}
		else 
		{
			println( "params.userId found: ${params.id}");
			String filePath = 
				"/development/projects/fogbeam/quoddy/quoddy2/profilepics/${params.id}/${params.id}_profile_thumbnail48x48.jpg";
			println( "filepath is ${filePath}");
			File thumbnailFile = new File(filePath);
			byte[] image = thumbnailFile.getBytes();
			println( "image size: ${image.length}");
			response.setHeader("Content-Type", "image/jpeg");
			response.setHeader("Content-Disposition", "attachment;filename=mypic.jpeg");
			response.setHeader("Content-Length", "${image.length}");
			// println image;
			println "class: " + response.outputStream.getClass().getName();
			response.outputStream.write( image );
			response.outputStream.flush();
			println "done";
			return;
		}
	}
}
