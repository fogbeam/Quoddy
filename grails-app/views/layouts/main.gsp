<!DOCTYPE html>
<html>
<head>
<title><g:layoutTitle default="Quoddy" /></title>
<nav:resources />
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'main.css')}" />
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'bootstrap.min.css')}" />
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css/FontAwesome/css', file:'font-awesome.css')}">
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'oagis.css')}" />
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'crappy_css_added_by_phil.css')}" />
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'hopscotch.css')}" />


<!--
<link rel="stylesheet" type="text/css"
	href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css" />
-->

<link rel="stylesheet" type="text/css"
 href="${createLinkTo(dir:'js/jquery-ui-1.10.3.custom/css/vader', file:'jquery-ui-1.10.3.custom.css') }" />

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<g:javascript library="jquery-ui-1.10.3.custom/js/jquery-1.9.1" />

<g:javascript>
	var $j = jQuery.noConflict();	
</g:javascript>

<g:javascript library="hopscotch" />
<g:javascript library="quoddy_intro_tour" />
<g:javascript library="activiti_bpm" />


<g:javascript library="jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom" />


<g:javascript library="dropdown" />
<g:javascript>
			 $j('.dropdown-toggle').dropdown();
		</g:javascript>

<g:javascript library="jquery.timers-1.2" />

<script type="text/javascript">
        	<g:render template="/javascript/application.js"/>
</script>

<script type="text/javascript">
$j(document).ready( function() {

	$j('#queryString').bind("keyup keypress", function(e) {
		  var code = e.keyCode || e.which; 
		  if (code  == 13) {               

			var queryString = $j('#queryString').val();
			// alert( queryString );
		    if( queryString.startsWith('@'))
			{
				// alert( 'stop propagation' );
				e.stopPropagation();
				e.preventDefault();
				$j('#searchForm').attr('action', '<g:createLink controller="omniCommand" action="submitCommand" />').submit();
			}
		    else
			{
				
			}
			
		  }
	});

	
	$j("#searchForm").submit(function(e) {
		
		// alert( 'ordinarily the form would submit here' );
		// e.preventDefault();
		// return false;
	});


	$j( '#searchPplIFollowBtn').click( function(e) {
		e.stopPropagation();
		$j('.btn-group.open .dropdown-toggle').dropdown('toggle');
		// alert( "Search people I follow!");
		
		$j( '#searchForm').attr('action', '<g:createLink controller="search" action="doIFollowSearch" />').submit();
		return false;
	});

	$j( '#searchFriendsBtn').click( function(e){
		e.stopPropagation();
		$j('.btn-group.open .dropdown-toggle').dropdown('toggle');
		// alert( "Search friends!");
		
		$j( '#searchForm').attr('action', '<g:createLink controller="search" action="doFriendSearch" />').submit();
		return false;
	});

	
	$j( '#searchPplBtn').click( function(e) {
		e.stopPropagation();
		$j('.btn-group.open .dropdown-toggle').dropdown('toggle');
		// alert( "Search people!");
		
		$j( '#searchForm').attr('action', '<g:createLink controller="search" action="doPeopleSearch" />').submit();
		return false;
	});
	

	$j( '#searchEverythingBtn').click( function(e) {
		e.stopPropagation();
		$j('.btn-group.open .dropdown-toggle').dropdown('toggle');
		// alert( "Search everything!");
		$j( '#searchForm').attr('action', '<g:createLink controller="search" action="doSearch" />').submit();
		return false;
	});

	
	$j( '#sparqlSearchBtn').click( function(e) {
		e.stopPropagation();
		$j('.btn-group.open .dropdown-toggle').dropdown('toggle');
		// alert( "Search everything!");
		$j( '#searchForm').attr('action', '<g:createLink controller="sparql" action="doSearch" />').submit();
		return false;
	});

	
});
</script>


<g:javascript library="application" />

<g:layoutHead />

<script type="text/javascript">
        	<g:render template="/javascript/hashtag.js"/>
</script>
<g:javascript library="enrich_content" />


