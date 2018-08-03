

<%@ page import="org.fogbeam.quoddy.SemanticEnhancement" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${semanticEnhancementInstance}">
            <div class="errors">
                <g:renderErrors bean="${semanticEnhancementInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${semanticEnhancementInstance?.id}" />
                <g:hiddenField name="version" value="${semanticEnhancementInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="activityId"><g:message code="semanticEnhancement.activityId.label" default="Activity Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: semanticEnhancementInstance, field: 'activityId', 'errors')}">
                                    <g:textField name="activityId" value="${fieldValue(bean: semanticEnhancementInstance, field: 'activityId')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="stanbolInput"><g:message code="semanticEnhancement.stanbolInput.label" default="Stanbol Input" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: semanticEnhancementInstance, field: 'stanbolInput', 'errors')}">
                                    <g:textField name="stanbolInput" value="${semanticEnhancementInstance?.stanbolInput}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="stanbolOutput"><g:message code="semanticEnhancement.stanbolOutput.label" default="Stanbol Output" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: semanticEnhancementInstance, field: 'stanbolOutput', 'errors')}">
                                    
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
