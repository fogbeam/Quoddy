<html>
	
	<head>
		<title>Quoddy: Display Subscription</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div id="bodyContent">	
		
		
		
	        <div class="subnav">
	            <ul class="nav nav-pills">
	
	                <li class="dropdown">
	                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Streams
	                        <b class="caret"></b>
	                    </a>
	                    <ul class="dropdown-menu">
	                        <li><g:link controller="userStreamDefinition" action="index">Manage Streams<i
	                                    class="icon-cog"></i>
	                            </g:link></li>
	
	                        <g:each var="stream" in="${sysDefinedStreams}">
	                            <li><g:link controller="home" action="index"
	                                    params="[streamId:stream.id]">
	                                    ${stream.name}
	                                </g:link></li>
	                        </g:each>
	                        <g:each var="stream" in="${userDefinedStreams}">
	                            <li><g:link controller="home" action="index"
	                                    params="[streamId:stream.id]">
	                                    ${stream.name}
	                                </g:link></li>
	                        </g:each>
	                    </ul>
	                </li>
	
	                <li class="dropdown">
	
	                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Lists<b class="caret"></b></a>
	                    <ul class="dropdown-menu">
	                        <g:link controller="userList" action="index">Manage Lists<i
	                                class="icon-cog"></i>
	                        </g:link>
	                            <g:each var="list" in="${userLists}">
	                                <li><g:link controller="userList" action="display"
	                                        params="[listId:list.id]">
	                                        ${list.name}
	                                    </g:link></li>
	                            </g:each>
	                        </ul>
	                    </li>
	
	                    <li class="dropdown">
	                        <a href="#" data-toggle="dropdown" class="dropdown-toggle">Groups
	                            <b class="caret"></b>
	                        </a>
	                        <ul class="dropdown-menu">
	                            <g:link controller="userGroup" action="index">Manage Groups<i
	                                    class="icon-cog"></i>
	                            </g:link>
	
	                                <g:each var="group" in="${userGroups}">
	                                    <li><g:link controller="userGroup" action="display"
	                                            params="[groupId:group.id]">
	                                            ${group.name}
	                                        </g:link></li>
	                                </g:each>
	                        </ul>
	                    </li>
	
	                    <li class="dropdown">
	                    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Subscriptions<b class="caret"></b></a>
	                    <ul class="dropdown-menu">
	                        <g:link controller="subscription" action="index">Manage Subscriptions<i class="icon-cog"></i></g:link>
	                        <li class="divider"></li>
	                        <g:each var="subscription" in="${businessEventSubscriptions}">
	                            <li><g:link controller="subscription" action="displayBusinessEventSubscription"
	                                    params="[subscriptionId:subscription.id]">
	                                    ${subscription.name}
	                                </g:link></li>
	                        </g:each>
	                        <li class="divider"></li>
	                        <g:each var="subscription" in="${calendarFeedSubscriptions}">
	                            <li><g:link controller="subscription" action="displayCalendarFeedSubscription"
	                                    params="[subscriptionId:subscription.id]">
	                                    ${subscription.name}
	                                </g:link></li>
	                        </g:each>
	                        <li class="divider"></li>
	                        <g:each var="subscription" in="${activitiUserTaskSubscriptions}">
	                            <li><g:link controller="subscription" action="displayActivitiUserTaskSubscription"
	                                    params="[subscriptionId:subscription.id]">
	                                    ${subscription.name}
	                                </g:link></li>
	                        </g:each>                           
	                        <li class="divider"></li>
	                        <g:each var="subscription" in="${rssFeedSubscriptions}">
	                            <li><g:link controller="subscription" action="displayRssFeedSubscription"
	                                    params="[subscriptionId:subscription.id]">
	                                    ${subscription.name}
	                                </g:link></li>
	                        </g:each>                           
	                    </ul>
	                </li>
	            </ul>
	      </div>
		
		  <g:if test="${activities != null}">
		      <div id="activityStream">
			     <g:render template="/activityStream" />
			 </div>
		  </g:if>

		</div>
	</body>
	
</html>