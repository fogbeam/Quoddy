package org.fogbeam.quoddy.spring.template;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;


public class JenaTemplate 
{
	private Dataset dataset = null;
	
	
	public Dataset getDataset() 
	{
		return dataset;
	}
	
	public void setDataset(Dataset dataset) 
	{
		this.dataset = dataset;
	}
	
	public Model getDefaultModel()
	{
		return this.dataset.getDefaultModel();
	}
	
	
	
}
