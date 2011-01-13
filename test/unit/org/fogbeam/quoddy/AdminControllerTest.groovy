package org.fogbeam.quoddy

import grails.test.ControllerUnitTestCase;
import groovy.lang.MetaClass;

class AdminControllerTest extends ControllerUnitTestCase 
{
	
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testIndex()
	{
		def returnMap = controller.index();
		
		assertEquals( [], returnMap );
	}
	
/*
    void testUpdateNotFound() 
	{
		
		def bm1 = new Bookmark( url: new URL("http://www.example.com/1"), title: "Bookmark1", notes:"", 
								dateCreated:new Date(), type: "blog", id:1 );
		def bm2 = new Bookmark( url: new URL("http://www.example.com/2"), title: "Bookmark1", notes:"", 
								dateCreated:new Date(), type: "blog", id:2 );
		

		mockDomain(Bookmark, [bm1, bm2])
		controller.metaClass.message = {args -> println "foo: ${args}"}
		controller.params.id = 27;
		controller.update();
		assertEquals "list", controller.redirectArgs["action"]		
		
		
    }		
 */
	
}
