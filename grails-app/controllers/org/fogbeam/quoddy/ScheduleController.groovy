package org.fogbeam.quoddy

import org.codehaus.groovy.grails.commons.ArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsClass
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Trigger
import org.quartz.TriggerKey
import org.quartz.impl.matchers.GroupMatcher
import org.quartz.impl.matchers.StringMatcher
import org.quartz.impl.triggers.SimpleTriggerImpl

class ScheduleController {

	def jobManagerService;
	
	def index = {
		
		// get all the "Job" Artefacts
		GrailsClass[] artefacts = grailsApplication.getArtefacts( "Job" );
		
		
		ArtefactHandler[] handlers = grailsApplication.getArtefactHandlers();
		
		Class[] artefacts2 = grailsApplication.getAllArtefacts();
		
		for( Class clazz : artefacts2 )
		{
			ArtefactHandler h = grailsApplication.getArtefactType( clazz );
		}
				
		
		[artefacts:artefacts];
			
	}

	def editSchedule =
	{
		log.debug( "editSchedule received id: ${params.id}" );
	
		List<String> jobGroups = jobManagerService.quartzScheduler.getJobGroupNames();
		def triggers = null;
		def jobGroup = null;
		def jobName = null;
		def jobFullName = null;
		for( String aJobGroup : jobGroups )
		{
			GroupMatcher groupMatcher = new GroupMatcher( aJobGroup, StringMatcher.StringOperatorName.EQUALS )
			for(JobKey aJobKey : jobManagerService.quartzScheduler.getJobKeys(groupMatcher))
			{
				JobDetail detail = jobManagerService.quartzScheduler.getJobDetail(aJobKey);
				
				log.debug( "detail: ${detail}");
				
				if( detail.key.name.contains( params.id ))
				{
					log.debug( "found a match for ${detail.fullName}");			
					triggers = jobManagerService.quartzScheduler.getTriggersOfJob(aJobKey);
					jobName = aJobKey.name
					jobGroup = aJobKey.group
					jobFullName = aJobKey.name;
				}
				
			}
		}
				
		[existingTriggers:triggers, jobGroup: jobGroup, jobName: jobName, jobFullName: jobFullName];
	}
	
	def createTrigger =
	{
		log.debug( "createTrigger:" );
		log.debug( "params: ${params}");
		
		List<String> jobGroups = jobManagerService.quartzScheduler.getJobGroupNames();
		def triggers = null;
		def jobGroup = null;
		def jobName = null;
		def jobFullName = null;
		for( String aJobGroup : jobGroups )
		{
			GroupMatcher groupMatcher = new GroupMatcher( aJobGroup, StringMatcher.StringOperatorName.EQUALS )
			
			for(JobKey aJobKey : jobManagerService.quartzScheduler.getJobKeys(groupMatcher))
			{

				log.debug( "aJobKey: ${aJobKey}");
				
				JobDetail detail = jobManagerService.quartzScheduler.getJobDetail(aJobKey);
					
				log.debug( "jobDetail: ${detail}");
				log.debug( "params.id = ${params.id}");
				
				if( detail?.key?.name?.contains( params.id ))
				{						
					triggers = jobManagerService.quartzScheduler.getTriggersOfJob(aJobKey);
					jobName = aJobKey.name;
					jobGroup = aJobKey.group;
					jobFullName = detail.description;
				}
				else {
					log.debug( "no job detail or no fullname found!" );
				}
			}
		}
				
		[existingTriggers:triggers, jobGroup: jobGroup, jobName: jobName, jobFullName:jobFullName];
			
	}
	
	def addTrigger =
	{
		String jobGroup = params.jobGroup;
		String jobName = params.jobName;
		String recurrenceInterval = params.recurrenceInterval;
		
		log.debug( "adding Trigger for jobName: ${jobName}" );
		
		GrailsClass jobClass = grailsApplication.getArtefact( "Job", jobName );
		
		if( jobClass == null )
		{
			log.error( "Could not load GrailsClass for ${jobName}" );
		}
		else
		{
			// SimpleTrigger(String name, String group, int repeatCount, long repeatInterval)
			Trigger trigger = new SimpleTriggerImpl( params.triggerName, params.triggerGroup, Integer.parseInt(params.repeatCount), Long.parseLong(params.recurrenceInterval));
			// jobClass.newInstance().schedule( Long.parseLong( recurrenceInterval ), SimpleTrigger.REPEAT_INDEFINITELY, sparams );
			jobClass.newInstance().schedule( trigger );
		}

		
		redirect(action:"index");
	}
	
	def executeJobNow =
	{
		GrailsClass jobClass = grailsApplication.getArtefact( "Job", params.jobName );
		jobClass.newInstance().triggerNow();
		
		redirect(action:"index");
	}
	
	def editTrigger =
	{
		log.debug( "Edit Trigger, params: ${params}" );
		
		Trigger theTrigger = jobManagerService.quartzScheduler.getTrigger( new TriggerKey( params.triggerName, params.triggerGroup ));
		[trigger: theTrigger];
	}

	
	def deleteTrigger =
	{
		jobManagerService.quartzScheduler.unscheduleJob( new TriggerKey( params.triggerName, params.triggerGroup ) );
		redirect(action:"index");
	}
		
	def saveTrigger =
	{
		TriggerKey theKey = new TriggerKey( params.oldTriggerName, params.oldTriggerGroup )
		Trigger theTrigger = jobManagerService.quartzScheduler.getTrigger(theKey);
		Trigger newTrigger = theTrigger.clone();
		newTrigger.name = params.triggerName;
		newTrigger.group = params.triggerGroup;
		newTrigger.repeatInterval = Long.parseLong( params.recurrenceInterval );
		jobManagerService.quartzScheduler.rescheduleJob( theKey, newTrigger);
			
		redirect(action:"index");
	}
}
