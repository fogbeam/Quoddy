<html>
    <head>
        <title>Welcome to Grails</title>
        <meta name="layout" content="admin" />
    </head>
    <body>
        <div id="bodyContent" style="height:600px;margin-left:100px;">
            <h1>Edit Trigger</h1>
            <p />
            <div>	
            	<g:form  controller="schedule" action="saveTrigger" >
            		<g:hiddenField name="jobName" value="${jobName}"/>
            		<g:hiddenField name="jobGroup" value="${jobGroup}"/>
            		<g:hiddenField name="oldTriggerName" value="${trigger.name}" />
            		<g:hiddenField name="oldTriggerGroup" value="${trigger.group}" />
            		<table>
	            		<tr>
		            		<td>
		            			<label for="triggerName">Trigger Name:</label>
		            		</td>
		            		<td>
		            			<g:textField name="triggerName" value="${trigger.name}"></g:textField>            		
		            		</td>
		            	</tr>
	            		<tr>
		            		<td>
		            			<label for="triggerGroup">Trigger Group:</label>
		            		</td>
		            		<td>
		            			<g:textField name="triggerGroup" value="${trigger.group}"></g:textField>            		
		            		</td>
		            	</tr>
		            	<tr>
		            		<td>
		            			<label for="recurrenceInterval">Recurrence Interval:</label>
		            		</td>
		            		<td>
			            		<g:textField name="recurrenceInterval" value="${trigger.repeatInterval}"></g:textField>
		    				</td>
		    			</tr>
		            	<tr>
		            		<td>
		            			<label for="repeatCount">Repeat Count:</label>
		            		</td>
		            		<td>
			            		<g:textField name="repeatCount" value="${trigger.repeatCount}"></g:textField>
		    				</td>
		    			</tr>
		    			
            		</table>
            		<g:submitButton name="saveTrigger" value="Save">Save</g:submitButton>
            	</g:form>
            </div>
            
        </div>
    </body>
</html>
