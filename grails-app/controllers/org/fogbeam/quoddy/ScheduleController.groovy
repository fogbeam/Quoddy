package org.fogbeam.quoddy

import java.util.List

import org.codehaus.groovy.grails.commons.ArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsClass
import org.quartz.JobDetail
import org.quartz.SimpleTrigger
import org.quartz.Trigger

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
		println( "editSchedule received id: ${params.id}" );
	
		List<String> jobGroups = jobManagerService.quartzScheduler.getJobGroupNames();
		def triggers = null;
		def jobGroup = null;
		def jobName = null;
		def jobFullName = null;
		for( String aJobGroup : jobGroups )
		{
			for(String aJobName : jobManagerService.quartzScheduler.getJobNames(aJobGroup))
			{
				JobDetail detail = jobManagerService.quartzScheduler.getJobDetail(aJobName, aJobGroup);
				
				println "detail: ${detail}";
				
				if( detail.fullName.contains( params.id ))
				{
					println "found a match for ${detail.fullName}";			
					triggers = jobManagerService.quartzScheduler.getTriggersOfJob(aJobName, aJobGroup);
					jobName = aJobName;
					jobGroup = aJobGroup;
					jobFullName = detail.fullName;
				}
				
			}
		}
				
		[existingTriggers:triggers, jobGroup: jobGroup, jobName: jobName, jobFullName: jobFullName];
	}
	
	def createTrigger =
	{
		log.debug( "createTrigger:" );
		
		List<String> jobGroups = jobManagerService.quartzScheduler.getJobGroupNames();
		def triggers = null;
		def jobGroup = null;
		def jobName = null;
		def jobFullName = null;
		for( String aJobGroup : jobGroups )
		{
			for(String aJobName : jobManagerService.quartzScheduler.getJobNames(aJobGroup))
			{

				JobDetail detail = jobManagerService.quartzScheduler.getJobDetail(aJobName, aJobGroup);
					
				if( detail?.fullName?.contains( params.id ))
				{						
					triggers = jobManagerService.quartzScheduler.getTriggersOfJob(aJobName, aJobGroup);
					jobName = aJobName;
					jobGroup = aJobGroup;
					jobFullName = detail.fullName;
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
			Trigger trigger = new SimpleTrigger( params.triggerName, params.triggerGroup, Integer.parseInt(params.repeatCount), Long.parseLong(params.recurrenceInterval));
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
		
		Trigger theTrigger = jobManagerService.quartzScheduler.getTrigger(params.triggerName, params.triggerGroup);
		[trigger: theTrigger];
	}

	
	def deleteTrigger =
	{
		jobManagerService.quartzScheduler.unscheduleJob(params.triggerName, params.triggerGroup);
		redirect(action:"index");
	}
		
	def saveTrigger =
	{

		Trigger theTrigger = jobManagerService.quartzScheduler.getTrigger(params.oldTriggerName, params.oldTriggerGroup);
		Trigger newTrigger = theTrigger.clone();
		newTrigger.name = params.triggerName;
		newTrigger.group = params.triggerGroup;
		newTrigger.repeatInterval = Long.parseLong( params.recurrenceInterval );
		jobManagerService.quartzScheduler.rescheduleJob(params.oldTriggerName, params.oldTriggerGroup, newTrigger);
			
		redirect(action:"index");
	}
}
