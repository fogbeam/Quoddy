package org.fogbeam.quoddy

import org.fogbeam.quoddy.stream.StreamItemBase;

class DummyController
{
	def grailsApplication;
	
	def index = {
	
		def foo = grailsApplication.config.foo.bar.baz;
		println "friends.backingStore: " + grailsApplication.config.friends.backingStore;
		println "foo.bar.baz: ${foo}";
		[value:foo];
	}
}
