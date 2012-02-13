<g:each in="${activities}" var="activity">
	<g:render template="${activity.templateName}" var="item" bean="${activity}" />
</g:each>

<div class="activityStreamFooter" style="clear:both;">
	<g:if test="${session.user}">
		<center><a href="#" id="loadMoreLink">Get More Events</a></center>
	</g:if>
</div>