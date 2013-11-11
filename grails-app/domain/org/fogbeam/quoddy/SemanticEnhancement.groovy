package org.fogbeam.quoddy

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType
import static groovyx.net.http.ContentType.URLENC

class SemanticEnhancement {

	String inputData
	String stanbolOutput
	String uuid
	Date dateUpdated

	//begin methods for calling Stanbol
	public static String queryStanbolServer(String content){
		try {
			def STANBOL_HOSTNAME = 'http://localhost:8090/'
			def ENHANCER_PATH = 'enhancer/'
			//TODO:add stanbol auth
			
			def response
			def restClient = new HTTPBuilder( STANBOL_HOSTNAME )
			response = restClient.request(Method.POST, ContentType.TEXT) {
				uri.path = ENHANCER_PATH
				body = content
				headers.'Accept' = 'application/rdf+xml'
				headers.'Content-type' = 'text/plain'
			}
			
			int r = response.read()
			StringBuilder responseText = new StringBuilder()
			while(r != -1){
				responseText.append((char)r)
				r = response.read()
			}
			return responseText.toString()
		}catch(Exception e){
			println ":::Stanbol not available"
			e.printStackTrace()
		}
	}
	public static String submitToStanbol(String content){
		String stanbolOutput = queryStanbolServer(content)
		return stanbolOutput
	}
	public static void processStanbolOutput(String stanbolOutput){
		stanbolOutput.each{
			System.out.println(it.key+" , "+it.value+".")
		}
		def semenh = SemanticEnhancement
		semenh.each {
			System.out.println(it)
		}
	}
	//end methods for calling Stanbol
		
	def submitData(String argData, String argUUID) {
		print ":::Submitting data -> "
		println "${argData}"
		this.inputData = argData
		this.uuid = argUUID
		this.save()
		String outputString = submitToStanbol(argData)
		this.stanbolOutput = outputString
		this.dateUpdated =  new Date()
		this.save
		println ":::Object Type -> ${this.stanbolOutput.getClass().getName()} -> ${outputString}"
		//this.stanbolOutput.toString()
	}

	static mapping = {
		stanbolOutput sqlType: 'text'
	}
	
    static constraints = { 
		inputData(nullable:true)
		stanbolOutput(nullable:true)
    }
}