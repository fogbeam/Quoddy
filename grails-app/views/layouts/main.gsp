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
        <!--  
        <g:javascript library="prototype" /> 
        <g:javascript library="scriptaculous" />
        -->
        <g:javascript library="jquery" plugin="jquery"/>
        <g:javascript library="application" />
        

        <g:javascript>
               
        </g:javascript>         
        
        <g:layoutHead />
                				
    </head>
    <body>
    
          <div id="doc3" class="yui-t4">
               <div id="hd">
                    
                    <!-- TODO: replace this with a template gsp -->
                    
                    <!-- header -->
                    <div style="background-color:cfe3ff;height:65px" >
                         
                         <center>
                              <h1 style="font-size:15pt;">Welcome to Quoddy</h1>
                         </center>
                         
                         <!-- nav -->
                         <div style="position:relative;margin-left:150px;">
                              <ul class="navigation" id="navigation_tabs">
                                   
                                   
                                   <li class="navigation_active navigation_first">
                                        
                                        <g:if test="${channelName == null }">
                                             <a href="/home/">Home</a>
                                        </g:if>
                                        <g:else>
                                             <a href="/home/">Home</a>                                  
                                        </g:else>
                                   </li>
                                   <li class="navigation_active">
                                        <g:if test="${channelName == null }">
                                             <a href="/user/listFriends">Friends</a>
                                        </g:if>
                                        <g:else>
                                             <a href="/user/listFriends">Friends</a>                                  
                                        </g:else>
                                   
                                   </li>
                                   <li class="navigation_active">
                                        <g:if test="${channelName == null }">
                                             <a href="/user/listFollowers">Followers</a>
                                        </g:if>
                                        <g:else>
                                             <a href="/user/listFollowers">Followers</a>                                  
                                        </g:else>
                                   
                                   </li>
                                   <li class="navigation_active">
                                   <g:if test="${channelName == null }">
                                        <a href="/search/showAdvanced">Advanced Search</a>
                                   </g:if>
                                   <g:else>
                                        <a href="/search/showAdvanced">Advanced Search</a>                                  
                                   </g:else>
                                        
                                   </li>
                                   <li class="navigation_active">
                                        <g:if test="${channelName == null }">
                                             <a href="/reports">Reports</a>
                                        </g:if>
                                        <g:else>
                                             <a href="/reports">Reports</a>                                  
                                        </g:else>
                                   
                                   </li>                                   
                                   <li class="navigation_active">
                                        <g:if test="${channelName == null }">
                                             <a href="/opensocial">OpenSocial</a>
                                        </g:if>
                                        <g:else>
                                             <a href="/opensocial">OpenSocial</a>                                  
                                        </g:else>
                                   </li>
                                   <!--
                                   <li class="navigation_active"><a href="/">Saved</a></li>
                                   -->
                                   <li class="navigation_active"><a href="/tag/list">Tags</a></li>
                                   
                                   <!--
                                   <li class="navigation_active"><a href="/">Channels</a></li>
                                   -->
                                   
                                   <li class="navigation_active"><a href="/admin/index">Admin</a></li>
                                   
                                   <li style="float:right;margin-right:100px;">
                                        <g:if test="${session.user}">
                                         <a href="/userHome/index/${session.user.userId}">${session.user.userId}</a>
                                        </g:if>
                                   </li>
                              </ul>

                         </div>

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
                    
                    <!-- TODO: replace this with a template gsp -->
                    
                    <!-- footer -->
                    <div>
                         <center>Footer for Quoddy</center>
                    </div>
               </div> 
          </div>            
    </body>	
</html>