</head>
<body>
	<!-- begin customizable header -->
	<div id="gbw" class="headerNavContainer navbar-top">
		<div class="container-fluid">
			<div class="headerNav row-fluid">
				<ul class="customNav span5">
					<li>
						<h1>
							<a href="${createLink(controller:'home', action:'index')}">Quoddy</a>
						</h1>
					</li>
					
					
					<shiro:authenticated>
					<!-- 
						<li><a href="#">Email</a></li>
						<li><a href="#">Calendar</a></li>
					 -->
						<li><a href="#">Apps</a></li>
						<li><a href="${createLink(controller:'spaces', action:'index')}">Spaces</a></li>
						<li><a href="#">BPM</a></li>
						<li><a href="#">Answers</a></li>
					</shiro:authenticated>
				</ul>
				<div id="gbg" class="settingsNav navbar">
					<ul class="topLevel">
						<g:if test="${session.user != null}">
						<li>
							<div class="searchBoxContainer">
								<g:form name="searchForm" controller="search" action="doSearch" method="GET">
									<input id="queryString" name="queryString" type="text" class="searchBox" 
									   autocomplete="off" style="margin-left:-200px;width:365px" />
									<!-- <input type="submit" value="Search" id="searchBtn" /> -->
									
									
									<div class="btn-group">
										<!-- " -->
										<button id="foo" name="foo" data-toggle="dropdown"
											class="btn dropdown-toggle btn-small">
											Search <span class="caret"></span>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li><a id="searchPplBtn" name="searchPplBtn"
												href="#">People</a></li>
											<li><a id="searchFriendsBtn" name="searchFriendsBtn"
												href="#">Friends</a></li>
											<li><a id="searchPplIFollowBtn" name="searchPplIFollowBtn"
												href="#">People
													I Follow</a></li>
											<li class="divider"></li>
											<li><a id="searchEverythingBtn" name="searchEverythingBtn" 
											href="#">Everything</a></li>
											<li><a id="sparqlSearchBtn" name="sparqlSearchBtn" href="#">SPARQL</a></li>
										</ul>
									</div>
									
									
									
								</g:form>
							</div>
						</li>
						</g:if>
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">My Account<b class="caret"></b></a>
							<ul class="dropdown-menu">
								
								<shiro:authenticated>
									<li><a
										href="${createLink(controller:'user', action:'listOpenFriendRequests')}">
										Pending Friend Requests</a></li>
																
									<li class="divider"></li>
									
									<li><a
										href="${createLink(controller:'user', action:'editAccount')}">
										Edit Account Info</a></li>
									<li><a
										href="${createLink(controller:'user', action:'editProfile')}">
										Edit Profile</a></li>
								</shiro:authenticated>
								
								<li class="divider"></li>
								<g:if test="${session.enable_self_registration == true}">
									<!-- /user/create -->
									<li><a
										href="${createLink(controller:'user', action:'create')}">Register</a></li>
								</g:if>
								
								<shiro:authenticated>
									<li><a href="${createLink(controller:'login') }">Login</a></li>
								</shiro:authenticated>
								
								<shiro:authenticated>
									<li><a
										href="${createLink(controller:'login', action:'logout')}">Logout</a></li>
								</shiro:authenticated>
							</ul>
						</li>
						
						<!-- Help menu -->
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Help<b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="docs/index.html">Help Contents</a></li>
								<li><a href="#" onclick="hopscotch.startTour(tour);">Interactive Tour</a></li>
								<li><a href="#">Quoddy Admin Guide</a></li>
								<li><a href="#">Quoddy Integration Guide</a></li>
								<li class="divider"></li>
								<li><a href="#" onclick="testSelector();">About Quoddy</a></li>
							</ul>
						</li>
						<!--  end Help menu -->


						<!-- Admin menu -->
						<shiro:authenticated>
							<shiro:hasRole name="admin">
								<li class="dropdown"><a class="dropdown-toggle"
									data-toggle="dropdown" href="#">Admin<b class="caret"></b></a>
									<ul class="dropdown-menu">
										<li><a
											href="${createLink(controller:'admin', action:'index')}">
											Admin Home</a>
										</li>
										<li class="divider"></li>
										<li>
										<a
											href="${createLink(controller:'user', action:'manageUsers')}">
											Manage Users</a>
										</li>
										<li><a href="#">Manage Site Config</a></li>
										<li><a
											href="${createLink(controller:'schedule', action:'index')}">
											Manage Scheduled Jobs</a>
										</li>
										<li><a href="#">More goes here...</a></li>
										<li class="divider"></li>
										<li><a href="#">Whatever...</a></li>
									</ul>
								</li>
							</shiro:hasRole>
						</shiro:authenticated>
						<!--  end Admin menu -->
						
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div id="body" class="container-fluid">

		<!-- left sidenav, global -->
		<div class="row-fluid">
			<div class="span3 leftContentNav">
				<shiro:authenticated>
					<g:render template="/leftSidebar" />
				</shiro:authenticated>
			</div>

			<!-- layout main content area -->
			<g:layoutBody />
		</div>
	</div>
	
	<div id="footer">

		<!-- TODO: replace this with a template gsp -->
		<!-- footer -->
		<div>
			<!-- <center>Footer for Quoddy</center> -->
		</div>
	</div>
</body>
</html>
