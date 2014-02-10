package org.fogbeam.quoddy.jms

import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.URLENC
import groovyx.net.http.RESTClient

import javax.jms.MapMessage
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import jenajsonld.JenaJSONLD

import org.apache.http.HttpEntity
import org.apache.http.HttpException
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.jena.riot.RDFDataMgr
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.DateTools
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.index.TermDocs
import org.apache.lucene.index.IndexWriter.MaxFieldLength
import org.apache.lucene.store.Directory
import org.apache.lucene.store.NIOFSDirectory
import org.apache.lucene.util.Version
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.Parser
import org.apache.tika.sax.BodyContentHandler
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.stream.ActivitiUserTask
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.BusinessEventSubscriptionItem
import org.fogbeam.quoddy.stream.CalendarFeedItem
import org.fogbeam.quoddy.stream.RssFeedItem
import org.fogbeam.quoddy.stream.StatusUpdate

import com.hp.hpl.jena.query.Dataset
import com.hp.hpl.jena.query.ReadWrite
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.rdf.model.ResIterator
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.tdb.TDBFactory
import com.hp.hpl.jena.vocabulary.DCTerms
import com.hp.hpl.jena.vocabulary.OWL
import com.hp.hpl.jena.vocabulary.RDF

public class SearchQueueInputService
{
	
	def siteConfigService;
	def eventStreamService;
	def searchService;
	def userService;
	
    static expose = ['jms']
    static destination = "quoddySearchQueue"                 
	
