<html>
	
	<head>
		<title>Quoddy: Manage your Streams</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
	<div class="span8 listView">
	<!-- Blank state, should display when there are not any streams
	<div class="hero-unit">
		<h1>Streams</h1>
    <p>Add new streams and manage existing ones here. <a href="#">What are streams?</a></p>
    <p><a class="btn btn-primary btn-large">Create a New Stream</a></p>
  </div>
	-->


	<!-- populated state -->
	<div class="hero-unit">
		<div class="span4">
		<h2>Streams</h2>
		<p>Manage existing streams and create new ones.</p>
		</div>
		<div class="span4 offset5">
		<g:link controller="userStream" action="createWizard" class="btn-large btn btn-primary">Create a New Stream</g:link>
		</div>
	<div class="clear"></div>
	</div>

		<ul>
			<g:each var="stream" in="${sysDefinedStreams}">
				<li><g:link controller="userStream" action="editWizard" params="[streamId:stream.id]" >${stream.name}</g:link> </li>
			</g:each>
				
			<g:each var="stream" in="${userDefinedStreams}">
				<li><g:link controller="userStream" action="editWizard" params="[streamId:stream.id]" >${stream.name}</g:link></li>
			</g:each> 
		</div>
		</ul>
	</div>
	</body>
	
</html>