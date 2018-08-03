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
	href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<g:javascript library="jquery-1.7.1.min" />
<g:javascript>
          var $j = jQuery.noConflict();	
        </g:javascript>

<g:javascript library="dropdown" />
<g:javascript>
			 $j('.dropdown-toggle').dropdown();
		</g:javascript>

<g:javascript library="jquery.timers-1.2" />

<script type="text/javascript">
        	<g:render template="/javascript/application.js"/>
    	</script>

<g:layoutHead />

</head>
<body>
	<!-- begin customizable header -->
	<div id="gbw" class="headerNavContainer navbar-top">
		<div class="container">
			<div class="headerNav row">
				<ul class="customNav span6">
					<li><h1>
							<a href="${createLink(controller:'home', action:'index')}">Quoddy</a>
						</h1></li>
				</ul>
				<div id="gbg" class="settingsNav navbar span6">
					<ul class="topLevel">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">My Account<b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a
									href="${createLink(controller:'user', action:'listOpenFriendRequests')}">Pending
										Friend Requests</a></li>
								<li><a
									href="${createLink(controller:'schedule', action:'index')}">Manage
										Scheduled Jobs</a></li>
								<li><a
									href="${createLink(controller:'calendar', action:'index')}">Manage
										Calendar Feeds</a></li>
								<li class="divider"></li>
								<li><a
									href="${createLink(controller:'user', action:'editAccount')}">Edit
										Account Info</a></li>
								<li><a
									href="${createLink(controller:'user', action:'editProfile')}">Edit
										Profile</a></li>
								<li class="divider"></li>
								<g:if test="${session.enable_self_registration == true}">
									<!-- /user/create -->
									<li><a
										href="${createLink(controller:'user', action:'create')}">Register</a></li>
								</g:if>
								<li><a href="${createLink(controller:'login') }">Login</a></li>
								<g:if test="${session.user != null}">
									<li><a
										href="${createLink(controller:'login', action:'logout')}">Logout</a></li>
								</g:if>
							</ul></li>
					</ul>

				</div>
			</div>
		</div>
	</div>

	<div id="body" class="container">
		<!-- layout main content area -->
		<g:layoutBody />
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
