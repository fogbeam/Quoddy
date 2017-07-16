<html>
<head>
<title>Welcome to Quoddy</title>
<meta name="layout" content="main" />
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


		<g:form name="updateStatusForm" controller="status"
			action="updateStatus">
			<input type="text" id="statusText" name="statusText" class="span6" />
			<input class="btn btn-primary btn-large col-md-2" id="updateStatusSubmit" type="submit" value="Update Status" />
		</g:form>

		<!--  we should have a collection available to render, with the statusupdates for this
            	User, that exist when this page is loaded.  Once we're loaded, we'll update an "available posts"
            	message with AJAX and provide an AJAX powered "update" link that will refresh this view
            	using JQuery's AJAX support. -->
		<div id="messageCount">
			<a href="#" id="refreshMessagesLink">0 messages pending</a>
		</div>

		<g:if test="${activities != null}">

			<div id="activityStream">

				<g:render template="/activityStream" />

			</div>
		</g:if>

	</div>
</body>
</html>

