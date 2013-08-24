package org.fogbeam.quoddy.service.search

import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import org.apache.http.HttpEntity
import org.apache.http.HttpException
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.DateTools
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.index.IndexWriter.MaxFieldLength
import org.apache.lucene.queryParser.MultiFieldQueryParser
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.BooleanClause
import org.apache.lucene.search.BooleanQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.TopDocs
import org.apache.lucene.search.BooleanClause.Occur
import org.apache.lucene.store.Directory
import org.apache.lucene.store.NIOFSDirectory
import org.apache.lucene.util.Version
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.Parser
import org.apache.tika.sax.BodyContentHandler
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.search.SearchResult
import org.fogbeam.quoddy.stream.ActivitiUserTask
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.BusinessEventSubscriptionItem
import org.fogbeam.quoddy.stream.CalendarFeedItem
import org.fogbeam.quoddy.stream.Question
import org.fogbeam.quoddy.stream.RssFeedItem
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.stream.StreamItemBase
import org.fogbeam.quoddy.stream.StreamItemComment

class SearchService
{
	
	def siteConfigService;
	def userService;
	def eventStreamService;
	def existDBService;
	
	public List<SearchResult> doEverythingSearch( final String queryString )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		// println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		// QueryParser queryParser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		String[] fields = ["content", "status", "description", "location", "summary" ];
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser( Version.LUCENE_30, fields, analyzer );
		
		Query query = queryParser.parse(queryString);
		
