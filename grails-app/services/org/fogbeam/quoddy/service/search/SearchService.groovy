package org.fogbeam.quoddy.service.search

import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.index.IndexWriter.MaxFieldLength
import org.apache.lucene.queryParser.MultiFieldQueryParser
import org.apache.lucene.queryParser.QueryParser
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
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.search.SearchResult
import org.fogbeam.quoddy.stream.StreamItemBase;

class SearchService
{
	
	def siteConfigService;
	
	public List<SearchResult> doEverythingSearch( final String queryString )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		println( "got indexDirLocation as: ${indexDirLocation}");
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "general_index" ) );
		
		
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
			String uuid = result.get("uuid");
			SearchResult searchResult = new SearchResult(uuid:uuid, docType:docType);
			
			searchResults.add( searchResult );
		}
		
		
		return searchResults;
	}

	
	public List<User> doUserSearch( final String queryString )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "person_index") );
		
		IndexSearcher searcher = new IndexSearcher( indexDir );
	
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "fullName", new StandardAnalyzer(Version.LUCENE_30));
		Query query = queryParser.parse(queryString);
		
		TopDocs hits = searcher.search(query, 20);
		
		List<User> users = new ArrayList<User>();
		ScoreDoc[] docs = hits.scoreDocs;
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			users.add( userService.findUserByUserId(userId));
		
		}
		
		return users;
	}
	
	
	// do YYY search
	public List<User> doPeopleSearch( final String queryString )
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "person_index") );
		
		IndexSearcher searcher = new IndexSearcher( indexDir );

		BooleanQuery outerQuery = new BooleanQuery();
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "fullName", new StandardAnalyzer(Version.LUCENE_30));
		Query userQuery = queryParser.parse(queryString);
		
		TopDocs hits = searcher.search( userQuery, 20);
		
		List<User> users = new ArrayList<User>();
		ScoreDoc[] docs = hits.scoreDocs;
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			users.add( userService.findUserByUserId(userId));
		
		}
		
		return users;
	}
	
	
	
	// do ZZZ search	
	public List<User> doIFollowSearch( final String queryString )
	{
		// get a list of my friends
		List<User> iFollow = userService.listIFollow( user );
		
		// use the list of iFollow ids as part of the lucene query.  Need to make sure that we
		// specify that the id field must be a match.
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "person_index") );

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
		
		System.out.println( "Query (" + outerQuery.getClass().getName() + "): "  + outerQuery.toString() );
		
		
		TopDocs hits = searcher.search( outerQuery, 20);
		
		List<User> users = new ArrayList<User>();
		ScoreDoc[] docs = hits.scoreDocs;
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			users.add( userService.findUserByUserId(userId));
		
		}
		
		return users;
	}
	
	
	public List<User> doFriendSearch( final String queryString )
	{
		// get a list of my friends
		List<User> myFriends = userService.listFriends( user );
		
		// use the list of friend ids as part of the lucene query.  Need to make sure that we
		// specify that the id field must be a match.
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "person_index") );

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
		
		System.out.println( "Query (" + outerQuery.getClass().getName() + "): "  + outerQuery.toString() );
		
		
		TopDocs hits = searcher.search( outerQuery, 20);
		
		List<User> users = new ArrayList<User>();
		ScoreDoc[] docs = hits.scoreDocs;
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			users.add( userService.findUserByUserId(userId));
		
		}
		
		return users;
	}
	
	public void rebuildGeneralIndex()
	{
		// note: this should probably involve writing out a whole new index, then doing
		// a quick "switch" at the end, so the main index isn't offline (or bogged down) the
		// whole time we are re-indexing
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "general_index") );
		IndexWriter writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.LIMITED);
		writer.setUseCompoundFile(false);
	
					
		// get all Activities...
		List<StreamItemBase> items = eventStreamService.getAllStreamItems();
		
		// iterate the list and store each to the DB
		for( StreamItemBase item : items )
		{
				
		}
		
	}
	
	public void rebuildPersonIndex()
	{
		// build the search index using Lucene
		List<User> users = userService.findAllUsers();
		println "reindexing ${users.size()} users";
		
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		Directory indexDir = new NIOFSDirectory( new java.io.File( indexDirLocation + "person_index") );
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
		log.debug( "indexDirLocation: ${indexDirLocation}" );
		if( indexDirLocation )
		{
			File indexFile = new java.io.File( indexDirLocation + "general_index" );
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
	
	public void initializePersonIndex()
	{
		String indexDirLocation = siteConfigService.getSiteConfigEntry( "indexDirLocation" );
		log.debug( "indexDirLocation: ${indexDirLocation}" );
		if( indexDirLocation )
		{
			File indexFile = new java.io.File( indexDirLocation + "person_index" );
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
		
	
}
