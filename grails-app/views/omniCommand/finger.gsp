<html>
	
	<head>
		<title>Quoddy: OmniCommand Results</title>
		<meta name="layout" content="main" />
	     <nav:resources />		
	</head>
	
	<body>
		<div class="span8">
			<div class="hero-unit" style="min-height:130px;margin-top:5px;padding-top:10px;padding-bottom:15px;">
			
				<div style="display: inline-block; margin-left: 20px; margin-top: 5px; height: 70px; width: 150px; vertical-align: top;">
					<label>UserId:</label>
					<span>${user.userId}</span>
				</div>
				
				<div style="display: inline-block; margin-left: 20px; margin-top: 5px; height: 70px; width: 150px; vertical-align: top;">
					<label>Name:</label>
					<span>${user.fullName}</span>
				</div>
				
				<div style="display: inline-block; margin-left: 20px; margin-top: 5px; height: 70px; width: 150px; vertical-align: top;">
					<label>Display Name:</label>
					<span>${user.displayName}</span>
				</div>
				
				<p />
				
				<div style="display: inline-block; margin-left: 20px; margin-top: 12px; height: 70px; width: 150px; vertical-align: top;">
					<label>Summary:</label>
					<span>${user.profile.summary}</span>
				</div>
				
				<div style="display: inline-block; margin-left: 20px; margin-top: 12px; height: 70px; width: 150px; vertical-align: top;">
					<label>Title:</label>
					<span>${user.profile.title}</span>
				</div>
				
				<div style="display: inline-block; margin-left: 20px; margin-top: 12px; height: 70px; width: 150px; vertical-align: top;">
					<label>Primary Phone:</label>
					<span>${user.profile.primaryPhoneNumber.address}</span>
				</div>
				
				<p />
				
				<div style="display: inline-block; margin-left: 20px; margin-top: 12px; height: 70px; width: 210px; vertical-align: top;">
					<label>Primary Email:</label>
					<span>${user.profile.primaryEmailAddress.address}</span>
				</div>
				
				<div style="display: inline-block; margin-left: 20px; margin-top: 12px; height: 70px; width: 210px; vertical-align: top;">
					<label>Primary IM:</label>
					<span>${user.profile.primaryInstantMessenger.address}</span>
				</div>
				
				<div style="display: inline-block; margin-left: 20px; margin-top: 12px; height: 70px; width: 150px; vertical-align: top;">
					<label>.plan</label>
					<span>${user.profile.dotPlan}</span>
				</div>
				
			</div>
			<div style="margin-top:25px;">
				<label>Latest Status</label>
				<g:render template="${user.currentStatus.templateName}" var="item" bean="${user.mostRecentUpdate}" />
			</div>
			
		</div>
		
	</body>
	
</html>