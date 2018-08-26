package org.fogbeam.quoddy

import org.fogbeam.quoddy.controller.mixins.SidebarPopulatorMixin
import org.fogbeam.quoddy.stream.ActivityStreamItem
import org.fogbeam.quoddy.stream.StatusUpdate
import org.fogbeam.quoddy.stream.constants.EventTypes
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

import grails.plugin.springsecurity.annotation.Secured


@Mixin(SidebarPopulatorMixin)
class UserGroupController
{
	def eventStreamService;
	def userService;
	def userStreamDefinitionService;
	def userListService;
	def userGroupService;
	def businessEventSubscriptionService;
	def calendarFeedSubscriptionService;
	def activitiUserTaskSubscriptionService;
	def rssFeedSubscriptionService;
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def index()
	{
		User user = null;
		
		def userOwnedGroups = new ArrayList<UserGroup>();
		def userMembershipGroups = new ArrayList<UserGroup>();
		
		User currentUser = userService.getLoggedInUser();        
        
        Map model = [:];
        
        def tempUserOwnedGroups = userGroupService.getGroupsOwnedByUser( currentUser );
        if( tempUserOwnedGroups )
        {
            userOwnedGroups.addAll( tempUserOwnedGroups );
        }
        
        def tempUserMembershipGroups = userGroupService.getGroupsWhereUserIsMember( currentUser );
        if( tempUserMembershipGroups )
        {
            userMembershipGroups.addAll( tempUserMembershipGroups );
        }
        
        model.putAll( [user:user,
                        userOwnedGroups:userOwnedGroups,
                        userMembershipGroups:userMembershipGroups] );
                    
        Map sidebarCollections = populateSidebarCollections( this, currentUser );
        model.putAll( sidebarCollections );

        
        return model;


	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def create()
	{
		[:];	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def save()
	{
		User currentUser = userService.getLoggedInUser();
		
		log.debug( "save using params: ${params}");

        UserGroup groupToCreate = new UserGroup();
        
        groupToCreate.name = params.groupName;
        groupToCreate.description = params.groupDescription;
        groupToCreate.owner = currentUser;
        
        if( ! groupToCreate.save(flush:true) )
        {
            log.error( "Saving UserGroup FAILED");
            groupToCreate.errors.allErrors.each { log.error( it.toString() ) };
        }
    
        redirect(controller:"userGroup", action:"index");

        
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def edit()
	{
		def groupId = params.id;
		log.debug( "Editing UserGroup with id: ${groupId}");
		UserGroup groupToEdit = null;
		
		groupToEdit = UserGroup.findById( groupId );
		
		
		[groupToEdit:groupToEdit];	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def update()
	{
		log.debug( "update using params: ${params}");
		def groupId = params.groupId;
		UserGroup groupToEdit = null;
		
		groupToEdit = UserGroup.findById( groupId );
		
		groupToEdit.name = params.groupName;
		groupToEdit.description = params.groupDescription;
		if( !groupToEdit.requireJoinConfirmation )
		{
			groupToEdit.requireJoinConfirmation = false;
		}
		if( ! groupToEdit.save(flush:true) )
		{
			log.error( "saving UserGroup FAILED");
			groupToEdit.errors.allErrors.each { log.error( it.toString() ) };
		}
		
		// TODO: deal with requireJoinConfirmation
		
		
		redirect(controller:"userGroup", action:"index");
	}

    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def display()
	{        
		User currentUser = userService.getLoggedInUser();        	
		
        log.debug( "Doing display with params: ${params}");
        
        // def items = new ArrayList<StreamItemBase>();
        List<ActivityStreamItem> activities = new ArrayList<ActivityStreamItem>();

        UserGroup group = UserGroup.findById( params.groupId );
    
        // check that this group is not one of the ones that the user either
        // owns or is a member of
        List<UserGroup> userGroups = userGroupService.getAllGroupsForUser( currentUser );
        
        boolean userIsGroupMember = false;
        userGroups.each {
            if( it.id == group.id ){
                userIsGroupMember = true;
                return;
            }
        }
    
        activities = userGroupService.getRecentActivitiesForGroup( group, 25 );
        
        log.info( "activities: ${activities}");
        
        
        // items = userGroupService.getRecentEventsForGroup( group, 25 );
                
        Map model = [:];
        
        model.putAll( [ group:group,
                        user: currentUser,
                        userIsGroupMember:userIsGroupMember,
                        activities:activities] );
        
        Map sidebarCollections = populateSidebarCollections( this, currentUser );
        model.putAll( sidebarCollections );

        return model;

	}	

    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def joinGroup()
	{
		// TODO: find group, see if joinConfirmation is required, 
		// and add user to group OR add pending group request
		// for the group owner / admin

		User currentUser = userService.getLoggedInUser();
        		
		// TODO: create group Membership
		String groupId = params.groupId;
		
		log.info( "doing joinGroup with groupId = ${groupId} and userId = ${currentUser.userId}");
		UserGroup group = UserGroup.findById( groupId );
		
		group.addToGroupMembers( currentUser );
		
        if( !group.save(flush:true) )
        {
            group.errors.allErrors.each { log.error( it.toString() ) }
        }
        
        
		redirect( controller:"userGroup", action:"display", params:['groupId':groupId]);	
	}
	
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def list()
	{
		List<UserGroup> allGroups = userGroupService.getAllGroups();
		
		[allGroups: allGroups];	
	}

    
    @Secured(["ROLE_USER", "ROLE_ADMIN"])
	def postToGroup()
	{
		log.info( "Posting to group: ${params.groupId}, with statusText: ${params.statusText}");		
		def groupId = params.groupId;

		User currentUser = userService.getLoggedInUser();
		
        // get our UserGroup
        UserGroup group = userGroupService.findByGroupId( Integer.parseInt( groupId ) );
                
        /* test to see if the user is a member of the group before allowing them to post */
        // check that this group is not one of the ones that the user either
        // owns or is a member of
        
        // TODO: move this blurb of code into UserGroupService or somewhere, with a signature
        // like boolean isUserInGroup( User user, UserGroup group )
        List<UserGroup> userGroups = userGroupService.getAllGroupsForUser( currentUser );
        
        boolean userIsGroupMember = false;
        userGroups.each {
            if( it.id == group.id ){
                userIsGroupMember = true;
                return;
            }
        }
        
        if( !userIsGroupMember )
        {
            flash.message = "You can only post to a group if you are a member of the group";
        }
        else
        {
            log.debug( "constructing our new StatusUpdate object...");
            // construct a status object
            log.debug( "statusText: ${params.statusText}");
        
            StatusUpdate newStatus = new StatusUpdate( text: params.statusText, creator: currentUser );
            newStatus.effectiveDate = new Date();
            newStatus.targetUuid = group.uuid; // NOTE: can we take 'targetUuid' out of StatusUpdate??
            newStatus.name = "321BCA";
            
            
            /* TODO: add call to Stanbol to get our enhancement JSON */
            newStatus.enhancementJSON = "";
            
            if( !newStatus.save(flush:true) )
            {
                log.error( "Save StatusUpdate FAILED!");
                newStatus.errors.allErrors.each { log.debug( it ) };
            }
            
            ActivityStreamItem activity = new ActivityStreamItem(content:newStatus.text);
            activity.title = "Internal Activity";
            activity.url = new URL( "http://www.example.com" );
            activity.verb = "quoddy_group_ytstatus_update";
            activity.actorObjectType = "User";
            activity.actorUuid = currentUser.uuid;
            activity.targetObjectType = "UserGroup";
            
            activity.owner = currentUser;
            activity.published = new Date(); // set published to "now"
            activity.targetUuid = group.uuid;
            activity.streamObject = newStatus;
            activity.objectClass = EventTypes.STATUS_UPDATE.name;
                        
            // NOTE: we added "name" to EventBase, but how is it really going
            // to be used?  Do we *really* need this??
            activity.name = activity.title;
            activity.published = activity.published;
            
            eventStreamService.saveActivity( activity );
            
            
            // Map msg = new HashMap();
            // msg.creator = activity.owner.userId;
            // msg.text = newStatus.text;
            // msg.published = activity.published;
            // msg.originTime = activity.dateCreated.time;
            // msg.targetUuid = activity.targetUuid;
            
            // log.debug( "sending message to JMS" );
            // jmsService.send( queue: 'uitestActivityQueue', msg, 'standard', null );
        }

        				
		redirect( controller:"userGroup", action:"display", params:['groupId':groupId]);
	}		
}