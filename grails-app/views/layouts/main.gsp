<!DOCTYPE html>
<html>
<head>
<title>
	<g:layoutTitle default="Quoddy" />
</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'bootstrap.css')}" />
<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'bootstrap-dropdown-multilevel.css')}" />
<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'main.css')}" />
<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'FontAwesome/css/font-awesome.css')}">

<!-- 
<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'oagis.css')}" />
-->

<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'crappy_css_added_by_phil.css')}" />
<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'hopscotch.css')}" />
<!--  for "mega menu" using YAMM3 -->
<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'demo.css')}" />
<link rel="stylesheet" type="text/css"
	href="${resource(dir:'css', file:'yamm.css')}" />


<link rel="stylesheet" type="text/css"
	href="${resource(dir:'javascripts', file:'jquery-ui-1.10.3.custom/css/vader/jquery-ui-1.10.3.custom.css') }" />


<!--
<g:javascript library="jquery-ui-1.10.3.custom/js/jquery-1.9.1" />
-->
<script type="text/javascript"
	src="${resource(dir:'javascripts', file:'jquery-ui-1.10.3.custom/js/jquery-1.9.1.js')}"></script>




<g:javascript>
	var $j = jQuery.noConflict();	
</g:javascript>

<!--
<g:javascript library="hopscotch" />
-->
<script type="text/javascript"
	src="${resource(dir:'javascripts', file:'hopscotch.js')}"></script>

<!-- 
<g:javascript library="quoddy_intro_tour" />
-->
<script type="text/javascript"
	src="${resource(dir:'javascripts', file:'quoddy_intro_tour.js')}"></script>

<!--
<g:javascript library="activiti_bpm" />
-->
<script type="text/javascript"
	src="${resource(dir:'javascripts', file:'activiti_bpm.js')}"></script>



<!--
<g:javascript
	library="jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom" />
-->
<script type="text/javascript"
	src="${resource(dir:'javascripts', file:'jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.js')}"></script>




<script type="text/javascript"
	src="${resource(dir:'javascripts', file:'bootstrap.js')}"></script>

<script type="text/javascript"
	src="${resource(dir:'javascripts', file:'bootstrap-dropdown-multilevel.js')}"></script>

<!--
<g:javascript library="jquery.timers-1.2" />
-->
<script type="text/javascript"
	src="${resource(dir:'javascripts', file:'jquery.timers-1.2.js')}"></script>


<script type="text/javascript">
	<g:render template="/javascript/application.js"/>
</script>

<script type="text/javascript">
	$j(document)
			.ready(
					function() {

						$j('#queryString')
								.bind(
										"keyup keypress",
										function(e) {
											var code = e.keyCode || e.which;
											if (code == 13) {

												var queryString = $j(
														'#queryString').val();
												// alert( queryString );
												if (queryString.startsWith('@')) {
													// alert( 'stop propagation' );
													e.stopPropagation();
													e.preventDefault();
													$j('#searchForm')
															.attr('action',
																	'<g:createLink controller="omniCommand" action="submitCommand" />')
															.submit();
												} else {

												}

											}
										});

						$j("#searchForm").submit(function(e) {

							// alert( 'ordinarily the form would submit here' );
							// e.preventDefault();
							// return false;
						});

						$j('#searchPplIFollowBtn')
								.click(
										function(e) {
											e.stopPropagation();
											$j(
													'.btn-group.open .dropdown-toggle')
													.dropdown('toggle');
											// alert( "Search people I follow!");

											$j('#searchForm')
													.attr('action',
															'<g:createLink controller="search" action="doIFollowSearch" />')
													.submit();
											return false;
										});

						$j('#searchFriendsBtn')
								.click(
										function(e) {
											e.stopPropagation();
											$j(
													'.btn-group.open .dropdown-toggle')
													.dropdown('toggle');
											// alert( "Search friends!");

											$j('#searchForm')
													.attr('action',
															'<g:createLink controller="search" action="doFriendSearch" />')
													.submit();
											return false;
										});

						$j('#searchPplBtn')
								.click(
										function(e) {
											e.stopPropagation();
											$j(
													'.btn-group.open .dropdown-toggle')
													.dropdown('toggle');
											// alert( "Search people!");

											$j('#searchForm')
													.attr('action',
															'<g:createLink controller="search" action="doPeopleSearch" />')
													.submit();
											return false;
										});

						$j('#searchEverythingBtn')
								.click(
										function(e) {
											e.stopPropagation();
											$j(
													'.btn-group.open .dropdown-toggle')
													.dropdown('toggle');
											// alert( "Search everything!");
											$j('#searchForm')
													.attr('action',
															'<g:createLink controller="search" action="doSearch" />')
													.submit();
											return false;
										});

						$j('#sparqlSearchBtn')
								.click(
										function(e) {
											e.stopPropagation();
											$j(
													'.btn-group.open .dropdown-toggle')
													.dropdown('toggle');
											// alert( "Search everything!");
											$j('#searchForm')
													.attr('action',
															'<g:createLink controller="sparql" action="doSearch" />')
													.submit();
											return false;
										});

					});
