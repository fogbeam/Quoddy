package com.fogbeam.poe

import org.apache.lucene.analysis.standard.StandardAnalyzer 
import org.apache.lucene.document.Document 
import org.apache.lucene.document.Field 
import org.apache.lucene.index.IndexWriter 
import org.apache.lucene.index.IndexWriter.MaxFieldLength 
import org.apache.lucene.queryParser.MultiFieldQueryParser 
import org.apache.lucene.queryParser.QueryParser 
import org.apache.lucene.search.IndexSearcher 
import org.apache.lucene.search.Query 
import org.apache.lucene.search.ScoreDoc 
import org.apache.lucene.search.TopDocs 
import org.apache.lucene.store.Directory 
import org.apache.lucene.store.FSDirectory 
import org.apache.lucene.store.NIOFSDirectory 
import org.apache.lucene.util.Version 

class SearchController {

	def userService;
	
	def index = {
			
	}
	
	
	
	def searchUsers = {
	
		// search users using supplied parameters and return the
		// model for rendering...				
		String queryString = params.queryString;
		println "searching Users, queryString: ${queryString}";
		
		File indexDir = new File( "/development/lucene_indexes/poe/person_index" );
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

	
	def searchFriends = {
	}
	
	def doFriendSearch = {
		println "Searching Friends";	
		
		// get a list of my friends
		
		// use the list of friend ids as part of the lucene query.  Need to make sure that we
		// specify that the id field must be a match.
		
		// search users using supplied parameters and return the
		// model for rendering...				
		String queryString = params.queryString;
		println "searching Users, queryString: ${queryString}";
		
		File indexDir = new File( "/development/lucene_indexes/poe/person_index" );
		Directory fsDir = FSDirectory.open( indexDir );
		
		IndexSearcher searcher = new IndexSearcher( fsDir );
	
		String[] searchFields = ['fullName'];
		QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_30, searchFields, new StandardAnalyzer(Version.LUCENE_30));
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
		
		
		println "done"
		render( view:'friendSearchResults', model:[allUsers:users]);
		
			
	}
	
	
	def rebuildIndex = {
		
		// build the search index using Lucene
		List<User> users = userService.findAllUsers();
		
		Directory indexDir = new NIOFSDirectory( new java.io.File( "/development/lucene_indexes/poe/person_index" ) );
		IndexWriter writer = new IndexWriter( indexDir, new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.LIMITED);
		writer.setUseCompoundFile(false);		
		
		for( User user : users )
		{	
			Document doc = new Document();
			
			
			doc.add( new Field( "fullName", user.getFullName(), 
						Field.Store.YES, Field.Index.ANALYZED ) );
			
			doc.add( new Field( "bio", user.getBio(), 
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
}
