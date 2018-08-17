<html>
    <head>
        <title>
          <g:layoutTitle default="Quoddy:Admin:Import Users" />
        </title>
        <nav:resources />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'main.css')}" />
        <link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'bootstrap.min.css')}" />
        <link rel="stylesheet" type="text/css" href="/css/reset-min.css">
        <link rel="stylesheet" type="text/css" href="/css/fonts-min.css">             
        <link rel="stylesheet" type="text/css" href="/css/grids-min.css">
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css" />
       
        
        <g:javascript>
            window.appContext = '${request.contextPath}';
        </g:javascript>
        
        
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
                				
    </head>
    <body>
    
          <div id="doc3" class="yui-t4">
               <div id="hd">                 

               </div> 
               <div id="bd">
                     <div id="yui-main">
                         <div class="yui-b">
                              
                              <!-- layout main content area -->
                              <g:layoutBody />             
                                    
                         </div>
                     </div>
                     <div class="yui-b">
                     
                         <!-- layout sidebar -->
                         <!--  probably don't need a sidebar here -->
                         <!--   <g:render template="/sidebar" /> -->
                     
                     </div>
               </div> 
               
               <div id="ft">
                    
                    <!-- TODO: replace this with a template gsp -->
                    
                    <!-- footer -->
                    <div>
                    </div>
               </div> 
          </div>            
    </body>	
</html>
