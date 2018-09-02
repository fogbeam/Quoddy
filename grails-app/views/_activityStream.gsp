<g:each in="${activities}" var="activity">
	<g:render template="${activity.templateName}" var="item" bean="${activity}" />
</g:each>


<!-- Modal -->
<div class="modal fade" id="discussItemModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Start OpenMeetings Discussion</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">

		<g:formRemote name="discussItemForm" url="[controller: 'activityStream', action:'discussItem']" onSuccess="openDiscussion(data,textStatus)" >

			
			<!--  the uuid of the thing being shared -->
			<input id="discussItemUuid" name="discussItemUuid" type="hidden" value="" />


			<!--  a target userId  -->
			<!--  TODO: need a picker of some sort to select users to invite to the conference -->
			<div style="margin-bottom:15px;">
				<input name="discussTargetUserId" type="text" placeholder="Select User(s)" />
			</div>
	
			<!--  what data do we need to pass here? -->
			
			<div style="margin-bottom:15px;">
				<!--  text of an (optional) comment -->
				<input name="discussItemComment" type="text" placeholder="Add A Comment" />
			</div>
		</g:formRemote>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button id="submitDiscussionLaunch" name="submitDiscussionLaunch" type="button" class="btn btn-primary">Submit</button>
      </div>
    </div>
  </div>
</div>



<!-- Modal -->
<div class="modal fade" id="shareItemModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Share Item</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">

		<g:formRemote name="shareItemForm" url="[controller: 'activityStream', action:'shareItem']">
	
			<!--  what data do we need to pass here? -->

			<!--  a target userId (later we'll support multiple targets, and different
			target types, such as UserGroups, etc.  Sharing should be bigger than
			just "share to timeline" though.  Eventually this could work similar to
			Neddick sharing and include email and XMPP support as well.  -->
			<div style="margin-bottom:15px;">	
				<input name="shareTargetUserId" type="text" placeholder="Select User(s)" />
			</div>

			<div style="margin-bottom:15px;">
				<!--  text of an (optional) comment -->
				<input name="shareItemComment" type="text" placeholder="Add A Comment" />
			</div>
			
			<!--  the uuid of the thing being shared -->
			<input id="shareItemUuid" name="shareItemUuid" type="hidden" value="" />
		</g:formRemote>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button id="submitShareItem" name="submitShareItem" type="button" class="btn btn-primary">Submit</button>
      </div>
    </div>
  </div>
</div>

<div class="activityStreamFooter" style="clear:both;">
	<sec:ifLoggedIn>
		<center><a href="#" id="loadMoreLink">Get More Events</a></center>
	</sec:ifLoggedIn>
</div>