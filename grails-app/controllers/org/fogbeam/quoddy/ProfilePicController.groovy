package org.fogbeam.quoddy

class ProfilePicController
{
	def thumbnail = 
	{
		
		if( !params.id ) 
		{
			[]	
		}
		else 
		{
			log.debug( "params.userId found: ${params.id}");
			String quoddyHome = System.getProperty( "quoddy.home" );
			String filePath = 
				"${quoddyHome}/profilepics/${params.id}/${params.id}_profile_thumbnail48x48.jpg";
			log.debug( "filepath is ${filePath}");
			File thumbnailFile = new File(filePath);
			byte[] image = thumbnailFile.getBytes();
			log.debug( "image size: ${image.length}");
			response.setHeader("Content-Type", "image/jpeg");
			response.setHeader("Content-Disposition", "attachment;filename=mypic.jpeg");
			response.setHeader("Content-Length", "${image.length}");
			log.debug( "class: " + response.outputStream.getClass().getName());
			response.outputStream.write( image );
			response.outputStream.flush();
			log.debug( "done");
			return;
		}
	}
	
	def full = 
	{
		if( !params.id )
		{
			[]
		}
		else
		{
			log.debug( "params.userId found: ${params.id}");
			String quoddyHome = System.getProperty( "quoddy.home" );
			String filePath =
				"${quoddyHome}/profilepics/${params.id}/${params.id}_profile.jpg";
			log.debug( "filepath is ${filePath}");
			File picFile = new File(filePath);
			byte[] image = picFile.getBytes();
			log.debug( "image size: ${image.length}");
			response.setHeader("Content-Type", "image/jpeg");
			response.setHeader("Content-Disposition", "attachment;filename=mypic.jpeg");
			response.setHeader("Content-Length", "${image.length}");
			log.debug(  "class: " + response.outputStream.getClass().getName());
			response.outputStream.write( image );
			response.outputStream.flush();
			log.debug( "done");
			return;
		}
	}
}
