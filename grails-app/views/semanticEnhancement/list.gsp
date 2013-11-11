
<%@ page import="org.fogbeam.quoddy.SemanticEnhancement" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'semanticEnhancement.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="activityId" title="${message(code: 'semanticEnhancement.activityId.label', default: 'Activity Id')}" />
                        
                            <g:sortableColumn property="stanbolInput" title="${message(code: 'semanticEnhancement.stanbolInput.label', default: 'Stanbol Input')}" />
                        
                            <g:sortableColumn property="stanbolOutput" title="${message(code: 'semanticEnhancement.stanbolOutput.label', default: 'Stanbol Output')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${semanticEnhancementInstanceList}" status="i" var="semanticEnhancementInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${semanticEnhancementInstance.id}">${fieldValue(bean: semanticEnhancementInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: semanticEnhancementInstance, field: "activityId")}</td>
                        
                            <td>${fieldValue(bean: semanticEnhancementInstance, field: "stanbolInput")}</td>
                        
                            <td>${fieldValue(bean: semanticEnhancementInstance, field: "stanbolOutput")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${semanticEnhancementInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
