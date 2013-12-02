<html>
    <head>
		<title>Welcome to Quoddy</title>
       	<meta name="layout" content="basic" />
       	       	
    </head>
	<body>
		<div class="row">
			<div id="bodyContent" class="span8" style="min-height:300px;">	
			
				<!-- 
				
				[id:reason, name:Reason, value:null, type:string, required:false, readable:true, writable:true]
				[id:count, name:Count, value:0, type:long, required:false, readable:true, writable:true]
				 -->
			
				<h4 style="margin-bottom:20px;">Complete Task</h4>
				<g:form controller="activitiBPM" action="completeTask" method="POST">
					
					<input type="hidden" value="${taskUuid}" name="taskUuid" id="taskUuid" ></input>
					
					<ul style="list-style-type: none;">
						<g:each in="${taskForm}" var="formVar">
							<li><label style="float:left;width:50px; margin-right:25px;" for="${formVar.id}">${formVar.name}</label>	
							<input type="text" name="activitiForm.${formVar.id}" id="activitiForm.${formVar.id}" value="${formVar.value}" ></input> </li>
						</g:each>
					</ul>
					<g:submitButton class="btn-primary" name="Complete"/>
				</g:form>
				
			</div>
			<div class="span4" style="margin:0px;background-color:rgb(245, 245, 245);min-height:200px;">
				<h4>Related Documents</h4>
			</div>
		</div>
		<div class="row" style="margin-top:25px;">
			<div class="span4" style="min-height:200px;margin-left:11px;margin-right:15px;background-color:rgb(245, 245, 245);">
				<h4>Related People</h4>
				<ul>
					<g:each in="${people}" var="person">
						<li>
							<g:link controller="user" action="viewUserProfile" params="${[userId:person.userId]}" >
								${person.fullName}
							</g:link>
						</li>
					</g:each>
				</ul>
			</div>
			
			<div class="span4" style="margin-left:0px;margin-right:15px;background-color:rgb(245, 245, 245);min-height:200px;">
				<h4>Related Updates</h4>
				<ul>
					<g:each in="${statusUpdates}" var="update">
						<li><g:link controller="permalink" action="index" params="${[uuid:update.uuid]}">${update.uuid}</g:link></li>
					</g:each>
				</ul>
			</div>
			
			
			<div class="span4" style="margin-left:0px;margin-right:15px;background-color:rgb(245, 245, 245);min-height:200px;">
				<h4>Related Entities</h4>
				<ul>
					<g:each in="${links}" var="link">
						<li><a href="${link.href}">${link.name}</a></li>
					</g:each>
				</ul>
			</div>
			
		</div>
	</body>
</html>