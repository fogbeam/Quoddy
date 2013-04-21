package org.fogbeam.quoddy

import javax.jms.MapMessage

import org.apache.lucene.analysis.standard.StandardAnalyzer
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
import org.fogbeam.quoddy.stream.ActivityStreamItem

public class SearchQueueInputService
{
	
	def siteConfigService;
	def eventStreamService;
	
    static expose = ['jms']
    static destination = "quoddySearchQueue"                 
    
    def onMessage(msg)
    { 
    	
    	/* note: what we would ordinarily do where is turn around and copy this message
    	 * to other queue's, topics, etc., or otherwise route it as needed.  But for
    	 * now we just assume we are the "indexer" job.
    	 */
    	
    	log.debug( "GOT MESSAGE: ${msg}" ); 
    
    	if( msg instanceof java.lang.String )
    	{
    		log.info( "Received message: ${msg}" );
    		
    		
    		if( msg.equals( "REINDEX_ALL" ))
    		{
    			rebuildIndex();
    			return;
    		}

    	}
    	else
    	{
			log.info( "Received message: ${msg}" );
			println "Received message: ${msg}";
			MapMessage mapMessage = (MapMessage)msg;
    		String msgType = mapMessage.getString( "msgType" );
    		if( msgType == null || msgType.isEmpty())
			{
				println( "No msgType received!" );
				return;
			}
			
    		if( msgType.equals( "NEW_STATUS_UPDATE" ))
    		{
		    	// add document to index
		    	// log.info( "adding document to index: ${msg.uuid}" );				
				newStatusUpdate( msg );

    		}
			else if( msgType.equals( "NEW_CALENDAR_FEED_ITEM" ))
    		{
		    	log.debug( "adding document to index" );
				newCalendarFeedItem( msg );
    		}
			else if( msgType.equals( "NEW_BUSINESS_SUBSCRIPTION_ITEM" ))
			{
				log.debug( "adding document to index" );
				// newComment( msg );
			}
    		else if( msgType.equals( "NEW_QUESTION" ))
    		{
		    	// add document to index
		    	log.debug( "adding document to index" );
				newQuestion( msg );
				
    		}
			else if( msgType.equals( "NEW_STREAM_ENTRY_COMMENT" ))
    		{
    			
		    	log.debug( "adding document to index" );
				newComment( msg );
    		}
			else 
    		{
    			println( "Bad message type: ${msgType}" );
    		}
    	}
    }

	
	private void newQuestion( def msg )
	{
	
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation ) );
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
		
			doc.add( new Field( "docType", "docType.entry", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
			doc.add( new Field( "uuid", msg['uuid'], Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "id", Long.toString( msg['id'] ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "url", msg['url'], Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "title", msg['title'], Field.Store.YES, Field.Index.ANALYZED ) );
			doc.add( new Field( "tags", "", Field.Store.YES, Field.Index.ANALYZED ));

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
	
	private void newStatusUpdate( def msg )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation ) );
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
			ActivityStreamItem statusUpdateActivity = eventStreamService.getEventById( msg.getLong("activityId") );

			// println( "Trying to add Document to index" );
			
			writer.setUseCompoundFile(true);

			Document doc = new Document();
		
			doc.add( new Field( "docType", "docType.statusUpdate", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
			doc.add( new Field( "uuid", msg.getString("activityUuid"), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "id", Long.toString( msg.getLong("activityId") ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
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

		println( "done with onMessage() call" );
	}
	
	
	private void newComment( def msg )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation ) );
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
		
			doc.add( new Field( "docType", "docType.comment", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
		
			doc.add( new Field( "entry_id", Long.toString( msg['entry_id'] ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "entry_uuid", msg['entry_uuid'], Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		
			doc.add( new Field( "id", Long.toString( msg['comment_id'] ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "uuid", msg['comment_uuid'], Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "content", msg['comment_text'], Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES ) );
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
				doc.add( new Field( "docType", "docType.entry", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
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
    
    private void rebuildIndex()
    {
		log.debug( "doing rebuildIndex" );	    	
    }
}