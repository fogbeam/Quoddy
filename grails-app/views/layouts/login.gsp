<html>
<head>
<title><g:layoutTitle default="Quoddy" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'bootstrap.css')}" />
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'bootstrap-dropdown-multilevel.css')}" />
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'main.css')}" />
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css/FontAwesome/css', file:'font-awesome.css')}">
<link rel="stylesheet" type="text/css"
	href="${createLinkTo(dir:'css', file:'oagis.css')}" />


    <g:javascript>
        window.appContext = '${request.contextPath}';
    </g:javascript>


<g:javascript library="jquery-1.7.1.min" />
<g:javascript>
          var $j = jQuery.noConflict();	
</g:javascript>


<script
	src=https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js></script>
<script type="text/javascript"
	src="${createLinkTo(dir:'js', file:'bootstrap.js')}"></script>
<script type="text/javascript"
	src="${createLinkTo(dir:'js', file:'bootstrap-dropdown-multilevel.js')}"></script>

<script type="text/javascript">
	<g:render template="/javascript/application.js"/>
</script>

<g:layoutHead />

</head>
<body>
	<!--  navbar -->

	<nav class="navbar navbar-default headerNavContainer">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>

				<!--  TODO: Pull this style out into a new class -->
				<a class="quoddy-brand navbar-brand"
					href="${createLink(controller:'home', action:'index')}">Quoddy</a>

			</div>

		</div>
		<!-- /.container-fluid -->
	</nav>


	<!--  main body content -->
	<div id="page-body" role="main" class="container-fluid">
		<g:layoutBody />
	</div>

</body>
</html>