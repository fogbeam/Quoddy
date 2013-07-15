<g:each in="${activities}" var="activity">
	<g:render template="${activity.templateName}" var="item" bean="${activity}" />
</g:each>


<div id="dialog" title="Share this Item">
	
	<g:formRemote name="shareItemForm" url="[controller: 'activityStream', action:'shareItem']">
	
		<!--  what data do we need to pass here? -->
	
		<!--  text of an (optional) comment -->
		<input name="shareItemComment" type="text" value="Add A Comment" />
	
		<!--  the uuid of the thing being shared -->
		<input id="shareItemUuid" name="shareItemUuid" type="hidden" value="" />
	
		<!--  a target userId (later we'll support multiple targets, and different
		target types, such as UserGroups, etc.  Sharing should be bigger than
		just "share to timeline" though.  Eventually this could work similar to
		Neddick sharing and include email and XMPP support as well.  -->
	
		<input name="shareTargetUserId" type="text" value="" />
		<br />
	</g:formRemote>
</div>

<div class="activityStreamFooter" style="clear:both;">
	<shiro:authenticated>
		<center><a href="#" id="loadMoreLink">Get More Events</a></center>
	</shiro:authenticated>
</div>