    def onMessage(msg)
    { 
    	System.out.print("INONMESSSAGE:::")
		System.out.println(msg)
    	/* note: what we would ordinarily do where is turn around and copy this message
    	 * to other queue's, topics, etc., or otherwise route it as needed.  But for
    	 * now we just assume we are the "indexer" job.
    	 */
    	
    	log.debug( "GOT MESSAGE: ${msg}" ); 
    
    	if( msg instanceof java.lang.String )
    	{
			System.out.println(":::Message is a string")	
    	}
    	else
    	{
			println( "Received message: ${msg}" );
			println "Received message: ${msg}";
			MapMessage mapMessage = (MapMessage)msg;
    		String msgType = mapMessage.getString( "msgType" );
			
			//begin get the contents of the update
			//var msgContents to store the update contents
			def msgContents = null
			String msgUUID = null
			ActivityStreamItem statusUpdateActivity = null

			try{
				statusUpdateActivity = eventStreamService.getActivityStreamItemById( msg.getLong("activityId") );
			}catch(Exception e){
				log.error(':::Unable to get update contents1.1')
				log.error(e.printStackTrace())
				log.error('end stack trace:::')
				msgContents = "This sentance is about New York City by default."
			}

			try{
				msgContents = statusUpdateActivity.content
				msgUUID = statusUpdateActivity.uuid
				System.out.println("::: msgUUID -> ${msgUUID}")
			}catch(Exception e){
				log.error(':::Unable to get update contents2.2')
				log.error(e.printStackTrace())
				log.error('end stack trace:::')
				msgContents = "This sentance is about New York City by default."
			}
			//end get the contents of the update

			//Begin Submitting the message to Stanbol
			// try{
			//	println ":::Submitting to Stanbol -> ${msgContents}"
			//	println ":::UUID to Stanbol -> ${msgUUID}"
			//	def sem = new SemanticEnhancement()
			//	sem.submitData(msgContents,msgUUID)
				//processStanbolOutput(submitToStanbol(msgContents, msg.getLong("activityId")))
			// }catch(Exception e){
				//TODO:cache requests for later processing
				// println "::::>Unable to query Stanbol"
				// e.printStackTrace()
				// log.error(":::>Unable to query Stanbol")
				// log.error(e)
				// log.error("<r:::")
			// }
			//End Submitting the message to Stanbol
				
			
    		if( msgType == null || msgType.isEmpty())
			{
				println( "No msgType received!" );
				return;
			}
			
			
			if( msgType.equals( "REINDEX_ALL" ))
			{
				rebuildAllIndexes();
			}
			else if( msgType.equals( "REINDEX_PERSON" ))
			{
				rebuildPersonIndex();
			}
			else if( msgType.equals( "REINDEX_GENERAL" ))
			{
				rebuildGeneralIndex();
			}
			else if( msgType.equals( "NEW_STATUS_UPDATE" )) // TODO: rename all this to STREAM_POST or something.
    		{
		    	// add document to index
				System.out.println("NEWSTATUSUPDATE:::")
		    	log.info( "adding document to index: ${mapMessage.getString('activityUuid')}" );				
				newStatusUpdate( msg );

    		}
			else if( msgType.equals( "NEW_CALENDAR_FEED_ITEM" ))
    		{
		    	log.info( "adding document to index" );
				newCalendarFeedItem( msg );
    		}
			else if( msgType.equals( "NEW_BUSINESS_EVENT_SUBSCRIPTION_ITEM" ))
			{
				log.info( "adding document to index" );
				newBusinessEventSubscriptionItem( msg );
			}
			else if( msgType.equals( "NEW_ACTIVITY_STREAM_ITEM" ))
			{
				log.info( "adding document to index" );
				newActivityStreamItem( msg );
			}
			else if( msgType.equals( "NEW_RSS_FEED_ITEM" ))
			{
				log.info( "adding document to index" );
				newRssFeedItem( msg );
			}
			else if( msgType.equals( "NEW_QUESTION" ))
    		{
		    	// add document to index
		    	log.debug( "adding document to index" );
				newQuestion( msg );
				
    		}
			else if( msgType.equals( "NEW_STREAM_ENTRY_COMMENT" ))
    		{
    			
		    	println( "adding StreamEntryComment to index" );

				newStreamEntryComment( msg );
    		}
			else if( msgType.equals( "NEW_USER" ) )
			{
				log.debug( "adding new User to Person index" );
				newUser( msg );
			}
			else if( msgType.equals( "NEW_ACTIVITI_USER_TASK" ))
			{
					newActivitiUserTask( msg );
			}
			else 
    		{
    			println( "Bad message type: ${msgType}" );
    		}
    	}
    }

	
	private void newActivitiUserTask( def msg )
	{	
		// TODO: implement call to do content extraction and Lucene indexing...
		// extractAndIndexContent( genericActivityStreamItem.streamObject );
		
		
		ActivityStreamItem activityStreamItem = eventStreamService.getActivityStreamItemById( msg.getLong("activityId") );
		ActivitiUserTask userTask = ActivitiUserTask.findById( activityStreamItem.streamObject.id );
		
		
		// make the call out to Stanbol for semantic enhancement(s)
		if( null != userTask )
		{
			doSemanticEnhancement( userTask );
		}
			
	}
	
