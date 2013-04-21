package org.fogbeam.quoddy

import org.apache.lucene.analysis.standard.StandardAnalyzer 
import org.apache.lucene.document.Document 
import org.apache.lucene.document.Field 
import org.apache.lucene.index.IndexWriter 
import org.apache.lucene.index.Term 
import org.apache.lucene.index.IndexWriter.MaxFieldLength 
import org.apache.lucene.queryParser.QueryParser 
import org.apache.lucene.search.BooleanQuery 
import org.apache.lucene.search.IndexSearcher 
import org.apache.lucene.search.Query 
import org.apache.lucene.search.ScoreDoc 
import org.apache.lucene.search.TermQuery 
import org.apache.lucene.search.TopDocs 
import org.apache.lucene.search.BooleanClause.Occur 
import org.apache.lucene.store.Directory 
import org.apache.lucene.store.FSDirectory 
import org.apache.lucene.store.NIOFSDirectory 
import org.apache.lucene.util.Version 
import org.fogbeam.quoddy.User;

class SearchController 
{

	def userService;
	
	def index = 
	{
		println "WTF?";
		
		[]		
	}
	
	
	def searchEverything =
	{
		
	}
	
	def showAdvanced =
	{
		[]	
	}
	
	def searchUsers = 
	{
	
		// search users using supplied parameters and return the
		// model for rendering...				
		String queryString = params.queryString;
		println "searching Users, queryString: ${queryString}";
		
		File indexDir = new File( "/development/lucene_indexes/quoddy/person_index" );
		Directory fsDir = FSDirectory.open( indexDir );
		
		IndexSearcher searcher = new IndexSearcher( fsDir );
	
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "fullName", new StandardAnalyzer(Version.LUCENE_30));
		Query query = queryParser.parse(queryString);
		
		TopDocs hits = searcher.search(query, 20);
		
		def users = new ArrayList<User>();
		ScoreDoc[] docs = hits.scoreDocs;
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			users.add( userService.findUserByUserId(userId));
		
		}
		
		println "found some users: ${users.size()}";
		
		render( view:'userSearchResults', model:[allUsers:users]);
	}

	def searchIFollow = 
	{
		
	}
	
	def doPeopleSearch =
	{
		
		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching People, queryString: ${queryString}";
				
		
		File indexDir = new File( "/development/lucene_indexes/quoddy/person_index" );
		Directory fsDir = FSDirectory.open( indexDir );
		
		IndexSearcher searcher = new IndexSearcher( fsDir );

		BooleanQuery outerQuery = new BooleanQuery();
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "fullName", new StandardAnalyzer(Version.LUCENE_30));
		Query userQuery = queryParser.parse(queryString);
		
		TopDocs hits = searcher.search( userQuery, 20);
		
		def users = new ArrayList<User>();
		ScoreDoc[] docs = hits.scoreDocs;
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			users.add( userService.findUserByUserId(userId));
		
		}
		
		println "found some users: ${users.size()}";
		
		
		println "done"
		render( view:'peopleSearchResults', model:[allUsers:users]);
		
	}
	
	def doIFollowSearch = 
	{
		
		println "Searching IFollow";
		
		User user = session.user;
		
		// TODO: verify login...

		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching IFollow, queryString: ${queryString}";
				
		// get a list of my friends
		List<User> iFollow = userService.listIFollow( user );
		
		// use the list of iFollow ids as part of the lucene query.  Need to make sure that we
		// specify that the id field must be a match.
		
		File indexDir = new File( "/development/lucene_indexes/quoddy/person_index" );
		Directory fsDir = FSDirectory.open( indexDir );
		
		IndexSearcher searcher = new IndexSearcher( fsDir );

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
		
		def users = new ArrayList<User>();
		ScoreDoc[] docs = hits.scoreDocs;
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			users.add( userService.findUserByUserId(userId));
		
		}
		
		println "found some users: ${users.size()}";
		
		
		println "done"
		render( view:'iFollowSearchResults', model:[allUsers:users]);
		
			
	}
	
	def searchFriends = {
	
	}

	def searchPeople = {
	
	}
		
	def doFriendSearch = {
		println "Searching Friends";	
		
		User user = session.user;
		
		// TODO: verify login...

		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching Users, queryString: ${queryString}";
				
		// get a list of my friends
		List<User> myFriends = userService.listFriends( user );
		
		// use the list of friend ids as part of the lucene query.  Need to make sure that we
		// specify that the id field must be a match.
		
		File indexDir = new File( "/development/lucene_indexes/quoddy/person_index" );
		Directory fsDir = FSDirectory.open( indexDir );
		
		IndexSearcher searcher = new IndexSearcher( fsDir );

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
		
		def users = new ArrayList<User>();
		ScoreDoc[] docs = hits.scoreDocs;
		for( ScoreDoc doc : docs )
		{
			Document result = searcher.doc( doc.doc );
			String userId = result.get("userId")
			println( userId + " " + result.get("fullName"));
		
			users.add( userService.findUserByUserId(userId));
		
		}
		
		println "found some users: ${users.size()}";
		
		
		println "done"
		render( view:'friendSearchResults', model:[allUsers:users]);
		
			
	}
	
	
	def rebuildIndex = {
		
		// build the search index using Lucene
		List<User> users = userService.findAllUsers();
		println "reindexing ${users.size()} users";
		Directory indexDir = new NIOFSDirectory( new java.io.File( "/development/lucene_indexes/quoddy/person_index" ) );
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
		
		println "done";
		render( "<html><head><title>Index Rebuilt</head><body><h1>Index Rebuilt</h1></body></html>" );
		
	}
}
