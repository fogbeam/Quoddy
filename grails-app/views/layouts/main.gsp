<html>
    <head>
        <title>
          <g:layoutTitle default="Quoddy" />
        </title>
        <nav:resources />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'main.css')}" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'bootstrap.min.css')}" />
        <!-- 
        <link rel="stylesheet" type="text/css" href="/css/reset-min.css">
        <link rel="stylesheet" type="text/css" href="/css/fonts-min.css">             
        <link rel="stylesheet" type="text/css" href="/css/grids-min.css">
		-->
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssreset/reset-min.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssbase/base-min.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css" />
		
        <g:javascript library="jquery-1.4" />
        <g:javascript library="jquery.timers-1.2" />
        <!-- <g:javascript library="application" />  -->
        
	    <script type="text/javascript">
        	<g:render template="/javascript/application.js"/>
    	</script>
        
        <g:layoutHead />
                				
    </head>
    <body>
    
    	<div id="gbw" class="a-Eo-T">
    		<div id="gbz">
    			<ul>
    				<li><a href="#">Email</a></li>
    				<li><a href="${createLink(controller:'user', action:'listFriends')}">Friends</a></li>
    				<li><a href="${createLink(controller:'user', action:'listFollowers')}">Followers</a></li>
    				<li><a href="#">Reports</a></li>
    				<li><a href="#">Calendar</a></li>
    				<li><a href="#">Apps</a></li>
    			</ul>
    		</div>
    		<div id="gbg">
    			<a href="${createLink(controller:'user', action:'editProfile')}">Edit Profile</a>
    		</div>
    	</div>
    	
    	<div id="header">
    		<div id="logo"><a href="${createLink(controller:'home', action:'index')}"><img src="${resource(dir:'images',file:'logo2.png')}" width="171" height="53" /></a></div>
    		<div id="icons"></div>
    		<div id="searchbox">
    		     <g:form controller="search" action="doSearch" method="GET">
          			<input name="queryString" type="text" class="searchbox" />
          			<input type="submit" value="Search" id="searchBtn" />
     			</g:form>
    		
    		</div>
    		
    	</div>
    	
       	<div id="body" class="yui3-g">
            
			<div class="yui3-u-5-24">
 
 				<p style="font-weight:bold;float:right;margin-right:45px;">
 					<g:render template="/leftSidebar" />				
 				</p>
 				
        	</div>
        	
        	<div class="yui3-u-7-12">
        	
	    		<!-- layout main content area -->
	    	   	<g:layoutBody /> 
 
        	</div>
    	
    		<div class="yui3-u-5-24">
    		
	    		<p style="font-weight:bold;float:left;margin-left:45px;">
	    			<g:render template="/rightSidebar" />	    		
	    		</p>
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
