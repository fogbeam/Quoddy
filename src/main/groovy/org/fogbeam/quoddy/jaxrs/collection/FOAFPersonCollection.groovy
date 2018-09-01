package org.fogbeam.quoddy.jaxrs.collection

import javax.xml.bind.annotation.XmlRootElement

import org.fogbeam.foaf.api.FoafPerson

public class FOAFPersonCollection
{
	private List<FoafPerson> foafPersons = new ArrayList<FoafPerson>();
	
	
	public List<FoafPerson> getFoafPersons()
	{
		return this.foafPersons;
	}
	
	public void setFoafPersons( final List<FoafPerson> foafPersons )
	{
		this.foafPersons = foafPersons;
	}
	
	public void addAll( final List<FoafPerson> foafPersons)
	{
		this.foafPersons.addAll( foafPersons );
	}
	
}