		TopDocs hits = searcher.search(query, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String docType = result.get("docType")
			String uuid = result.get("activityUuid");
			SearchResult searchResult = new SearchResult(uuid:uuid, docType:docType);
			
			searchResults.add( searchResult );
		}
		
		
		return searchResults;
	}

	public List<SearchResult> doStatusUpdateSearch( final String queryString )
	{
		
		println "in doStatusUpdateSearch";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		// println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		// QueryParser queryParser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		String[] fields = ["content", "status", "description", "location", "summary" ];
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser( Version.LUCENE_30, fields, analyzer );
		
		Query userQuery = queryParser.parse(queryString);
		BooleanQuery query = new BooleanQuery();
		query.add(userQuery, BooleanClause.Occur.MUST );
		
		BooleanQuery docTypeQuery = new BooleanQuery();
		TermQuery docTypeTermStatusUpdate = new TermQuery(new Term("docType","docType.statusUpdate"));
		TermQuery docTypeTermComment = new TermQuery(new Term("docType","docType.streamEntryComment"));
		docTypeQuery.add(docTypeTermStatusUpdate, BooleanClause.Occur.SHOULD );
		docTypeQuery.add(docTypeTermComment, BooleanClause.Occur.SHOULD );
		
		query.add( docTypeQuery, BooleanClause.Occur.MUST );
		
		TopDocs hits = searcher.search(query, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String docType = result.get( "docType" );
			String uuid = result.get("activityUuid");
			
			// lookup our object by it's UUID and assign it to the searchResult instance
			ActivityStreamItem item = eventStreamService.getActivityStreamItemByUuid( uuid );
			SearchResult searchResult = new SearchResult(uuid:uuid, docType:docType, object:item);
				
			searchResults.add( searchResult );
			
		}
		
				
		return searchResults;
	}	
	
	public List<SearchResult> doCalendarFeedItemSearch( final String queryString )
	{
		println "in doCalendarFeedItemSearch";
				
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		// println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		// QueryParser queryParser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		String[] fields = ["content", "status", "description", "location", "summary" ];
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser( Version.LUCENE_30, fields, analyzer );
		
		Query userQuery = queryParser.parse(queryString);
		BooleanQuery query = new BooleanQuery();
		query.add(userQuery, BooleanClause.Occur.MUST );
		TermQuery docTypeTerm = new TermQuery(new Term("docType","docType.calendarFeedItem"));
		query.add( docTypeTerm, BooleanClause.Occur.MUST );
		
		TopDocs hits = searcher.search(query, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String docType = result.get("docType")
			String uuid = result.get("activityUuid");
			// lookup our object by it's UUID and assign it to the searchResult instance
			ActivityStreamItem item = eventStreamService.getActivityStreamItemByUuid( uuid );
			SearchResult searchResult = new SearchResult(uuid:uuid, docType:docType, object:item);
			
			searchResults.add( searchResult );
		}
		
		
		return searchResults;
	}
	
	
	public List<SearchResult> doActivitiUserTaskSearch( final String queryString )
	{
		println "in doActivitiUserTaskSearch";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		// println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		String[] fields = ["content", "status", "description", "location", "summary" ];
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser( Version.LUCENE_30, fields, analyzer );
		
		Query userQuery = queryParser.parse(queryString);
		BooleanQuery query = new BooleanQuery();
		query.add(userQuery, BooleanClause.Occur.MUST );
		TermQuery docTypeTerm = new TermQuery(new Term("docType", "docType.activitiUserTask"));
		query.add( docTypeTerm, BooleanClause.Occur.MUST );
		
		TopDocs hits = searcher.search(query, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String docType = result.get("docType")
			String uuid = result.get("activityUuid");
			// lookup our object by it's UUID and assign it to the searchResult instance
			ActivityStreamItem item = eventStreamService.getActivityStreamItemByUuid( uuid );
			
			SearchResult searchResult = new SearchResult(uuid:uuid, docType:docType, object:item);
			
			searchResults.add( searchResult );
		}
		
		
		return searchResults;

	}
	
	public List<SearchResult> doBusinessSubscriptionItemSearch( final String queryString )
	{
		
		println "in doBusinessSubscriptionItemSearch";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		// println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		// QueryParser queryParser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		String[] fields = ["content", "status", "description", "location", "summary" ];
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser( Version.LUCENE_30, fields, analyzer );
		
		Query userQuery = queryParser.parse(queryString);
		BooleanQuery query = new BooleanQuery();
		query.add(userQuery, BooleanClause.Occur.MUST );
		TermQuery docTypeTerm = new TermQuery(new Term("docType", "docType.businessEventSubscriptionItem"));
		query.add( docTypeTerm, BooleanClause.Occur.MUST );
		
		TopDocs hits = searcher.search(query, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String docType = result.get("docType")
			String uuid = result.get("activityUuid");
			// lookup our object by it's UUID and assign it to the searchResult instance
			ActivityStreamItem item = eventStreamService.getActivityStreamItemByUuid( uuid );
			
			BusinessEventSubscriptionItem besItem = item.streamObject;
			besItem = existDBService.populateSubscriptionEventWithXmlDoc( besItem );
			item.streamObject = besItem;
			
			// need to populate the XMLDoc data...
			
			SearchResult searchResult = new SearchResult(uuid:uuid, docType:docType, object:item);
			
			searchResults.add( searchResult );
		}
		
		
		return searchResults;
	}
	
	
	public List<SearchResult> doRssFeedItemSearch( final String queryString )
	{
		println "in doRssFeedItemSearch";

		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		// println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		// QueryParser queryParser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		String[] fields = ["content", "status", "description", "location", "summary" ];
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser( Version.LUCENE_30, fields, analyzer );
		
		Query userQuery = queryParser.parse(queryString);
		BooleanQuery query = new BooleanQuery();
		query.add(userQuery, BooleanClause.Occur.MUST );
		TermQuery docTypeTerm = new TermQuery(new Term("docType", "docType.rssFeedItem"));
		query.add( docTypeTerm, BooleanClause.Occur.MUST );
		
		TopDocs hits = searcher.search(query, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String docType = result.get("docType")
			String uuid = result.get("activityUuid");
			// lookup our object by it's UUID and assign it to the searchResult instance
			ActivityStreamItem item = eventStreamService.getActivityStreamItemByUuid( uuid );
			SearchResult searchResult = new SearchResult(uuid:uuid, docType:docType, object:item);
			
			searchResults.add( searchResult );
		}
				
		return searchResults;
	}
	
	public List<SearchResult> doActivityStreamItemSearch( final String queryString )
	{
		println "in doActivityStreamItemSearch";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		// println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index" ) );
		
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		// QueryParser queryParser = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30));
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		String[] fields = ["content", "status", "description", "location", "summary" ];
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser( Version.LUCENE_30, fields, analyzer );
		
		Query userQuery = queryParser.parse(queryString);
		BooleanQuery query = new BooleanQuery();
		query.add(userQuery, BooleanClause.Occur.MUST );
		TermQuery docTypeTerm = new TermQuery(new Term("docType", "docType.activityStreamItem"));
		query.add( docTypeTerm, BooleanClause.Occur.MUST );
		
		TopDocs hits = searcher.search(query, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String docType = result.get("docType")
			String uuid = result.get("activityUuid");
			// lookup our object by it's UUID and assign it to the searchResult instance
			ActivityStreamItem item = eventStreamService.getActivityStreamItemByUuid( uuid );
			SearchResult searchResult = new SearchResult(uuid:uuid, docType:docType, object:item);
			
			searchResults.add( searchResult );
		}
		
		
		return searchResults;
	}
	
	
	public List<SearchResult> doUserSearch( final String queryString )
	{
		println "in doUserSearch";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/person_index") );
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "fullName", new StandardAnalyzer(Version.LUCENE_30));
		Query query = queryParser.parse(queryString);
		
		TopDocs hits = searcher.search(query, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
			User userResult = userService.findUserByUserId(userId);
			searchResults.add( new SearchResult(docType:"user", uuid:userResult.uuid, object:userResult) );
		
		}
		
		return searchResults;
	}
	
	public List<SearchResult> doPeopleSearch( final String queryString )
	{
		println "in doPeopleSearch";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/person_index") );
		IndexSearcher searcher = new IndexSearcher( indexDir );

		BooleanQuery outerQuery = new BooleanQuery();
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "fullName", new StandardAnalyzer(Version.LUCENE_30));
		Query userQuery = queryParser.parse(queryString);
		
		TopDocs hits = searcher.search( userQuery, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			User userResult = userService.findUserByUserId(userId);
			searchResults.add( new SearchResult(docType:"user", uuid:userResult.uuid, object:userResult) );
		}
		
		return searchResults;
	}
	
	
		
	public List<SearchResult> doIFollowSearch( final String queryString, final User user )
	{
		println "in doIFollowSearch";
		
		// get a list of my friends
		List<User> iFollow = userService.listIFollow( user );
		
		// use the list of iFollow ids as part of the lucene query.  Need to make sure that we
		// specify that the id field must be a match.
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/person_index") );
		IndexSearcher searcher = new IndexSearcher( indexDir );
		
		BooleanQuery outerQuery = new BooleanQuery();
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "fullName", new StandardAnalyzer(Version.LUCENE_30));
		Query userQuery = queryParser.parse(queryString);
		
		BooleanQuery userIdQuery = new BooleanQuery();
		for( User person : iFollow )
		{
			Term term = new Term( "userId", person.userId );
			TermQuery termQuery = new TermQuery( term );
			userIdQuery.add(termQuery, Occur.SHOULD );
		}
		
		outerQuery.add( userQuery, Occur.MUST );
		outerQuery.add( userIdQuery, Occur.MUST );
		
		// System.out.println( "Query (" + outerQuery.getClass().getName() + "): "  + outerQuery.toString() );
		
		
		TopDocs hits = searcher.search( outerQuery, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			User userResult = userService.findUserByUserId(userId);
			searchResults.add( new SearchResult(docType:"user", uuid:userResult.uuid, object:userResult) );
		}
		
		return searchResults;
	}
	
	
	public List<SearchResult> doFriendSearch( final String queryString, final User user )
	{
		println "in doFriendSearch";
		
		// get a list of my friends
		List<User> myFriends = userService.listFriends( user );
		
		// use the list of friend ids as part of the lucene query.  Need to make sure that we
		// specify that the id field must be a match.
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/person_index") );
		IndexSearcher searcher = new IndexSearcher( indexDir );
		
		BooleanQuery outerQuery = new BooleanQuery();
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "fullName", new StandardAnalyzer(Version.LUCENE_30));
		Query userQuery = queryParser.parse(queryString);
		
		BooleanQuery userIdQuery = new BooleanQuery();
		for( User friend : myFriends )
		{
			Term term = new Term( "userId", friend.userId );
			TermQuery termQuery = new TermQuery( term );
			userIdQuery.add(termQuery, Occur.SHOULD );
		}
		
		outerQuery.add( userQuery, Occur.MUST );
		outerQuery.add( userIdQuery, Occur.MUST );
		
		// System.out.println( "Query (" + outerQuery.getClass().getName() + "): "  + outerQuery.toString() );
		
		
		TopDocs hits = searcher.search( outerQuery, 20);
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		ScoreDoc[] docs = hits.scoreDocs;
		println "Search returned " + docs.length + " results";
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			User userResult = userService.findUserByUserId(userId);
			searchResults.add( new SearchResult(docType:"user", uuid:userResult.uuid, object:userResult) );
		}
		
		return searchResults;
	}
	
	public void rebuildGeneralIndex()
	{
		// note: this should probably involve writing out a whole new index, then doing
		// a quick "switch" at the end, so the main index isn't offline (or bogged down) the
		// whole time we are re-indexing
		String indexDirLocation = null;
		Directory indexDir = null;
		IndexWriter writer = null;
		try
		{
			indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
			
			
			println "rebuildGeneralIndex";
			println "indexDirLocation: ${indexDirLocation}";
			
			indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/general_index") );
			writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.LIMITED);
			writer.setUseCompoundFile(false);
	
					
			// get all Activities...
			List<ActivityStreamItem> items = eventStreamService.getAllActivityStreamItems();
		
			// iterate the list and store each to the DB
			for( ActivityStreamItem item : items )
			{
				println "indexing ASI with id: ${item.id}, uuid: ${item.uuid} and objectClass: ${item.objectClass}";
				// if streamObject is null, the only valid scenario is for this ASI to be a 3rd party (remote)
				// ActivityStreamItem, so the object we're indexing is the ASI itself.
				
				
				Object streamObject = item.streamObject;
				if( streamObject != null )
				{
					streamObject = existDBService.populateSubscriptionEventWithXmlDoc( streamObject );
					item.streamObject = streamObject;	
					
					if( streamObject instanceof BusinessEventSubscriptionItem )
					{
						String xmlString = nodeToString( streamObject.xmlDoc );
						streamObject.summary = xmlString;
					}
									
				}
				
				
				addToIndex( writer, item, ( ( item.streamObject != null ) ? item.streamObject : item ) );		
			}
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
		
		
	}
	
	public void addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final ActivitiUserTask item )
	{
		

		println "adding ActivitiUserTask object to Search Index";
		
		Document doc = new Document();
		
		doc.add( new Field( "docType", "docType.activitiUserTask", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));

		
		doc.add( new Field( "objectUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "objectId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
				
		doc.add( new Field( "content", item.description, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );

		writer.addDocument( doc );
		writer.optimize();
		
		addCommentsToIndex( writer, asi, item );
		
	}
	
	private void addCommentsToIndex( final IndexWriter writer, final ActivityStreamItem asi, final StreamItemBase item )
	{
		
		def comments = item.comments;
		
		if( comments == null )
		{
			return;
		}
		
		for( StreamItemComment comment : comments )
		{
		
			Document doc = new Document();
		
		 
			doc.add( new Field( "docType", "docType.streamEntryComment", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));

				
			doc.add( new Field( "objectUuid", comment.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "objectId", Long.toString( comment.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

		
			doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			doc.add( new Field( "entryUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "entryId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

		
			doc.add( new Field( "content", comment.text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
		
			writer.addDocument( doc );
		}
		
		writer.optimize();
		
	}
	
	
	public void addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final ActivityStreamItem item )
	{
		println "addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final ActivityStreamItem item )";
		
		Document doc = new Document();
		
		doc.add( new Field( "docType", "docType.activityStreamItem", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));

		
		doc.add( new Field( "objectUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "objectId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );		
				
		doc.add( new Field( "content", item.content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			
		writer.addDocument( doc );
		writer.optimize();
		
		addCommentsToIndex( writer, asi, item );
	}

	public void addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final StatusUpdate item )
	{
		println "addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final StatusUpdate item )";
		Document doc = new Document();
		
		doc.add( new Field( "docType", "docType.statusUpdate", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
		
		doc.add( new Field( "objectUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "objectId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );		
		
		doc.add( new Field( "content", item.text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
			
		writer.addDocument( doc );
		writer.optimize();
		
		addCommentsToIndex( writer, asi, item );
	}
	
		
	public void addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final CalendarFeedItem item )
	{
		println "addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final CalendarFeedItem item )";
		
		Document doc = new Document();
		
		doc.add( new Field( "docType", "docType.calendarFeedItem", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
			
			
		doc.add( new Field( "objectUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "objectId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

		// extract content from the item and add to appropriate fields
		doc.add( new Field( "startDate", DateTools.dateToString(item.startDate, DateTools.Resolution.MINUTE ), Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ) );
		doc.add( new Field( "endDate", DateTools.dateToString(item.endDate, DateTools.Resolution.MINUTE ), Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ) );
		doc.add( new Field( "dateEventCreated", DateTools.dateToString(item.dateEventCreated, DateTools.Resolution.MINUTE ), Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ) );
		doc.add( new Field( "status", item.status, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
		doc.add( new Field( "summary", item.summary, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
		doc.add( new Field( "description", item.description, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
		doc.add( new Field( "location", item.location, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
		
		writer.addDocument( doc );
		writer.optimize();
		
		addCommentsToIndex( writer, asi, item );

	}
	
	/* NOTE: question is, do we even want to put this stuff in the
	 * Lucene index at all? Having it together is a convenience, but we can search for
	 * this stuff right out of the eXistDB database, no? 
	 */
	public void addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final BusinessEventSubscriptionItem item )
	{
		
		Document doc = new Document();
	
		doc.add( new Field( "docType", "docType.businessEventSubscriptionItem", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
		
		doc.add( new Field( "objectUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "objectId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

		doc.add( new Field( "summary", item.summary, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
		
		writer.addDocument( doc );
		writer.optimize();
		
		addCommentsToIndex( writer, asi, item );
	}	
	
	public void addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final Question item )
	{
		Document doc = new Document();
		
			doc.add( new Field( "docType", "docType.question", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
			doc.add( new Field( "objectUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "objectId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

			
			//			doc.add( new Field( "url", msg['url'], Field.Store.YES, Field.Index.NOT_ANALYZED ) );
//			doc.add( new Field( "title", msg['title'], Field.Store.YES, Field.Index.ANALYZED ) );
//			doc.add( new Field( "tags", "", Field.Store.YES, Field.Index.ANALYZED ));

			writer.addDocument( doc );
			writer.optimize();
			
			addCommentsToIndex( writer, asi, item );
		
	}

	public void addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final RssFeedItem item )
	{
		Document doc = new Document();
		
		doc.add( new Field( "docType", "docType.rssFeedItem", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
		doc.add( new Field( "objectUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "objectId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

		/* TODO - BIG TODO: get rid of all the duplicated code between this class and the SearchQueueInputService
		 * class.  We should do this indexing stuff in ONE place 
		 **/
		
		
		HttpClient client = new DefaultHttpClient();
		
		//establish a connection within 10 seconds
		// client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		HttpGet httpget = new HttpGet(item.linkUrl);
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

		doc.add( new Field( "content", content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES ) );
		
		writer.addDocument( doc );
		writer.optimize();
		
		addCommentsToIndex( writer, asi, item );
	}
		
	public void addToIndex( final IndexWriter writer, final ActivityStreamItem asi, final StreamItemComment item )
	{
		Document doc = new Document();
		 
		doc.add( new Field( "docType", "docType.streamEntryComment", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO ));
		doc.add( new Field( "objectUuid", item.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "objectId", Long.toString( item.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityUuid", asi.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "activityId", Long.toString( asi.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );

		/*
		doc.add( new Field( "entry_id", Long.toString( item.event.id ), Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		doc.add( new Field( "entry_uuid", item.event.uuid, Field.Store.YES, Field.Index.NOT_ANALYZED ) );
		
		doc.add( new Field( "content", item.text, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES ) );
		*/
		
		writer.addDocument( doc );
		writer.optimize();
		
		addCommentsToIndex( writer, asi, item );
		
	}

	
	public void rebuildPersonIndex()
	{
		// build the search index using Lucene
		List<User> users = userService.findAllUsers();
		println "reindexing ${users.size()} users";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println "rebuildPersonIndex";
		println "indexDirLocation: ${indexDirLocation}";
		
		
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "/person_index") );
		IndexWriter writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.LIMITED);
		writer.setUseCompoundFile(false);
		
		for( User user : users )
		{
			Document doc = new Document();
			
			
			doc.add( new Field( "fullName", user.getFullName(),
						Field.Store.YES, Field.Index.ANALYZED ) );
			
			String bio = user.getBio();
			if( bio == null )
			{
				bio= "NA";
			}
			
			doc.add( new Field( "bio", bio,
					Field.Store.YES, Field.Index.ANALYZED ) );
			
			doc.add( new Field( "userId", user.userId,
					Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			
			doc.add( new Field( "email", user.getEmail(),
					Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			
			String homepage = user.getHomepage();
			if( homepage == null )
			{
				homepage = "NA";
			}
			doc.add( new Field( "homepage", homepage,
					Field.Store.YES, Field.Index.NOT_ANALYZED ) );
			
			writer.addDocument( doc );
		}
		
		writer.optimize();
		writer.close();
	}

	public void initializeGeneralIndex()
	{
	
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "initializeGeneralIndex" );
		println( "indexDirLocation: ${indexDirLocation}" );
		if( indexDirLocation )
		{
			File indexFile = new java.io.File( indexDirLocation + "/general_index" );
			String[] indexFileChildren = indexFile.list();
			boolean indexIsInitialized = (indexFileChildren != null && indexFileChildren.length > 0 );
			if( ! indexIsInitialized )
			{
				println( "Index not previously initialized, creating empty index" );
				/* initialize empty index */
				Directory indexDir = new NIOFSDirectory( indexFile );
				IndexWriter writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.UNLIMITED);
				Document doc = new Document();
				writer.addDocument(doc);
				writer.close();
		   }
		   else
		   {
			   
			   println( "Index already initialized, skipping..." );
		   }
		}
		else
		{
			println( "No indexDirLocation configured!!");
		}
		
	}
	
	public void initializePersonIndex()
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "initializePersonIndex" );
		println( "indexDirLocation: ${indexDirLocation}" );
		if( indexDirLocation )
		{
			File indexFile = new java.io.File( indexDirLocation + "/person_index" );
			String[] indexFileChildren = indexFile.list();
			boolean indexIsInitialized = (indexFileChildren != null && indexFileChildren.length > 0 );
			if( ! indexIsInitialized )
			{
				log.debug( "Index not previously initialized, creating empty index" );
				/* initialize empty index */
				Directory indexDir = new NIOFSDirectory( indexFile );
				IndexWriter writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.UNLIMITED);
				Document doc = new Document();
				writer.addDocument(doc);
				writer.close();
		   }
		   else
		   {
			   
			   log.info( "Index already initialized, skipping..." );
		   }
		}
		else
		{
			log.warn( "No indexDirLocation configured!!");
		}
		
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
