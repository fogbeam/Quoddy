<div class="aseWrapper" id="aseWrapper.${item.uuid}">

	<!-- begin aseTitleBar -->
	<div class="aseTitleBar">
		<!-- begin aseAvatarBlock -->
		<div class="aseAvatarBlock">
			<img
				src="${createLink(controller:'profilePic',action:'thumbnail',id:item.userId)}" />
		</div>
		<!-- end aseAvatarBlock -->				
	</div>
	<!-- end aseTitleBar -->
	
	<div>
		<div class="viewUserButtons">
			<div class="btn-group">
				<g:link controller="user" action="viewUser" params="[userId:item.userId]" elementid="btn1" name="btn1" type="button" class="btn btn-info btn-small">Timeline</g:link>
				<g:link controller="user" action="viewUserProfile" params="[userId:item.userId]" elementid="btn2" name="btn2" type="button" class="btn btn-info btn-small">Profile</g:link>
				<button id="btnAddToFriends.${item.userId}" name="btnAddToFriends.${item.userId}" type="button" class="btn btn-info btn-small">Add To Friends</button>
				<button id="btnFollowUser.${item.userId}" name="btnFollowUser.${item.userId}" type="button" class="btn btn-info btn-small">Follow User</button>
				<button id="btnAddAnnotation.${item.userId}" name="btnAddAnnotation.${item.userId}" type="button" class="btn btn-info btn-small">Annotate</button>
			</div>		
		</div>
		<p />		
		
		<div style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 60px; width: 150px; vertical-align: top;">
			<label>Full Name:</label>
			<span name="fullName" id="fullName" >
				<a href="${createLink(controller:'user', action:'viewUserProfile', params:[userId:item.userId])}">${item.fullName}</a>
			</span>				
		</div>		
		<div style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 60px; width: 150px; vertical-align: top;">
			<label>Title:</label>
			<g:if
				test="${item.profile?.title == null || item.profile.title?.isEmpty() }">
				<span name="title" id="title"></span>
			</g:if>
			<g:else>
				<span name="title" id="title"> ${item.profile.title}
				</span>
			</g:else>
		</div>
		<div style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 60px; width: 150px; vertical-align: top;">
			<label>Description:</label>
			<g:if
				test="${item.profile?.summary == null || item.profile.summary?.isEmpty() }">
				<span name="description" id="description"></span>
			</g:if>
			<g:else>
				<span name="description" id="description"> ${item.profile.summary}
				</span>
			</g:else>
		</div>		
		<div style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 60px; width: 150px; vertical-align: top;">
			<label>Phone:</label>
			<g:if
				test="${item.profile?.primaryPhoneNumber == null || item.profile.primaryPhoneNumber.address?.isEmpty() }">
				<span name="phone" id="phone"></span>
			</g:if>
			<g:else>
				<span name="phone" id="phone"> ${item.profile.primaryPhoneNumber.address}
				</span>
			</g:else>
		</div>
		
		<div />
		
		<!-- 	
			Instant Messenger:
			Email:
			Location:
			.plan
		-->
		<div style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 60px; width: 150px; vertical-align: top;">
			<label>Instant Messenger:</label>

				<g:if
					test="${item.profile?.primaryInstantMessenger == null || item.profile.primaryInstantMessenger?.address.isEmpty() }">
					<span name="primaryInstantMessenger" id="primaryInstantMessenger"
						style="display: inline;"></span>
				</g:if>
				<g:else>
					<span name="primaryInstantMessenger" id="primaryInstantMessenger"
						style="display: inline;"> ${item.profile.primaryInstantMessenger.address}
					</span>
				</g:else>
		</div>
		
		<div
			style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 150px; vertical-align: top;">
			<label>Email:</label>
			<g:if
				test="${item.profile?.primaryEmailAddress == null || item.profile.primaryEmailAddress?.address.isEmpty() }">
				<span name="primaryEmail" id="primaryEmail"
					style="display: inline;"></span>
			</g:if>
			<g:else>
				<span name="primaryEmail" id="primaryEmail"
					style="display: inline;"> ${item.profile.primaryEmailAddress.address}
				</span>
			</g:else>

		</div>

		<div
			style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 150px; vertical-align: top;">
			<label>Location:</label>

			<g:if
				test="${item.profile?.location == null || item.profile.location?.isEmpty() }">
				<span name="location" id="location" style="display: inline;"></span>
			</g:if>
			<g:else>
				<span name="location" id="location" style="display: inline;">
					${item.profile.location}
				</span>
			</g:else>
		</div>

		<div
			style="display: inline-block; margin-left: 20px; margin-top: 15px; height: 110px; width: 150px; vertical-align: top;">
			<label>.plan</label>

			<g:if
				test="${item.profile?.dotPlan == null || item.profile.dotPlan?.isEmpty() }">
				<span name="dotPlan" id="dotPlan" style="display: inline;"></span>
			</g:if>
			<g:else>
				<span name="dotPlan" id="dotPlan" style="display: inline;">
					${item.profile.dotPlan}
				</span>
			</g:else>

		</div>
	</div>
</div>