</script>


<script type="text/javascript"
    src="${resource(dir:'javascripts', file:'enrich_content.js')}"></script>

<script type="text/javascript">
	<g:render template="/javascript/hashtag.js"/>
</script>

<g:layoutHead />


</head>
<body>


	<nav class="navbar yamm navbar-default headerNavContainer">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>

				<!--  TODO: Pull this style out into a new class -->
				<a class="quoddy-brand navbar-brand"
					href="${createLink(controller:'home', action:'index')}">Quoddy</a>

			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">		
					<li>
						<a href="#">Link <span class="sr-only">(current)</span></a>
					</li>
					<li>
						<a href="#">Link</a>
					</li>
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<fogcutter:externalTemplate externalTemplate="megamenu.gsp" />
						</ul>
					</li>			
							
				<li>
				<div class="searchBoxContainer">
				
					<g:form name="searchForm" controller="search" action="doSearch" method="GET" class="navbar-form navbar-left">
						<input id="queryString" name="queryString" type="text" class="searchBox"  autocomplete="off" />
						<div class="btn-group">
							<button id="searchMenuBtn" name="foo" data-toggle="dropdown" class="btn dropdown-toggle btn-small"> Search <span class="caret"></span></button>
							<ul class="dropdown-menu" role="menu">
								<li><a id="searchPplBtn" name="searchPplBtn" href="#">People</a></li>
								<li><a id="searchFriendsBtn" name="searchFriendsBtn" href="#">Friends</a></li>
								<li><a id="searchPplIFollowBtn" name="searchPplIFollowBtn" href="#">People I Follow</a></li>
								<li class="divider"></li>
								<li><a id="searchEverythingBtn" name="searchEverythingBtn" href="#">Everything</a></li>
								<li><a id="sparqlSearchBtn" name="sparqlSearchBtn" href="#">SPARQL</a></li>
							</ul>
						</div>
					</g:form>
	
				</div>
				</li>
				</ul>
				
				<ul class="nav navbar-nav navbar-right">
				
					<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">My Account<b class="caret"></b></a>
						<ul class="dropdown-menu">		
						
							<li><a href="${createLink(controller:'user', action:'listOpenFriendRequests')}">Pending Friend Requests</a></li>
							<li class="divider"></li>
								
							<li><a href="${createLink(controller:'user', action:'editAccount')}">Edit Account Info</a></li>
							<li><a href="${createLink(controller:'user', action:'editProfile')}">Edit Profile</a></li>
							
							<li class="divider"></li>
							<g:if test="${session.enable_self_registration == true}">
								<li><a href="${createLink(controller:'user', action:'create')}">Register</a></li>
							</g:if>
							
							<li><a href="${createLink(controller:'localLogin', action:'logout')}">Logout</a></li>
						</ul>
					</li>
					
					<!-- Help menu -->
					<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">Help<b class="caret"></b></a>
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
					<sec:ifAllGranted roles="ROLE_ADMIN">
						<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">Admin<b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="${createLink(controller:'admin', action:'index')}">Admin Home</a></li>
								<li class="divider"></li>
								<li><a href="${createLink(controller:'user', action:'manageUsers')}">Manage Users</a></li>
								<li><a href="#">Manage Site Config</a></li>
								<li><a href="${createLink(controller:'schedule', action:'index')}">Manage Scheduled Jobs</a></li>
								<li><a href="#">More goes here...</a></li>
								<li class="divider"></li>
								<li><a href="#">Whatever...</a></li>
							</ul>
						</li>
					</sec:ifAllGranted>
					<!--  end Admin menu -->
					
				</ul>
				
			</div>
			<!-- /.navbar-collapse -->
			
		</div>
		<!-- /.container-fluid -->
		
	</nav>

	<!--  main body content -->
	<div id="page-body" role="main" class="container-fluid">

		<div class="row">

			<!--  left sidebar  -->
			<div class="col-md-2">
				<g:render template="/leftSidebar" />
			</div>

			<!--  left sidebar  -->
			<div class="col-md-10">
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
	</div>
</body>
</html>