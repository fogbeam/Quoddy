<div class="aseWrapper">
	
	<div class="aseAvatarBlock">
		<img src="${createLinkTo(dir:'images/', file:'business_event.jpg')}" />
	</div>
	<div class="aseTitleBar"> <!-- http://localhost:8080/quoddy/user/viewUser?userId=testuser2 -->
		<a href="${createLink(controller:'eventSubscription', action:'display', params:[subscriptionId:item.streamObject.owningSubscription.id])}">${item.streamObject.owningSubscription.name}</a>
	</div>
	<div class="activityStreamEntry"> 
		<h2>Business Subscription Event</h2>
		<p>
			<g:if test="${item.streamObject.xmlDoc != null}">
				<g:transform stylesheet="oagis3" source="${item.streamObject.xmlDoc}" factory="org.apache.xalan.processor.TransformerFactoryImpl" />
			</g:if>
			<g:else>
				<h3>Error rendering Subscription Event: Please Contact Your System Administrator</h3>
			</g:else>
		</p>
	</div>
	<div class="aseClear" >
	</div>
	<div class="aseFooter" >
		<g:formatDate date="${item.streamObject.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"/>
	</div>
</div>