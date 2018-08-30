package org.fogbeam.quoddy.spring.factorybean;

import java.io.File;

import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.fogbeam.quoddy.spring.template.JenaTemplate;
import org.springframework.beans.factory.FactoryBean;

public class JenaTemplateFactoryBean implements FactoryBean<JenaTemplate> 
{
	// our Spring injected fields...
	private String tdbDirectory = null;
	
	
	@Override
	public JenaTemplate getObject() throws Exception 
	{
		JenaTemplate template = new JenaTemplate();
		
		// do all the work of building up our template depending
		// on the configured options...
		
		File testFile = new File( tdbDirectory );
		
		if( !testFile.exists())
		{
			testFile.mkdirs();
		}		
		
		Dataset dataset = TDBFactory.createDataset(tdbDirectory);
		
		TDB.sync( dataset );
		
		template.setDataset(dataset);
		
		
		return template;
	}
	
	@Override
	public Class<?> getObjectType() 
	{
		return JenaTemplate.class;
	}
	
	@Override
	public boolean isSingleton() 
	{
		return true;
	}
	
	public String getTdbDirectory() {
		return tdbDirectory;
	}
	
	public void setTdbDirectory(String tdbDirectory) {
		this.tdbDirectory = tdbDirectory;
	}
	
}
