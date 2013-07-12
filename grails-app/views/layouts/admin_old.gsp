<html>
    <head>
        <title>
          <g:layoutTitle default="Quoddy" />
        </title>
        <nav:resources />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'main.css')}" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'bootstrap.min.css')}" />
        <link rel="stylesheet" type="text/css" href="/css/reset-min.css">
        <link rel="stylesheet" type="text/css" href="/css/fonts-min.css">             
        <link rel="stylesheet" type="text/css" href="/css/grids-min.css">
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css" />
       
        
        <g:javascript library="jquery-1.4" />
        <g:javascript library="jquery.timers-1.2" />
        <g:javascript>
          var $j = jQuery.noConflict();
        </g:javascript>
        
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
						<li><h1>Quoddy</h1></li>
    				<li><a href="#">Email</a></li>
    				<li><a href="${createLink(controller:'user', action:'listFriends')}">Friends</a></li>
    				<li><a href="${createLink(controller:'user', action:'listFollowers')}">Followers</a></li>
    				<li><a href="#">Reports</a></li>
    				<li><a href="#">Calendar</a></li>
    				<li><a href="#">Apps</a></li>
    			</ul>
					<div id="gbg" class="settingsNav">
						<a href="${createLink(controller:'user', action:'editProfile')}">Edit Profile</a>
					</div>
				</div>
    	</div>
    	
    	<div id="header">
    	</div>
    	
       	<div id="body" class="yui3-g">
        	
        	<div class="yui3-u-7-12">
        	
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
