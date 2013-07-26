<g:each in="${activities}" var="activity">
	<g:render template="${activity.templateName}" var="item" bean="${activity}" />
	<b>:)</b>
</g:each>
<g:each in="${se}" status="i" var="trip">
	<b>hi</b>
</g:each>

<div id="dialog" title="Basic dialog">
<p>This is an animated dialog which is useful for displaying information. 
The dialog window can be moved, resized and closed with the 'x' icon.</p>
</div>

<div class="activityStreamFooter" style="clear:both;">
	<g:if test="${session.user}">
		<center><a href="#" id="loadMoreLink">Get More Events</a></center>
	</g:if>
</div>