<html>
    <head>
        <title>
          <g:layoutTitle default="Quoddy" />
        </title>
        <nav:resources />
        <link rel="stylesheet" type="text/css" href="/css/main.css">
        <link rel="stylesheet" type="text/css" href="/css/reset-min.css">
        <link rel="stylesheet" type="text/css" href="/css/fonts-min.css">             
        <link rel="stylesheet" type="text/css" href="/css/grids-min.css">
            

		<!-- default container look and feel -->
		<link rel="stylesheet" href="/css/gadgets.css">
		<script type="text/javascript" src="../gadgets/js/shindig-container:rpc.js?c=1&debug=1&nocache=1"></script>
		<script type="text/javascript">
			var specUrl0 = 'http://www.google.com/ig/modules/horoscope.xml';
			var specUrl1 = 'http://www.labpixies.com/campaigns/todo/todo.xml';

			// This container lays out and renders gadgets itself.

			function renderGadgets() {
  			var gadget0 = shindig.container.createGadget({specUrl: specUrl0});
  			var gadget1 = shindig.container.createGadget({specUrl: specUrl1});

			shindig.container.addGadget(gadget0);
  			shindig.container.addGadget(gadget1);
  			shindig.container.layoutManager.setGadgetChromeIds(
      		['gadget-chrome-x', 'gadget-chrome-y']);
			shindig.container.renderGadget(gadget0);
  			shindig.container.renderGadget(gadget1);
			};
		</script>

        <g:javascript>
               
        </g:javascript>         
        
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
