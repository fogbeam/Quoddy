package org.fogbeam.quoddy


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
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.NIOFSDirectory
import org.apache.lucene.util.Version
import org.fogbeam.quoddy.search.SearchResult


class SearchController 
{

	def siteConfigService;
	def userService;
	def searchService;
	def jmsService;
	
	def index = 
	{	
		[]		
	}
	
	
	
	def doEverythingSearch =
	{
		
		// search using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching Everything, queryString: ${queryString}";
		
		List<SearchResult> searchResults = searchService.doEverythingSearch( queryString );
		println "found some results: ${searchResults.size()}";
		
		render( view:'everythingSearchResults', model:[searchResults:searchResults]);
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
		
		List<User> users = searchService.doUserSearch();
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
				
		
		
		List<User> users = searchService.doPeopleSearch( queryString );
		println "found some users: ${users.size()}";
		
		render( view:'peopleSearchResults', model:[allUsers:users]);
		
	}
	
	def doIFollowSearch = 
	{
		
		println "Searching IFollow";
		
		User user = session.user;
		

		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching IFollow, queryString: ${queryString}";
				

		List<User> users = searchService.doIFollowSearch( queryString );
		println "found some users: ${users.size()}";
		
		
		render( view:'iFollowSearchResults', model:[allUsers:users]);
		
			
	}
	
	def searchFriends = {
	
	}

	def searchPeople = {
	
	}
		
	def doFriendSearch = {
		println "Searching Friends";	
		
		User user = session.user;


		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		println "searching Users, queryString: ${queryString}";
				
		List<User> users = searchService.doFriendSearch( queryString );
		
		println "found some users: ${users.size()}";
		
		
		println "done"
		render( view:'friendSearchResults', model:[allUsers:users]);
		
			
	}
	
	
	def rebuildAll = {
	
		// send JMS message requesting ALL index rebuild
		def msg = [ msgType:'REINDEX_ALL'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>Person Index Rebuilding...</title></head><body><h1>All Indexes Rebuilding...</h1></body></html>" );
		
	}
	
	def rebuildPersonIndex = {
		
		// TODO: send JMS message requesting PERSON index rebuild
		def msg = [ msgType:'REINDEX_PERSON'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>Person Index Rebuilding...</title></head><body><h1>Person Index Rebuilding...</h1></body></html>" );
		
	}
	
	def rebuildGeneralIndex = {
		
		// TODO: send JMS message requesting GENERAL index rebuild
		def msg = [ msgType:'REINDEX_GENERAL'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>General Index Rebuilding...</title></head><body><h1>General Index Rebuilding...</h1></body></html>" );
		
	}
	
	
}
