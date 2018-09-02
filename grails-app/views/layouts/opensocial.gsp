<html>
    <head>
        <title>
          <g:layoutTitle default="Quoddy" />
        </title>
        <link rel="stylesheet" type="text/css" href="/css/main.css">
        <link rel="stylesheet" type="text/css" href="/css/reset-min.css">
        <link rel="stylesheet" type="text/css" href="/css/fonts-min.css">             
        <link rel="stylesheet" type="text/css" href="/css/grids-min.css">
            

        <g:javascript>
            window.appContext = '${request.contextPath}';
        </g:javascript>


		<!-- default container look and feel -->
		<!-- 
		<link rel="stylesheet" href="/shindig/css/gadgets.css">
		<script type="text/javascript" src="/shindig/gadgets/js/shindig-container:rpc.js?c=1&debug=1&nocache=1"></script>
		-->
		<!--  		
			<script type="text/javascript" src="/shindig/gadgets/js/container:open-views:opensearch:rpc:xmlutil:pubsub-2.js?c=1&debug=1&container=default"></script>
		-->
		
		<script type="text/javascript" src="/shindig/gadgets/js/container.js?c=1"></script>
		
		<script type="text/javascript">

			function renderGadgets() {


				var elem = document.getElementById("gadget-chrome-x");    
				var gadget = "http://www.labpixies.com/campaigns/todo/todo.xml";
				var container = new shindig.container.Container(); 
				var site = container.newGadgetSite(elem);
				container.navigateGadget(site, gadget, {}, {});
			};
		</script>
                
        <g:layoutHead />
                				
    </head>
    <body onLoad="renderGadgets();" >
    
          <div id="doc3" class="yui-t4">
               <div id="hd">
               
                    <!-- header -->
                    <div style="background-color:cfe3ff;height:65px" >
                         
                         <center>
                              <h1 style="font-size:15pt;">Welcome to Quoddy</h1>
                         </center>
                    </div>
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
                         <g:render template="/sidebar" />
                     
                     </div>
               </div> 
               
               <div id="ft">
                    
                    <!-- footer -->
                    <div>
                         <center>Footer for Quoddy</center>
                    </div>
               </div> 
          </div>            
    </body>	
</html>
