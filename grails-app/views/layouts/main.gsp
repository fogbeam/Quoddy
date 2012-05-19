<html>
    <head>
        <title>
          <g:layoutTitle default="Quoddy" />
        </title>
        <nav:resources />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'main.css')}" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'bootstrap.min.css')}" />
				<link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css/FontAwesome/css', file:'font-awesome.css')}">        
        
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css" />
       
        
        <g:javascript library="jquery-1.7.1.min" />
        <g:javascript>
				<script type="text/javascript">
          var $j = jQuery.noConflict();	
				</script>
        </g:javascript>
				<g:javascript>
				<script type="text/javascript">
					$j('.dropdown-toggle').dropdown();
				</script>
				</g:javascript>
        <g:javascript library="dropdown" />
        <g:javascript library="jquery.timers-1.2" />
        
	    <script type="text/javascript">
        	<g:render template="/javascript/application.js"/>
    	</script>
        
        <g:layoutHead />
                				
    </head>
    <body>
   
<!-- begin customizable header -->
 
    	<div id="gbw" class="headerNavContainer">
				<div class="headerNav">
    			<ul class="customNav">
						<li><h1><a href="${createLink(controller:'home', action:'index')}">Quoddy</a></h1></li>
    				<li><a href="#">Email</a></li>
    				<li><a href="${createLink(controller:'user', action:'listFriends')}">Friends</a></li>
    				<li><a href="${createLink(controller:'user', action:'listFollowers')}">Followers</a></li>
    				<li><a href="#">Reports</a></li>
    				<li><a href="#">Calendar</a></li>
    				<li><a href="#">Apps</a></li>
    			</ul>
					<div id="gbg" class="settingsNav navbar">
						<ul class="topLevel">
							<li>
								<div class="searchBoxContainer">
									<g:form controller="search" action="doSearch" method="GET">
                   <input name="queryString" type="text" class="searchbox" /> 
                   <!-- <input type="submit" value="Search" id="searchBtn" /> -->
                   <div class="btn-group">
                      <button data-toggle="dropdown" class="btn dropdown-toggle btn-small">Search <span class="caret"></span></button>
                       <ul class="dropdown-menu">
                         <li><a href="${createLink(controller:'search', action:'searchPeople')}">People</a></li>
                         <li><a href="${createLink(controller:'search', action:'searchFriends')}">Friends</a></li>
                         <li><a href="${createLink(controller:'search', action:'searchIFollow')}">People I Follow</a></li>
                         <li class="divider"></li>
                         <li><a href="#">Everything</a></li>
                       </ul>
                   </div>
              </g:form>
             </div>
						</li>
							<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">My Account<b class="caret"></b></a>
								<ul class="dropdown-menu">
									<li><a href="${createLink(controller:'user', action:'listOpenFriendRequests')}">Pending Friend Requests</a></li>
									<li><a href="${createLink(controller:'schedule', action:'index')}">Manage Scheduled Jobs</a></li>
									<li><a href="${createLink(controller:'calendar', action:'index')}">Manage Calendar Feeds</a></li>
									<li class="divider"></li>
									<li><a href="${createLink(controller:'user', action:'editAccount')}">Edit Account Info</a></li>
									<li><a href="${createLink(controller:'user', action:'editProfile')}">Edit Profile</a></li>
									<li class="divider"></li>
										<g:if test="${session.enable_self_registration == true}">
										<!-- /user/create -->
											<li><a href="${createLink(controller:'user', action:'create')}">Register</a></li>
										</g:if>
											<li><a href="${createLink(controller:'login') }">Login</a></li>
										<g:if test="${session.user != null}">
											<li><a href="${createLink(controller:'login', action:'logout')}">Logout</a></li>
										</g:if>
								</ul>
							</li>
						</ul>	

					</div>
				</div>
			</div>
    	
    	<div id="body" class="span12">
			
			<!-- left sidenav, global -->

 				<div class="leftContentNav span3">
 					<g:render template="/leftSidebar" />				
 				</div>
 			
	    		<!-- layout main content area -->
	    	 
				<g:layoutBody /> 
        	</div>
    	
    	</div> <!--  "body" -->
               
       	<div id="footer">
                    
            <!-- TODO: replace this with a template gsp -->
            <!-- footer -->
            <div>
                 <center>Footer for Quoddy</center>
           	</div>
       	</div>      
    </body>	
</html>
