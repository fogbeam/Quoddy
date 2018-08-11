<g:each in="${activities}" var="activity">
	<g:render template="${activity.templateName}" var="item" bean="${activity}" />
</g:each>


<div id="discussDialog" title="" style="display:none;">
	
		<g:formRemote name="discussItemForm" url="[controller: 'activityStream', action:'discussItem']" onSuccess="openDiscussion(data)" >
	
			<!--  what data do we need to pass here? -->
			
			<!--  text of an (optional) comment -->
			<input name="discussItemComment" type="text" value="Add A Comment" />
	
			<!--  the uuid of the thing being shared -->
			<input id="discussItemUuid" name="discussItemUuid" type="hidden" value="" />
	
			<!--  a target userId  -->
			<!--  TODO: need a picker of some sort to select users to invite to the conference -->
			<input name="discussTargetUserId" type="text" value="" />
			<br />
			
			<!-- TODO: deal with the return from the server with our URL  -->
			
		</g:formRemote>
</div>


<div id="shareDialog" title="Share this Item" style="display:none;">
	
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
	<sec:ifLoggedIn>
		<center><a href="#" id="loadMoreLink">Get More Events</a></center>
	</sec:ifLoggedIn>
</div>