<g:each in="${activities}" var="activity">
	<g:render template="${activity.templateName}" var="item" bean="${activity}" />
</g:each>


<div id="dialog" title="Basic dialog">
<p>This is an animated dialog which is useful for displaying information. 
The dialog window can be moved, resized and closed with the 'x' icon.</p>
</div>

<div class="activityStreamFooter" style="clear:both;">
	<shiro:authenticated>
		<center><a href="#" id="loadMoreLink">Get More Events</a></center>
	</shiro:authenticated>
</div>