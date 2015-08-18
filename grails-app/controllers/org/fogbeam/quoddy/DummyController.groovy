package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.StreamItemBase;

/*
	TODO: figure out what this was for, and (mostly likely) delete if, if it isn't needed anymore.
*/
class DummyController
{
	def grailsApplication;
	
	def index = {
	
		def foo = grailsApplication.config.foo.bar.baz;
		log.debug( "friends.backingStore: " + grailsApplication.config.friends.backingStore );
		log.debug( "foo.bar.baz: ${foo}" );
		[value:foo];
	}
}
