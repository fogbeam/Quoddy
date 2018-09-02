package org.fogbeam.quoddy


import org.fogbeam.quoddy.search.SearchResult

import grails.plugin.springsecurity.annotation.Secured


class SearchController 
{

	def siteConfigService;
	def userService;
	def searchService;
	def jmsService;
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def index() 
	{	
		[]		
	}
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def doSearch()
	{
		User currentUser = userService.getLoggedInUser();	
		
		log.info( "Searching using queryString: ${params.queryString}");
		String queryString = params.queryString;

		boolean bSearchStatusUpdates = true;
		boolean bSearchCalendarFeedItems = true;
		boolean bSearchBusSubItems = true;
		boolean bSearchActivitiUserTasks = true;
		boolean bSearchRssFeedItems = true;
		boolean bSearchActivityStreamItems = true;
		boolean bSearchUsers = true;
		boolean bSearchFriends = true;
				
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		
		
		if( bSearchStatusUpdates )
		{
			log.debug( "searching status updates");
			List<SearchResult> tempResults = searchService.doStatusUpdateSearch( queryString );
			log.debug( "SearchStatusUpdates returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchCalendarFeedItems )
		{
			log.debug( "searching calendar feed items");
			List<SearchResult> tempResults = searchService.doCalendarFeedItemSearch( queryString );
			log.debug( "SearchCalendarFeedItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchBusSubItems )
		{
			log.debug( "searching business event subscription items");
			List<SearchResult> tempResults = searchService.doBusinessSubscriptionItemSearch( queryString );
			log.debug( "SearchBusSubItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchActivitiUserTasks )
		{
			log.debug( "searching Activiti User Tasks");
			List<SearchResult> tempResults = searchService.doActivitiUserTaskSearch( queryString );
			log.debug("SearchBusSubItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
			
		}
		
		if( bSearchRssFeedItems )
		{
			log.debug( "searching rss feed items");
			List<SearchResult> tempResults = searchService.doRssFeedItemSearch( queryString );
			log.debug( "SearchRssFeedItems returned " + tempResults.size() + " results" );
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchActivityStreamItems )
		{
			log.debug( "searching activity stream items");
			List<SearchResult> tempResults = searchService.doActivityStreamItemSearch( queryString );
			log.debug( "SearchActivityStreamItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size() );
		}
		
		if( bSearchUsers )
		{
			log.debug( "searching users");
			List<SearchResult> tempResults = searchService.doUserSearch( queryString );
			log.debug( "SearchUsers returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchFriends )
		{
			log.debug( "searching friends");
			List<SearchResult> tempResults = searchService.doFriendSearch( queryString, currentUser );
			log.debug( "SearchFriends returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		render( view:'basicSearchResults', model:[searchResults:searchResults]);
	}
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def doAdvancedSearch()
	{
		
		User currentUser = userService.getLoggedInUser();
		
		// search using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		
		log.debug( "doAdvancedSearch, queryString: ${queryString}");
		
		boolean bSearchEverything = false;
		boolean bSearchStatusUpdates = false;
		boolean bSearchCalendarFeedItems = false;
		boolean bSearchBusSubItems = false;
		boolean bSearchActivitiUserTasks = false;
		boolean bSearchRssFeedItems = false;
		boolean bSearchActivityStreamItems = false;		
		boolean bSearchUsers = false;
		boolean bSearchFriends = false;
		
		String searchEverything = params.searchEverything;
		log.debug( "searchEverything: ${searchEverything}");
		bSearchEverything = searchEverything ? true: false;
		
		if( bSearchEverything )
		{
			log.debug( "setting all flags to true");
			bSearchStatusUpdates = true;
			bSearchCalendarFeedItems = true;
			bSearchBusSubItems = true;
			bSearchActivitiUserTasks = true;
			bSearchRssFeedItems = true;
			bSearchActivityStreamItems = true;
			bSearchUsers = true;
			bSearchFriends = true;
		}
		else
		{	
			String searchStatusUpdates = params.searchStatusUpdates;
			bSearchStatusUpdates = searchStatusUpdates ? true: false;
			log.debug( "searchStatusUpdates: ${searchStatusUpdates}");
		
			String searchCalendarFeedItems = params.searchCalendarFeedItems;
			bSearchCalendarFeedItems = searchCalendarFeedItems ? true : false;
			log.debug( "searchCalendarFeedItems: ${searchCalendarFeedItems}");
		
			String searchBusSubItems = params.searchBusSubItems;
			bSearchBusSubItems = searchBusSubItems ? true : false;
			log.debug( "searchBusSubItems: ${searchBusSubItems}");
					
			String searchActivitiUserTasks = params.searchActivitiUserTasks;
			bSearchActivitiUserTasks = searchActivitiUserTasks ? true : false;
			log.debug( "searchActivitiUserTasks: ${searchActivitiUserTasks}");
			
			String searchRssFeedItems = params.searchRssFeedItems;
			bSearchRssFeedItems = searchRssFeedItems ? true : false;
			log.debug( "searchRssFeedItems: ${searchRssFeedItems}");
		
			String searchActivityStreamItems = params.searchActivityStreamItems;
			bSearchActivityStreamItems = searchActivityStreamItems ? true : false;
			log.debug( "searchActivityStreamItems: ${searchActivityStreamItems}" );
			
			String searchUsers = params.searchUsers;
			bSearchUsers = searchUsers ? true : false;
			log.debug( "searchUsers: ${searchUsers}" );
			
			String searchFriends = params.searchFriends;
			bSearchFriends = searchFriends ? true : false;
			log.debug( "searchFriends: ${searchFriends}" );
		
		}
		
		
		List<SearchResult> searchResults = null;
		
//		if( bSearchEverything )
//		{
//			searchResults = searchService.doEverythingSearch( queryString );
//		}
//		else 
//		{
		
		searchResults = new ArrayList<SearchResult>();	
		if( bSearchStatusUpdates )
		{
			log.debug( "searching status updates" );
			List<SearchResult> tempResults = searchService.doStatusUpdateSearch( queryString );
			log.debug( "SearchStatusUpdates returned " + tempResults.size() + " results" );
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchCalendarFeedItems )
		{
			log.debug( "searching calendar feed items");
			List<SearchResult> tempResults = searchService.doCalendarFeedItemSearch( queryString );
			log.debug( "SearchCalendarFeedItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchBusSubItems )
		{
			log.debug( "searching business event subscription items");
			List<SearchResult> tempResults = searchService.doBusinessSubscriptionItemSearch( queryString );
			log.debug( "SearchBusSubItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchActivitiUserTasks )
		{
			log.debug( "searching Activiti User Tasks");
			List<SearchResult> tempResults = searchService.doActivitiUserTaskSearch( queryString );
			log.debug( "SearchBusSubItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
			
		}
		
		if( bSearchRssFeedItems )
		{
			log.debug( "searching rss feed items");
			List<SearchResult> tempResults = searchService.doRssFeedItemSearch( queryString );
			log.debug( "SearchRssFeedItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchActivityStreamItems )
		{
			log.debug( "searching activity stream items" );
			List<SearchResult> tempResults = searchService.doActivityStreamItemSearch( queryString );
			log.debug( "SearchActivityStreamItems returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchUsers )
		{
			log.debug( "searching users");
			List<SearchResult> tempResults = searchService.doUserSearch( queryString );
			log.debug( "SearchUsers returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());
		}
		
		if( bSearchFriends )
		{
			log.debug( "searching friends");
			List<SearchResult> tempResults = searchService.doFriendSearch( queryString, currentUser );
			log.debug( "SearchFriends returned " + tempResults.size() + " results");
			searchResults.addAll( tempResults );
			log.debug( "searchResults.size() = " + searchResults.size());		
		}
	
		
		log.debug( "found some results: ${searchResults.size()}");
		
		render( view:'everythingSearchResults', model:[searchResults:searchResults]);
	}
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def showAdvanced()
	{
		[]	
	}
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def searchUsers()
	{
		// search users using supplied parameters and return the
		// model for rendering...				
		String queryString = params.queryString;
		log.debug( "searching Users, queryString: ${queryString}");
		
		List<SearchResult> results = searchService.doUserSearch();
		log.debug( "found some users: ${results.size()}");
		
		render( view:'userSearchResults', model:[allUsers:results]);
	}

    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def searchIFollow()
	{
		
	}
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def doPeopleSearch()
	{
		
		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		log.debug( "searching People, queryString: ${queryString}");	
		
		List<SearchResult> results = searchService.doPeopleSearch( queryString );
		log.debug( "found some users: ${results.size()}");
		
		render( view:'peopleSearchResults', model:[allUsers:results]);
	}
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def doIFollowSearch()
	{
		log.debug( "Searching IFollow");
		
		User user = userService.getLoggedInUser();
		
		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		log.debug( "searching IFollow, queryString: ${queryString}");
				

		List<SearchResult> results = searchService.doIFollowSearch( queryString );
		log.debug( "found some users: ${results.size()}");
		
		
		render( view:'iFollowSearchResults', model:[allUsers:results]);		
	}
	
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def searchFriends()
    {
	
	}

    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def searchPeople() 
    {
	
	}
		
    @Secured(['ROLE_USER','ROLE_ADMIN'])
	def doFriendSearch()
    {
		log.debug( "Searching Friends");	
		
		User user = userService.getLoggedInUser();

		// search users using supplied parameters and return the
		// model for rendering...
		String queryString = params.queryString;
		log.debug( "searching Users, queryString: ${queryString}");
				
		List<SearchResult> results = searchService.doFriendSearch( queryString );
		
		log.debug( "found some users: ${results.size()}");
		
		render( view:'friendSearchResults', model:[allUsers:results]);		
	}
	
    
    @Secured(['ROLE_ADMIN'])
	def rebuildAll() 
    {
		// send JMS message requesting ALL index rebuild
		def msg = [ msgType:'REINDEX_ALL'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>Person Index Rebuilding...</title></head><body><h1>All Indexes Rebuilding...</h1></body></html>" );	
	}
	
    @Secured(['ROLE_ADMIN'])
	def rebuildPersonIndex()
	{
		
		// TODO: send JMS message requesting PERSON index rebuild
		def msg = [ msgType:'REINDEX_PERSON'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>Person Index Rebuilding...</title></head><body><h1>Person Index Rebuilding...</h1></body></html>" );	
	}
	
    @Secured(['ROLE_ADMIN'])
	def rebuildGeneralIndex()
	{	
		// TODO: send JMS message requesting GENERAL index rebuild
		def msg = [ msgType:'REINDEX_GENERAL'];
		jmsService.send( queue: 'quoddySearchQueue', msg, 'standard', null );
		
		render( "<html><head><title>General Index Rebuilding...</title></head><body><h1>General Index Rebuilding...</h1></body></html>" );	
	}
}