	private void newStatusUpdate( def msg )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "got indexDirLocation as: ${indexDirLocation}");
		
		if( indexDirLocation == null )
		{
			String quoddyHome = System.getProperty( "quoddy.home" );
			indexDirLocation = quoddyHome + "/index";
		}
		
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		IndexWriter writer = null;
		
		// TODO: fix this so it will eventually give up, to deal with the pathological case
		// where we never do get the required lock.
		int count = 0;
		println( "Trying to acquire IndexWriter");
		while( writer == null )
		{
			count++;
			if( count > 3 ) {
				println( "tried to obtain Lucene lock 3 times, giving up..." );
				return;
			}
			try
			{
				writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
			}
			catch( org.apache.lucene.store.LockObtainFailedException lfe )
			{
				Thread.sleep( 1200 );
			}
		}
		
		println( "opened Writer" );
		
		ActivityStreamItem statusUpdateActivity = null;
		try
		{
			statusUpdateActivity = eventStreamService.getActivityStreamItemById( msg.getLong("activityId") );
			StatusUpdate statusUpdate = statusUpdateActivity.streamObject;
			
			// println( "Trying to add Document to index" );
			
			writer.setUseCompoundFile(true);

			Document doc = new Document();
		
			doc.add( new Field( "docType", "docType.statusUpdate", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
			
			doc.add( new Field( "objectUuid", statusUpdate.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "objectId", Long.toString( statusUpdate.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			
			doc.add( new Field( "activityUuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityId", Long.toString( msg.getLong("activityId")), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		
			doc.add( new Field( "content", statusUpdateActivity.content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
		
				
			writer.addDocument( doc );
			writer.optimize();
			// println( "Updated Lucene Index for new StatusUpdate" );
		}
		finally
		{
				
			try
			{
				if( writer != null )
				{
					writer.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
			
			try
			{
				if( indexDir != null )
				{
					indexDir.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
		}

		
		/* TEMPORARY CODE, DELETE AFTER USE */
		String enhancementJSON = statusUpdateActivity.streamObject.enhancementJSON;
		// create an empty Model
		Model tempModel = ModelFactory.createDefaultModel();
		
		StringReader reader = new StringReader( enhancementJSON );
	
		RDFDataMgr.read(tempModel, reader, "http://www.example.com", JenaJSONLD.JSONLD);
		

		// Make a TDB-backed dataset
		String quoddyHome = System.getProperty( "quoddy.home" );
		String directory = "${quoddyHome}/jenastore/triples" ;
		println "Opening TDB triplestore at: ${directory}";
		Dataset dataset = TDBFactory.createDataset(directory) ;
		try
		{
			dataset.begin(ReadWrite.WRITE);
			// Get model inside the transaction
			Model model = dataset.getDefaultModel() ;
		
			// find all the "entity" entries in our graph and then associate each
			// one with our "DocumentID"
			ResIterator iter = tempModel.listSubjectsWithProperty( RDF.type, OWL.Thing );
		
			while( iter.hasNext() )
			{
				Resource anEntity = iter.nextResource();
		
				// do we have the "type" (rdf:type) triples that we need for "anEntity"
			
			
				println "adding resource \"quoddy:${statusUpdateActivity.uuid}\" dcterm:references entity: ${anEntity.toString()}";
			
				Resource newResource = model.createResource( "quoddy:${statusUpdateActivity.uuid}" );
				newResource.addProperty( DCTerms.references, anEntity);

			}

			// now add all the triples from the Stanbol response to our canonical Model
			model.add( tempModel );
				
			dataset.commit();
		}
		catch( Exception e )
		{
			dataset.abort();	
		}
		finally
		{
			dataset.end();
		}
		
		
		
		
		// TODO: extract all of the above into a method call
		// extractAndIndexContent( genericActivityStreamItem.streamObject );
		
		// make the call out to Stanbol for semantic enhancement(s)
		// if( null != genericActivityStreamItem.streamObject )
		// {
		//	doSemanticEnhancement();
		// }
		
		
		println( "done with onMessage() call" );
	}
	
	
	/* CalendarFeedItem */
	private void newCalendarFeedItem( def msg )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		IndexWriter writer = null;
		
		// TODO: fix this so it will eventually give up, to deal with the pathological case
		// where we never do get the required lock.
		int count = 0;
		println( "Trying to acquire IndexWriter");
		while( writer == null )
		{
			count++;
			if( count > 3 ) {
				println( "tried to obtain Lucene lock 3 times, giving up..." );
				return;
			}
			try
			{
				writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
			}
			catch( org.apache.lucene.store.LockObtainFailedException lfe )
			{
				Thread.sleep( 1200 );
			}
		}
		
		println( "opened Writer" );
		
		try
		{
			ActivityStreamItem calendarFeedItemActivity = eventStreamService.getActivityStreamItemById( msg.getLong("activityId") );
			CalendarFeedItem calendarFeedItem = calendarFeedItemActivity.streamObject;
			
			println( "Trying to add Document to index" );
			
			writer.setUseCompoundFile(true);

			Document doc = new Document();
			
			doc.add( new Field( "docType", "docType.calendarFeedItem", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
				
			doc.add( new Field( "objectUuid", calendarFeedItem.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "objectId", Long.toString( calendarFeedItem.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			
			doc.add( new Field( "activityUuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityId", Long.toString( msg.getLong("activityId")), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		
			// extract content from the item and add to appropriate fields
			doc.add( new Field( "startDate", DateTools.dateToString(calendarFeedItem.startDate, DateTools.Resolution.MINUTE ), Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ) );
			doc.add( new Field( "endDate", DateTools.dateToString(calendarFeedItem.endDate, DateTools.Resolution.MINUTE ), Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ) );
			doc.add( new Field( "dateEventCreated", DateTools.dateToString(calendarFeedItem.dateEventCreated, DateTools.Resolution.MINUTE ), Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ) );
			doc.add( new Field( "status", calendarFeedItem.status, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			doc.add( new Field( "summary", calendarFeedItem.summary, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			doc.add( new Field( "description", calendarFeedItem.description, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			doc.add( new Field( "location", calendarFeedItem.location, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			
			writer.addDocument( doc );
			writer.optimize();
			
			println( "Updated Lucene Index for new CalendarFeedItem" );			

		}
		finally
		{
				
			try
			{
				if( writer != null )
				{
					writer.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
			
			try
			{
				if( indexDir != null )
				{
					indexDir.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
		}

		
		// TODO: extract all of the above into a method call
		// extractAndIndexContent( genericActivityStreamItem.streamObject );
		
		// make the call out to Stanbol for semantic enhancement(s)
		// if( null != genericActivityStreamItem.streamObject )
		// {
		//	doSemanticEnhancement();
		// }
		
		println( "done with onMessage() call" );
	}
	
	
	/* BusinessEventSubscriptionItem */
	/* Question: Should we even store this stuff in the Lucene
	 * index at all?  We already have the XML stored in the xmlDb, should
	 * the searchService just be "smart" and query that? For now, let's assume
	 * that we create a synthetic "summary" or "description" entry from business events
	 * and index that.  When we go "advanced search" on Business Events we can go
	 * straight to the xmlDb.
	 */
	private void newBusinessEventSubscriptionItem( def msg )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		IndexWriter writer = null;
		
		// TODO: fix this so it will eventually give up, to deal with the pathological case
		// where we never do get the required lock.
		int count = 0;
		println( "Trying to acquire IndexWriter");
		while( writer == null )
		{
			count++;
			if( count > 3 ) {
				println( "tried to obtain Lucene lock 3 times, giving up..." );
				return;
			}
			try
			{
				writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
			}
			catch( org.apache.lucene.store.LockObtainFailedException lfe )
			{
				Thread.sleep( 1200 );
			}
		}
		
		println( "opened Writer" );
		
		ActivityStreamItem besItemActivity = null;
		try
		{
			besItemActivity = eventStreamService.getActivityStreamItemById( msg.getLong("activityId") );
			BusinessEventSubscriptionItem besItem = besItemActivity.streamObject;
			besItem = existDBService.populateSubscriptionEventWithXmlDoc( besItem );
			// println( "Trying to add Document to index" );
			
			writer.setUseCompoundFile(true);

			Document doc = new Document();
		
			doc.add( new Field( "docType", "docType.businessEventSubscriptionItem", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
			doc.add( new Field( "summary", besItem.summary, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			
			doc.add( new Field( "objectUuid", besItem.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "objectId", Long.toString( besItem.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			
			doc.add( new Field( "activityUuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityId", Long.toString( msg.getLong("activityId")), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			
			// TODO: figure out how to convert from any one of a bazillion different possible XML messages, to something
			// we can index.  
			if( besItem.xmlDoc != null )
			{
				String xmlString = nodeToString( besItem.xmlDoc );
				doc.add( new Field( "content", xmlString, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			}
			else
			{
				println( "WARNING: NO XML DOC AVAILABLE IN BES_ITEM" );	
			}
			
			writer.addDocument( doc );
			writer.optimize();
			// println( "Updated Lucene Index for new StatusUpdate" );
		}
		finally
		{
				
			try
			{
				if( writer != null )
				{
					writer.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
			
			try
			{
				if( indexDir != null )
				{
					indexDir.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
		}

		
		// TODO: extract all of the above into a method call
		// extractAndIndexContent( besItem );
		
		// make the call out to Stanbol for semantic enhancement(s)
		if( null != besItemActivity.streamObject )
		{
			doSemanticEnhancement( besItemActivity.streamObject );
		}
		
		
		println( "done with onMessage() call" );
	}
	
	
	/* ActivityStreamItem */
	private void newActivityStreamItem( def msg )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		IndexWriter writer = null;
		
		// TODO: fix this so it will eventually give up, to deal with the pathological case
		// where we never do get the required lock.
		int count = 0;
		println( "Trying to acquire IndexWriter");
		while( writer == null )
		{
			count++;
			if( count > 3 ) {
				println( "tried to obtain Lucene lock 3 times, giving up..." );
				return;
			}
			try
			{
				writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
			}
			catch( org.apache.lucene.store.LockObtainFailedException lfe )
			{
				Thread.sleep( 1200 );
			}
		}
		
		println( "opened Writer" );
		
		ActivityStreamItem genericActivityStreamItem = null;
		
		try
		{
			genericActivityStreamItem = eventStreamService.getActivityStreamItemById( msg.getLong("activityId") );

			// println( "Trying to add Document to index" );
			
			writer.setUseCompoundFile(true);

			Document doc = new Document();
		
			doc.add( new Field( "docType", "docType.activityStreamItem", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
			
			doc.add( new Field( "activityUuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityId", Long.toString( msg.getLong("activityId") ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			
			/* 
			 * TODO: this is wrong.  Or it should be.  We *should* have a streamObject even for "external" activitystrea.ms 
			 * objects.
			 */ 
			doc.add( new Field( "objectUuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "objectId", Long.toString( msg.getLong("activityId") ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			
			doc.add( new Field( "content", genericActivityStreamItem.content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			
			writer.addDocument( doc );
			writer.optimize();
			// println( "Updated Lucene Index for new StatusUpdate" );
			
		}
		finally
		{
				
			try
			{
				if( writer != null )
				{
					writer.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
			
			try
			{
				if( indexDir != null )
				{
					indexDir.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
		}

		
		// TODO: extract all of the above into a method call
		// extractAndIndexContent( genericActivityStreamItem.streamObject );
		
		// make the call out to Stanbol for semantic enhancement(s)
		// if( null != genericActivityStreamItem.streamObject )
		// {
		//	doSemanticEnhancement();
		// }
		
				
		println( "done with onMessage() call" );
	}
	
	
	
	/* RssFeedItem */
	private void newRssFeedItem( def msg )
	{
		println "newRssFeedItem indexing new item...";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		IndexWriter writer = null;
		
		// this will eventually give up, to deal with the pathological case
		// where we never do get the required lock.
		int count = 0;
		println( "Trying to acquire IndexWriter");
		while( writer == null )
		{
			count++;
			if( count > 3 ) {
				println( "tried to obtain Lucene lock 3 times, giving up..." );
				return;
			}
			try
			{
				writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
			}
			catch( org.apache.lucene.store.LockObtainFailedException lfe )
			{
				Thread.sleep( 1200 );
			}
		}
		
		println( "opened Writer" );
		
		try
		{
			ActivityStreamItem activityStreamItem = eventStreamService.getActivityStreamItemById( msg.getLong("activityId") );

			println( "Trying to add Document to index" );
			
			RssFeedItem rssFeedItem = activityStreamItem.streamObject;
			
			/* Note:  We *could* archive the content of the Item when we initially encounter it, and
			 * that would save having to re-download the content now.  That could matter if we're
			 * in a re-index scenario after the resource has become unavailable, and now we're
			 * unable to index this item.  OTOH, if it's not available anymore, you can ask if it
			 * *should* be indexed.   Anyway, we'll go with the approach of re-downloading for now...
			 */

			HttpClient client = new DefaultHttpClient();
			
			//establish a connection within 10 seconds
			// client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			HttpGet httpget = new HttpGet(rssFeedItem.linkUrl);
			InputStream httpStream = null;
			try
			{
				HttpResponse httpResponse = client.execute(httpget);
				HttpEntity entity = httpResponse.getEntity();
				
				httpStream = entity.content;
				
			}
			catch ( HttpException he) {
			   log.error("Http error connecting to '" + url + "'");
			   log.error(he.getMessage());
			}
			catch (IOException ioe){
			   // ioe.printStackTrace();
			   log.error("Unable to connect to '" + url + "'");
			   log.error( ioe );
			}
	   
			// extract text with Tika
			String content = "";
			try
			{
				org.xml.sax.ContentHandler textHandler = new BodyContentHandler(-1);
				Metadata metadata = new Metadata();
				Parser parser = new AutoDetectParser();
				parser.parse(httpStream, textHandler, metadata);
				
				content = textHandler.toString()?.replaceAll('\\s+', ' ');
							
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
						
			writer.setUseCompoundFile(true);
			
			Document doc = new Document();

			doc.add( new Field( "docType", "docType.rssFeedItem", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
			
			doc.add( new Field( "activityUuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityId", Long.toString( msg.getLong("activityId") ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			
			/* TODO: this is wrong... use the id/uuid of the streamObject here */
			doc.add( new Field( "objectUuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "objectId", Long.toString( msg.getLong("activityId") ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			
			doc.add( new Field( "content", content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );

			writer.addDocument( doc );
			writer.optimize();
			println( "Updated Lucene Index for new RssFeedItem" );
		
		}
		finally
		{
				
			try
			{
				if( writer != null )
				{
					writer.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
			
			try
			{
				if( indexDir != null )
				{
					indexDir.close();
				}
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
		}

		println( "done with onMessage() call" );
	}
	
	
	
	
	/* Question */
	private void newQuestion( def msg )
	{
	
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		IndexWriter writer = null;
		
		// TODO: fix this so it will eventually give up, to deal with the pathological case
		// where we never do get the required lock.
		while( writer == null )
		{
			try
			{
				writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
			}
			catch( org.apache.lucene.store.LockObtainFailedException lfe )
			{
				Thread.sleep( 1200 );
			}
		}
		
		try
		{
			writer.setUseCompoundFile(true);
	
			Document doc = new Document();
		
			doc.add( new Field( "docType", "docType.question", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
		
			/*	doc.add( new Field( "uuid", msg['uuid'], Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "id", Long.toString( msg['id'] ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "url", msg['url'], Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "title", msg['title'], Field.Store.YES, Field.Index.ANALYZED ) );
			doc.add( new Field( "tags", "", Field.Store.YES, Field.Index.ANALYZED ));
			*/
			
			writer.addDocument( doc );
		
			writer.optimize();
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch( Exception e ) {
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
			
			try
			{
				indexDir.close();
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
				e.printStackTrace();
			}
		}
		
	}
	
	
	private void newStreamEntryComment( def msg )
	{
		
		println "adding NEW_STREAM_ENTRY_COMMENT to index!";
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		IndexWriter writer = null;
		
		// TODO: fix this so it will eventually give up, to deal with the pathological case
		// where we never do get the required lock.
		int luceneLockRetryCount = 0;
		while( writer == null )
		{
			try
			{
				writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
			}
			catch( org.apache.lucene.store.LockObtainFailedException lfe )
			{
				luceneLockRetryCount++;
				if( luceneLockRetryCount > 10 )
				{
					log.error( "Failed to obtain lock for Lucene store", e );
					return;
				}
				else
				{
					Thread.sleep( 1200 );
					continue;
				}
			}
		}
		
		try
		{
			
			writer.setUseCompoundFile(true);
	
			Document doc = new Document();
			
			 
			doc.add( new Field( "docType", "docType.streamEntryComment", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));

					
			doc.add( new Field( "objectUuid", msg.getString('comment_uuid'), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "objectId", Long.toString( msg.getLong('comment_id')), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			
			doc.add( new Field( "activityUuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityId", Long.toString( msg.getLong("activityId")), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			
			doc.add( new Field( "entryUuid", msg.getString('entry_uuid'), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "entryId", Long.toString( msg.getLong('entry_id')), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			
			doc.add( new Field( "content", msg.getString('comment_text'), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			
			
			writer.addDocument( doc );
	
			writer.optimize();
		
			println "Done adding index for NEW_STREAM_ENTRY_COMMENT";
				
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch( Exception e ) {
				// ignore this for now, but add a log message at least
			}
			
			try
			{
				indexDir.close();
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
			}
		}
	}
	
	
	private void newUser( def msg )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/person_index" ) );
		IndexWriter writer = null;

		
		int luceneLockRetryCount = 0;
		while( writer == null )
		{
			try
			{
				writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
			}
			catch( org.apache.lucene.store.LockObtainFailedException lfe )
			{
				luceneLockRetryCount++;
				if( luceneLockRetryCount > 10 )
				{
					log.error( "Failed to obtain lock for Lucene store", e );
					return;
				}
				else
				{
					Thread.sleep( 1200 );
					continue;
				}
			}
		}
		
		try
		{
			
			User user = userService.findUserByUuid( msg['user_uuid']);
			
			writer.setUseCompoundFile(true);
	
			Document doc = new Document();
			
			doc.add( new Field( "fullName", user.getFullName(),
				Field.Store.YES, Field.Index.ANALYZED ) );
			
			
			writer.addDocument( doc );
	
			writer.optimize();
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch( Exception e ) {
				// ignore this for now, but add a log message at least
			}
			
			try
			{
				indexDir.close();
			}
			catch( Exception e )
			{
				// ignore this for now, but add a log message at least
			}
		}
			
	}
	
	
    private void addTag( final String uuid, final String tagName )
    {
    	log.debug( "addTag called with uuid: ${uuid} and tagName: ${tagName}" );
    	
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
    	Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation ) );
    	IndexReader indexReader = IndexReader.open( indexDir, false );
    	
    	Term uuidTerm = new Term( "uuid", uuid );
    	TermDocs termDocs = indexReader.termDocs(uuidTerm);
    	
    	if( termDocs.next() )
    	{
    		int docNum = termDocs.doc();
    		indexReader.deleteDocument( docNum );
    		indexReader.close();
    		
    		IndexWriter writer = null;
			
			// TODO: fix this so it will eventually give up, to deal with the pathological case
			// where we never do get the required lock.
			while( writer == null )
			{
				try
				{
					writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), false, MaxFieldLength.UNLIMITED );
				}
				catch( org.apache.lucene.store.LockObtainFailedException lfe )
				{
					Thread.sleep( 1200 );
				}
			}
			
	   		writer.setUseCompoundFile( true );
			
			try
			{
				/* Entry entry = entryService.findByUuid( uuid );
				Document doc = new Document();
				doc.add( new Field( "docType", "docType.tag", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
				doc.add( new Field( "uuid", entry.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
				doc.add( new Field( "id", Long.toString(entry.id), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
				doc.add( new Field( "url", entry.url, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
				doc.add( new Field( "title", entry.title, Field.Store.YES, Field.Index.ANALYZED ) );
	
				String tagString = "";
				entry.tags.each { tagString += it.name + " " };
				doc.add( new Field( "tags", tagString, Field.Store.YES, Field.Index.ANALYZED ) );
				*/
				
				/* use HttpClient to load the page, then extract the content and index it.
				* We'll assume HTTP only links for now... */
		
								
				log.debug( "adding document to writer" );
				writer.addDocument( doc );		;
	    		writer.optimize();
			}
			finally
			{
				
				try
				{
					if( input != null )
					{
						input.close();
					}
				}
				catch( Exception e )
				{
					// ignore this for now, but add a log message at least
				}
			
				try
				{
					if( client != null )
					{
						log.debug( "calling connectionManager.shutdown()" );
						client.getConnectionManager().shutdown();
					}
				}
				catch( Exception e )
				{
					// ignore this for now, but add a log message at least
				}
				
				
				try
				{
					writer.close();
				}
				catch( Exception e ) {
					// ignore this for now, but add a log message at least
				}
				
				try
				{
					indexDir.close();
				}
				catch( Exception e )
				{
					// ignore this for now, but add a log message at least
				}
			}
    	}
    	else
    	{
    		// no document with that uuid???
    		log.debug( "no document for uuid: ${uuid}" );
    	}
    }
    
	
	private void extractAndIndexContent( BusinessEventSubscriptionItem item )
	{
		
	}

	private void extractAndIndexContent( ActivitiUserTask item )
	{
		
	}

	private void doSemanticEnhancement( BusinessEventSubscriptionItem item )
	{
		// tokenize the XML content and send it to Stanbol for enhancement
	
		// Hit Stanbol to get enrichmentData
		// call Stanbol REST API to get enrichment data
		RESTClient restClient = new RESTClient( "http://localhost:8080" )
	
		// println "content submitted: ${content}";
		def restResponse = restClient.post(	path:'enhancer',
										body: "",
										requestContentType : TEXT );
	
		def restResponseText = restResponse.getData();
		
			
	}

	private void doSemanticEnhancement( ActivitiUserTask userTask )
	{
		// extract variables and content and send it to Stanbol for enhancement
		StringBuilder content = new StringBuilder();
		
		Map<String, String> variables = userTask.variables;
		
		variables.each {
			content.append( it.value );
			content.append( " " );
		}
		
		
		// Hit Stanbol to get enrichmentData
		// call Stanbol REST API to get enrichment data
		RESTClient restClient = new RESTClient( "http://localhost:8080" )
	
		// println "content submitted: ${content}";
		def restResponse = restClient.post(	path:'enhancer',
										body: content,
										requestContentType : TEXT );
	
		def restResponseText = restResponse.getData();
		
		// println "\n************************************\n\n${restResponseText}\n\n****************************************\n";
		
		if( restResponseText != null && !restResponseText.isEmpty())
		{
		
			// create an empty Model
			Model tempModel = ModelFactory.createDefaultModel();
			
			StringReader reader = new StringReader( restResponseText.toString() );
		
			RDFDataMgr.read(tempModel, reader, "http://www.example.com", JenaJSONLD.JSONLD);
			
	
			// Make a TDB-backed dataset
			String quoddyHome = System.getProperty( "quoddy.home" );
			String directory = "${quoddyHome}/jenastore/triples" ;
			println "Opening TDB triplestore at: ${directory}";
			Dataset dataset = TDBFactory.createDataset(directory) ;
			
			dataset.begin(ReadWrite.WRITE);
			// Get model inside the transaction
			Model model = dataset.getDefaultModel() ;
			
			// find all the "entity" entries in our graph and then associate each
			// one with our "DocumentID"
			ResIterator iter = tempModel.listSubjectsWithProperty( RDF.type, OWL.Thing );
			
			while( iter.hasNext() )
			{
				Resource anEntity = iter.nextResource();
			
				// do we have the "type" (rdf:type) triples that we need for "anEntity"
				
				
				println "adding resource \"quoddy:${userTask.uuid}\" dc:references entity: ${anEntity.toString()}";
				
				Resource newResource = model.createResource( "quoddy:${userTask.uuid}" );
				newResource.addProperty( DCTerms.references, anEntity);
	
			}
	
			// now add all the triples from the Stanbol response to our canonical Model
			model.add( tempModel );
					
			dataset.commit();
			
			dataset.end();
		
		}
		else
		{
			println "Can't process JSON -> TDB operation!";
		}
				
	}

			
	/* *****************************/
	/* Index rebuilding methods ****/
	/*******************************/
	
    private void rebuildAllIndexes()
    {
		log.warn( "doing rebuildIndex" );
		searchService.rebuildGeneralIndex();
		searchService.rebuildPersonIndex();	    	
    }
	
	private void rebuildPersonIndex()
	{
		searchService.rebuildPersonIndex();
	}
	
	private void rebuildGeneralIndex()
	{
		searchService.rebuildGeneralIndex();
	}
	
	
	private String nodeToString(org.w3c.dom.Node node) 
	{
		StringWriter sw = new StringWriter();
		try 
		{
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} 
		catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		
		return sw.toString();
	}
}

