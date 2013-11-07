package org.fogbeam.quoddy

class SemanticEnhancementController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [semanticEnhancementInstanceList: SemanticEnhancement.list(params), semanticEnhancementInstanceTotal: SemanticEnhancement.count()]
    }

    def create = {
        def semanticEnhancementInstance = new SemanticEnhancement()
        semanticEnhancementInstance.properties = params
        return [semanticEnhancementInstance: semanticEnhancementInstance]
    }

    def save = {
        def semanticEnhancementInstance = new SemanticEnhancement(params)
        if (semanticEnhancementInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement'), semanticEnhancementInstance.id])}"
            redirect(action: "show", id: semanticEnhancementInstance.id)
        }
        else {
            render(view: "create", model: [semanticEnhancementInstance: semanticEnhancementInstance])
        }
    }

    def show = {
        def semanticEnhancementInstance = SemanticEnhancement.get(params.id)
        if (!semanticEnhancementInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement'), params.id])}"
            redirect(action: "list")
        }
        else {
            [semanticEnhancementInstance: semanticEnhancementInstance]
        }
    }

    def edit = {
        def semanticEnhancementInstance = SemanticEnhancement.get(params.id)
        if (!semanticEnhancementInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [semanticEnhancementInstance: semanticEnhancementInstance]
        }
    }

    def update = {
        def semanticEnhancementInstance = SemanticEnhancement.get(params.id)
        if (semanticEnhancementInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (semanticEnhancementInstance.version > version) {
                    
                    semanticEnhancementInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement')] as Object[], "Another user has updated this SemanticEnhancement while you were editing")
                    render(view: "edit", model: [semanticEnhancementInstance: semanticEnhancementInstance])
                    return
                }
            }
            semanticEnhancementInstance.properties = params
            if (!semanticEnhancementInstance.hasErrors() && semanticEnhancementInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement'), semanticEnhancementInstance.id])}"
                redirect(action: "show", id: semanticEnhancementInstance.id)
            }
            else {
                render(view: "edit", model: [semanticEnhancementInstance: semanticEnhancementInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def semanticEnhancementInstance = SemanticEnhancement.get(params.id)
        if (semanticEnhancementInstance) {
            try {
                semanticEnhancementInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'semanticEnhancement.label', default: 'SemanticEnhancement'), params.id])}"
            redirect(action: "list")
        }
    }
}
