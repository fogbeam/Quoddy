<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img src="${createLinkTo(dir:'images/flavour-icons', file:'speed_mph.png')}" />
	</div>
	<div class="aseTitleBar"> <!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
		<a href="${createLink(controller:'user', action:'viewUser', params:[userId:item.owner.userId])}">${item.owner.fullName}</a>
	</div>
	<div class="activityStreamEntry"> 
		<font color="red">BUSINESS SUBSCRIPTION EVENT</font>
		<p>
			<g:transform stylesheet="oagis3" source="${item.xmlDoc}" factory="org.apache.xalan.processor.TransformerFactoryImpl" />
		</p>
	</div>
	<div class="aseClear" >
	</div>
	<div class="aseFooter" >
		<g:formatDate date="${item.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"/>
	</div>
</div>