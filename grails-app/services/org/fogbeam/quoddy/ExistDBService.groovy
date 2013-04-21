package org.fogbeam.quoddy

import javax.xml.transform.OutputKeys

import org.exist.storage.serializers.EXistOutputKeys
import org.fogbeam.quoddy.stream.StreamItemBase;
import org.fogbeam.quoddy.stream.BusinessEventSubscriptionItem;
import org.xmldb.api.DatabaseManager
import org.xmldb.api.base.Collection
import org.xmldb.api.base.Database
import org.xmldb.api.modules.XMLResource

class ExistDBService
{
	
	protected static String driver = "org.exist.xmldb.DatabaseImpl";
	protected static String URI = "xmldb:exist://localhost:8090/exist/xmlrpc";

	public populateSubscriptionEventWithXmlDoc( Object event )
	{
		// unless we're passed a BusinessEventSubscriptionItem instance, this is a NOP
		println "NOT a BusinessEventSubscriptionItem, nothing to do!";
		return event;
	}
		
	public populateSubscriptionEventWithXmlDoc( StreamItemBase event ) 
	{
		// unless we're passed a BusinessEventSubscriptionItem instance, this is a NOP
		println "NOT a BusinessEventSubscriptionItem, nothing to do!";
		return event;	
	}
	
	public BusinessEventSubscriptionItem populateSubscriptionEventWithXmlDoc( BusinessEventSubscriptionItem event )
	{
		
		println "It's a BusinessEventSubscriptionItem, populate XML body";
		// get the XML uuid from the event, pull the XML from the existDB instance
		// and populate it into the object for rendering in the stream
		
		String collection = "/db/hatteras";
		System.out.println( "Collection: " + collection );
		
		// initialize database drivers
		Class cl = Class.forName(driver);
		Database database = (Database) cl.newInstance();
		DatabaseManager.registerDatabase(database);

		// get the collection
		Collection col = DatabaseManager.getCollection(URI + collection);
		if( col == null ) 
		{
			println "ERROR: could not locate collection: ${URI + collection}";
			return event;
		}
		
		col.setProperty(OutputKeys.INDENT, "yes");
		col.setProperty(EXistOutputKeys.EXPAND_XINCLUDES, "no");
		col.setProperty(EXistOutputKeys.PROCESS_XSL_PI, "yes");
		
		String xmlUuid = event.xmlUuid;
		
		XMLResource res = (XMLResource)col.getResource(xmlUuid);
		if(res == null)
			System.out.println("document not found!");
		else {
			println "Found document for id: ${xmlUuid}";
			// String content = res.getContent();
			// println "got content as: ${content}";
			// StringReader xmlDoc = new StringReader( content );
			org.w3c.dom.Node xmlDoc = res.getContentAsDOM();
			event.xmlDoc= xmlDoc;
		}
		
		return event;
	}
	
}
